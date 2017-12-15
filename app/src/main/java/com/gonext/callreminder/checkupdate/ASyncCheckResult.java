package com.gonext.callreminder.checkupdate;


public interface ASyncCheckResult {

    /**
     * If the library found a version available on the Store, and it's different from the installed one, notify it to the user.
     *
     * @param versionDownloadable String to compare to the version installed of the app.
     *                            @param updatedDate last updated date
     */
    void versionDownloadableFound(String versionDownloadable, String updatedDate);

    /**
     * Can't get the versionName from the Store.
     * See #1
     *
     * @see <a href="https://github.com/rampo/UpdateChecker/issues/1">Issue #1</a>
     */
    void multipleApksPublished();

    /**
     * Can't download the store page.
     */
    void networkError();

    /**
     * Can't find the store page for this app.
     */
    void appUnpublished();

    /**
     * The check returns null for new version downloadble
     */
    void storeError();

}