package com.gonext.callreminder.checkupdate;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.gonext.callreminder.utils.TimeUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class VersionComparator {
    /**
     * Compare the string versionDownloadable to the version installed of the app.
     *
     * @param versionDownloadable String to compare to the version installed of the app.
     *                            @param updatedDate last updatedDate.
     */
    static boolean isVersionDownloadableNewer(Activity mActivity, String versionDownloadable,String updatedDate) {
        String versionInstalled = "";
        String versionDateInstalled = "";
        try {
            versionInstalled = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionName;
            versionDateInstalled = String.valueOf(mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).lastUpdateTime);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        if (versionInstalled.equalsIgnoreCase(versionDownloadable)) { // If it is equal, no new version downloadable
            return false;
        } else {
            return versionCompareNumerically(versionInstalled,versionDownloadable ,versionDateInstalled,updatedDate); // Return if the versionDownloadble is newer than the installed
        }
    }

    /**
     * Method to compare date and version. we give first priority to version check. if any thing un expected result found then we will compare date.
     */
    private static boolean versionCompareNumerically(String str1, String str2,String deviceVersionDate, String playstoreVersionDate) {
        long versionLong;
        try {
            versionLong = Integer.parseInt(str2.replace(".",""));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            versionLong = 0;
        }

        if (versionLong > 0) {
            String[] vals1 = str1.split("\\.");
            String[] vals2 = str2.split("\\.");
            List<String> lstVal1 = new LinkedList<>(Arrays.asList(vals1));
            List<String> lstVal2 = new LinkedList<>(Arrays.asList(vals2));
            return compareVersion(lstVal1, lstVal2);
        }else{
            return  compareTwoDates(deviceVersionDate, playstoreVersionDate);
        }
    }

    private static boolean compareTwoDates(String deviceVersionDate, String playstoreVersionDate) {

        String deviceDate = "";
        try {
            deviceDate = TimeUtils.getDateInDayFormat(Long.valueOf(deviceVersionDate));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(deviceDate)) {
            long deviceInstalledDate = 0;
            long playVersionDate = 0;
            try {
                deviceInstalledDate = TimeUtils.getDateInLong(deviceDate);
                playVersionDate = TimeUtils.getDateInLong(playstoreVersionDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return playVersionDate > deviceInstalledDate;
        }
        return false;
    }

    private static boolean compareVersion(List<String> lstInstalled, List<String> lstPlayStore){
        int installed = 0;
        int playstoreVersion = 0;

        try {
            if (Integer.parseInt(lstPlayStore.get(0)) >Integer.parseInt(lstInstalled.get(0)) ){
                return true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (lstInstalled.size() > lstPlayStore.size()){

            try {
                for (int i =0; i <= lstInstalled.size()-1;i++) {
                    int lenghtOfPlaystore = 0;
                    if (i <= lstPlayStore.size() - 1) {
                        lenghtOfPlaystore = lstPlayStore.get(i).length();
                    }
                    int lenghtOfInstalled = lstInstalled.get(i).length();

                    if (lenghtOfInstalled > lenghtOfPlaystore) {

                        for (int j = 0; j < lenghtOfInstalled; j++) {
                            if (j == lenghtOfPlaystore) {
                                int zeroCount = lenghtOfPlaystore - j;
                                if (zeroCount < 0) {
                                    zeroCount = 0;
                                }
                                StringBuilder stringBuilder = null;

                                if (i <= lstPlayStore.size() - 1) {
                                    stringBuilder = new StringBuilder(lstPlayStore.get(i));
                                } else {
                                    stringBuilder = new StringBuilder();
                                }
                                for (int k = 0; k <= zeroCount; k++) {
                                    stringBuilder.append("0");
                                }
                                if (lenghtOfPlaystore == 0) {
                                    lstPlayStore.add(stringBuilder.toString());
                                } else {
                                    lstPlayStore.set(i, stringBuilder.toString());
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (lstInstalled.size() < lstPlayStore.size()){

            try {
                for (int i =0; i <= lstPlayStore.size();i++){
                    int lenghtOfInstalled = 0;
                    if (i<=lstInstalled.size()-1) {
                        lenghtOfInstalled = lstInstalled.get(i).length();
                    }
                    int lenghtOfPlaystore = lstPlayStore.get(i).length();

                    if (lenghtOfInstalled < lenghtOfPlaystore){

                        for (int j=0;j<lenghtOfPlaystore;j++){
                            if (j == lenghtOfInstalled){
                                int zeroCount = lenghtOfInstalled - j;
                                if (zeroCount <0){
                                    zeroCount =0;
                                }
                                StringBuilder stringBuilder =null;

                                if (i<=lstInstalled.size()-1){
                                    stringBuilder = new StringBuilder(lstInstalled.get(i));
                                }else{
                                    stringBuilder = new StringBuilder();
                                }
                                for (int k = 0 ;k <= zeroCount; k ++){
                                    stringBuilder.append("0");
                                }
                                if (lenghtOfInstalled == 0){
                                    lstInstalled.add(stringBuilder.toString());
                                }else {
                                    lstInstalled.set(i, stringBuilder.toString());
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            for (int i =0;i<lstInstalled.size();i++){
                if (i == 0 ){
                    installed = Integer.parseInt(lstInstalled.get(i));
                    playstoreVersion = Integer.parseInt(lstPlayStore.get(i));
                }else {
                    installed = Integer.valueOf(String.valueOf(installed).concat(lstInstalled.get(i)));
                    playstoreVersion = Integer.valueOf(String.valueOf(playstoreVersion).concat(lstPlayStore.get(i)));
                }
                if (installed < playstoreVersion){
                    return  true;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
