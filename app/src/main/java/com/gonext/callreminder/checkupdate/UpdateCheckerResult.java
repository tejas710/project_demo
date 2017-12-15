package com.gonext.callreminder.checkupdate;


interface UpdateCheckerResult {
    /**
     * versionDonwloadable isn't equal to manifest versionName -> New update available.
     * Show the Notice because it's the first time or the number of the checks made is a multiple of the argument of setSuccessfulChecksRequired(int) method. (If you don't call setSuccessfulChecksRequired(int) the default is 5).
     *
     * @param versionDonwloadable version downloadable from the Store.
     */
    void foundUpdateAndShowIt(String versionDonwloadable);

    /**
     * versionDonwloadable is equal to manifest versionName -> No new update available.
     * Don't show the Notice
     *
     * @param versionDonwloadable version downloadable from the Store.
     */
    void returnUpToDate(String versionDonwloadable);

    /**
     * Can't get the versionName from the Store.
     * See #1
     *
     * @see <a href="https://github.com/rampo/UpdateChecker/issues/1">Issue #1</a>
     */
    void returnMultipleApksPublished();

    /**
     * Can't download the store page.
     */
    void returnNetworkError();

    /**
     * Can't find the store page for this app.
     */
    void returnAppUnpublished();

    /**
     * The check returns null for new version downloadble
     */
    void returnStoreError();
}