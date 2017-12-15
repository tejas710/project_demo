package com.gonext.callreminder.utils.view;

public class CustomViewUtils {

    public static String setFont(int textFont,boolean isInEditMode) {
        String textFontType = null;
        if (!isInEditMode) {
            switch (textFont) {
                case 1:
                    textFontType = "novecentosanswide_bold.otf";
                    break;
                case 2:
                    textFontType = "novecentosanswide_book.otf";
                    break;
                case 3:
                    textFontType = "novecentosanswide_medium.otf";
                    break;
                case 4:
                    textFontType = "novecentosanswide_normal.otf";
                    break;
                default:
                    textFontType = "novecentosanswide_normal.otf";
                    break;
            }

        }
        return textFontType;

    }
}