package com.gonext.callreminder.checkupdate;

import android.app.Activity;


public class UpdateChecker implements ASyncCheckResult, UpdateCheckerResult {

    private  Activity mActivity;
    private UpdateCheckerResult mLibraryResultCallBack;
    private static ASyncCheckResult mCheckResultCallback;
    private static VersionCallListener versionCallListener;

    public UpdateChecker(Activity activity) {
        mActivity = activity;
        mCheckResultCallback = this;
        mLibraryResultCallBack = this;
    }

    public UpdateChecker(Activity activity, UpdateCheckerResult updateCheckerResult) {
        mActivity = activity;
        mCheckResultCallback = this;
        mLibraryResultCallBack = updateCheckerResult;
    }


    /**
     * Start the process
     */
    public static void start(String packageName, VersionCallListener versionCallListener1) {
        versionCallListener = versionCallListener1;
        ASyncCheck asyncTask = new ASyncCheck( mCheckResultCallback, packageName);
        asyncTask.execute();
    }

    /**
     * If the library found a version available on the Store, and it's different from the installed one, notify it to the user.
     *
     * @param versionDownloadable String to compare to the version installed of the app.
     */
    @Override
    public void versionDownloadableFound(String versionDownloadable,String updatedDate) {
        if (VersionComparator.isVersionDownloadableNewer(mActivity, versionDownloadable,updatedDate)) {
            mLibraryResultCallBack.foundUpdateAndShowIt(versionDownloadable);
            versionCallListener.versionCall(versionDownloadable,updatedDate,true);
        } else { // No new update available
            mLibraryResultCallBack.returnUpToDate(versionDownloadable);
            versionCallListener.versionCall(versionDownloadable,updatedDate,false);
        }
    }

    /**
     * Can't get the versionName from Play Store.
     * See #1 error.
     *
     * @see <a href="https://github.com/rampo/UpdateChecker/issues/1">Issue #1</a>
     */
    @Override
    public void multipleApksPublished() {
        mLibraryResultCallBack.returnMultipleApksPublished();
    }

    /**
     * Can't download the store page.
     */
    @Override
    public void networkError() {
        mLibraryResultCallBack.returnNetworkError();
        versionCallListener.versionCall("","",false);
    }

    /**
     * Can't find the store page for this app.
     */
    @Override
    public void appUnpublished() {
        mLibraryResultCallBack.returnAppUnpublished();
    }

    /**
     * The check returns null for new version downloadble
     */
    @Override
    public void storeError() {
        mLibraryResultCallBack.returnStoreError();
    }

    /**
     * versionDownloadable isn't equal to manifest versionName -> New update available.
     * Show the Notice because it's the first time or the number of the checks made is a multiple of the argument of setSuccessfulChecksRequired(int) method. (If you don't call setSuccessfulChecksRequired(int) the default is 5).
     *
     * @param versionDownloadable version downloadable from the Store.
     */
    @Override
    public void foundUpdateAndShowIt(String versionDownloadable) {
       /* if (mNotice == Notice.NOTIFICATION) {

        } else if (mNotice == Notice.DIALOG) {

        }*/
    }

    /**
     * versionDownloadable is equal to manifest versionName -> No new update available.
     * Don't show the Notice
     *
     * @param versionDonwloadable version downloadable from the Store.
     */
    @Override
    public void returnUpToDate(String versionDonwloadable) {
    }

    @Override
    public void returnMultipleApksPublished() {
    }

    @Override
    public void returnNetworkError() {
    }

    @Override
    public void returnAppUnpublished() {
    }

    @Override
    public void returnStoreError() {

    }

}
