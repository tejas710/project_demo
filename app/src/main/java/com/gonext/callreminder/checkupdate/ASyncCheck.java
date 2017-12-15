package com.gonext.callreminder.checkupdate;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


class ASyncCheck extends AsyncTask<String, Integer, Integer> {
    private final String PLAY_STORE_ROOT_WEB = "https://play.google.com/store/apps/details?id=";
    private final String PLAY_STORE_HTML_TAGS_TO_GET_RIGHT_POSITION = "itemprop=\"softwareVersion\"> ";
    private final String PLAY_STORE_HTML_TAG_UPDATED_DATE = "itemprop=\"datePublished\">";
    private final String PLAY_STORE_HTML_TAGS_TO_REMOVE_USELESS_CONTENT = "  </div> </div>";
    private final String PLAY_STORE_HTML_TAGS_TO_REMOVE_DIV_CONTENT = "</div> </div>";
    private final String PLAY_STORE_PACKAGE_NOT_PUBLISHED_IDENTIFIER = "We're sorry, the requested URL was not found on this server.";

    private final int VERSION_DOWNLOADABLE_FOUND = 0;
    private final int MULTIPLE_APKS_PUBLISHED = 1;
    private final int NETWORK_ERROR = 2;
    private final int PACKAGE_NOT_PUBLISHED = 3;
    private final int STORE_ERROR = 4;
    private ASyncCheckResult mResultInterface;
    private String mVersionDownloadable;
    private String packageName;
    private String lastUpdatedDate;

    ASyncCheck(ASyncCheckResult resultInterface, String packageName) {
        this.mResultInterface = resultInterface;
        this.packageName = packageName;
    }

    @Override
    protected Integer doInBackground(String... notused) {
        try {

            String url = PLAY_STORE_ROOT_WEB + packageName;

            URL object = new URL(url.replace(" ","%20"));
            HttpsURLConnection connection = (HttpsURLConnection) object.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(60 * 1000);
            connection.setConnectTimeout(60 * 1000);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();

            InputStream is = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(PLAY_STORE_HTML_TAG_UPDATED_DATE) || line.contains(PLAY_STORE_HTML_TAGS_TO_GET_RIGHT_POSITION)) {
                    if (line.contains(PLAY_STORE_HTML_TAG_UPDATED_DATE)) {
                        String containingDate = line.substring(line.lastIndexOf(PLAY_STORE_HTML_TAG_UPDATED_DATE) + 25);  // Get the String starting with version available + Other HTML tags
                        String[] removingUnusefulTags = containingDate.split(PLAY_STORE_HTML_TAGS_TO_REMOVE_DIV_CONTENT); // Remove useless HTML tags
                        lastUpdatedDate = removingUnusefulTags[0]; // Obtain version available
                    }
                    if (line.contains(PLAY_STORE_HTML_TAGS_TO_GET_RIGHT_POSITION)){
                        String containingVersion = line.substring(line.lastIndexOf(PLAY_STORE_HTML_TAGS_TO_GET_RIGHT_POSITION) + 28);  // Get the String starting with version available + Other HTML tags
                        String[] removingUnusefulTags = containingVersion.split(PLAY_STORE_HTML_TAGS_TO_REMOVE_USELESS_CONTENT); // Remove useless HTML tags
                        mVersionDownloadable = removingUnusefulTags[0]; // Obtain version available
                    }
                }else if (line.contains(PLAY_STORE_PACKAGE_NOT_PUBLISHED_IDENTIFIER)) { // This packages has not been found in Play Store
                    return PACKAGE_NOT_PUBLISHED;
                }
            }

            if (mVersionDownloadable == null &&  lastUpdatedDate == null) {
                return STORE_ERROR;
            }else if (lastUpdatedDate != null){
                return  VERSION_DOWNLOADABLE_FOUND;
            }else {
                if (containsNumber(mVersionDownloadable))
                    return VERSION_DOWNLOADABLE_FOUND;
                else
                    return STORE_ERROR;
            }
        } catch (IOException connectionError) {
            return NETWORK_ERROR;
        }
    }

    /**
     * Return to UpdateChecker class to work with the versionDownloadable if the library found it.
     *
     * @param result Integer
     */
    @Override
    protected void onPostExecute(Integer result) {
        if (result == VERSION_DOWNLOADABLE_FOUND) {
            // versionCall.versionCall(mVersionDownloadable, true);
            if (!TextUtils.isEmpty(mVersionDownloadable)) {
                try {
                    Integer.parseInt(mVersionDownloadable.replace(".", ""));
                    mResultInterface.versionDownloadableFound(mVersionDownloadable, lastUpdatedDate);
                } catch (NumberFormatException e) {
                    mResultInterface.versionDownloadableFound("0", lastUpdatedDate);
                }
            }else{
                mResultInterface.versionDownloadableFound("0", lastUpdatedDate);
            }
        } else if (result == NETWORK_ERROR) {
            mResultInterface.networkError();
        } else if (result == MULTIPLE_APKS_PUBLISHED) {
            mResultInterface.multipleApksPublished();
        } else if (result == PACKAGE_NOT_PUBLISHED) {
            mResultInterface.appUnpublished();
        } else if (result == STORE_ERROR) {
            mResultInterface.storeError();
        }
    }

    /**
     * Since the library check from the Desktop Web Page of the app the "Current Version" field, if there are different apks for the app,
     * the Play Store will shown "Varies depending on the device", so the library can't compare it to versionName installed.
     *
     * @see <a href="https://github.com/rampo/UpdateChecker/issues/1">Issue #1</a>
     */
    private boolean containsNumber(String string) {
        try {
            return string.matches("..[0-9]..");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
