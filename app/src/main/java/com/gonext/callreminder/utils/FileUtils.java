package com.gonext.callreminder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.OrientationEventListener;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gonext.callreminder.utils.logger.CustomLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import static com.gonext.callreminder.utils.StaticData.AD_FILE_NAME;

/**
 * Created by Lenovo.
 */

public class FileUtils {

    public static final int ORIENTATION_HYSTERESIS = 5;
    public static final int PROFILE_IMAGE_SIZE = 450;
    private static final boolean DEBUG = false; // Set to true to enable logging
    public static final int MEDIA_TYPE_IMAGE = 1;

    public static String PROJECT_PARENT_FOLDER = Environment.getExternalStorageDirectory() + "/demo/";


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
    public static int getJpegRotation(int cameraId, int orientation) {
        // See android.hardware.Camera.Parameters.setRotation for
        // documentation.
        int rotation = 0;
        if (orientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                rotation = (info.orientation - orientation + 360) % 360;
            } else {  // back-facing camera
                rotation = (info.orientation + orientation) % 360;
            }

            CustomLog.info("Utils", "info.orientation= " + info.orientation + " orientation=" + orientation + " rotation=" + rotation);
        }
        return rotation;
    }

    public static int roundOrientation(int orientation, int orientationHistory) {
        boolean changeOrientation = false;
        if (orientationHistory == OrientationEventListener.ORIENTATION_UNKNOWN) {
            changeOrientation = true;
        } else {
            int dist = Math.abs(orientation - orientationHistory);
            dist = Math.min(dist, 360 - dist);
            changeOrientation = (dist >= 45 + ORIENTATION_HYSTERESIS);
        }
        if (changeOrientation) {
            return ((orientation + 45) / 90 * 90) % 360;
        }
        return orientationHistory;
    }

    public static String getUniqueImageFilename() {
        return System.currentTimeMillis() + ".jpg";
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(String.valueOf(new File(uri.getPath())), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(String.valueOf(new File(uri.getPath())), options);
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG)
                    DatabaseUtils.dumpCursor(cursor);

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static Bitmap getImageFromCamera(Context mContext, Uri IMAGE_CAPTURE_URI) {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (manufacturer.equalsIgnoreCase("samsung") || model.equalsIgnoreCase("samsung")) {
            int rotation = getCameraPhotoOrientation(mContext, IMAGE_CAPTURE_URI, IMAGE_CAPTURE_URI.getPath());
            CustomLog.info("Rotate", String.valueOf(rotation));
            Matrix matrix = new Matrix();

            if (model.equalsIgnoreCase("GT-I9500")) {
                if (rotation == 0)
                    matrix.postRotate(90);
                else
                    matrix.postRotate(rotation);
            } else {
                matrix.postRotate(rotation);
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            /*if (options.outWidth < PROFILE_IMAGE_SIZE && options.outHeight < PROFILE_IMAGE_SIZE) {
                Bitmap bitmap;
                bitmap = BitmapFactory.decodeFile(IMAGE_CAPTURE_URI.getPath(), options);
                return bitmap;

            } else {*/
            final Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_CAPTURE_URI.getPath(), options);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            //}

        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            if (options.outWidth < PROFILE_IMAGE_SIZE && options.outHeight < PROFILE_IMAGE_SIZE) {
                Bitmap bitmap = null;

                try {
                    InputStream inputStream = new FileInputStream(new File(IMAGE_CAPTURE_URI.getPath()));
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //bitmap = BitmapFactory.decodeFile(IMAGE_CAPTURE_URI.getPath(), options);
                return bitmap;

            } else {
                final Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_CAPTURE_URI.getPath(), options);
                return Bitmap.createBitmap(bitmap, 0, 0, PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE);
            }
        }
    }


    public static Uri getOutputMediaFileUri(Context context,int type) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return  FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(type));
        }

        try {
            return Uri.fromFile(getOutputMediaFile(type));
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "demo_Profile");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + System.currentTimeMillis() + ".jpeg");
        } else {
            return null;
        }
        return mediaFile;
    }

    /**
     * This method is used get orientation of camera photo
     *
     * @param context context
     * @param imageUri  This parameter is Uri type
     * @param imagePath This parameter is String type
     * @return rotate
     */
    private static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            try {
                if (imageUri != null)
                    context.getContentResolver().notifyChange(imageUri, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    rotate = 0;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }



    public static String rotateImageAndGetNewPath(Context mContext, String filePath ) {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String finalFilePath = "";
        OutputStream outputStream;
        Uri IMAGE_CAPTURE_URI = Uri.parse(filePath);
        if (manufacturer.equalsIgnoreCase("samsung") || model.equalsIgnoreCase("samsung")) {
            int rotation = getCameraPhotoOrientation(mContext, IMAGE_CAPTURE_URI, IMAGE_CAPTURE_URI.getPath());
            CustomLog.info("Rotate", String.valueOf(rotation));
            Matrix matrix = new Matrix();

            if (model.equalsIgnoreCase("GT-I9500")) {
                if (rotation == 0)
                    matrix.postRotate(90);
                else
                    matrix.postRotate(rotation);
            } else {
                matrix.postRotate(rotation);
            }

            final Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_CAPTURE_URI.getPath());
            if (bitmap == null){
                finalFilePath = IMAGE_CAPTURE_URI.getPath();
                return finalFilePath;
            }
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            /*try {
                outputStream = mContext.getContentResolver().openOutputStream(IMAGE_CAPTURE_URI);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/

            File newFile = saveImageToSDCard(rotatedBitmap,IMAGE_CAPTURE_URI,mContext);
            assert newFile != null;
            finalFilePath = newFile.getPath();
            return finalFilePath;

        } else {
            finalFilePath = IMAGE_CAPTURE_URI.getPath();
            return finalFilePath;
        }
    }

    private static File saveImageToSDCard(Bitmap bitmap, Uri IMAGE_CAPTURE_URI, Context context) {
        try {
            String current;
            current = new File(IMAGE_CAPTURE_URI.getPath()).getName();
            File file;
           /* File myNewFolder = new File(IMAGE_CAPTURE_URI.getPath());
            if (!myNewFolder.isDirectory())
                myNewFolder.mkdir();*/

            String[] segments = IMAGE_CAPTURE_URI.getPath().split("/");
            String idStr = segments[segments.length-1];

            file = new File(IMAGE_CAPTURE_URI.getPath().replace(current,""), current);

            if (file.exists())
                file.delete();

            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

           /* final File tempFile = file;
            MediaScannerConnection.scanFile(context, new String[]{Uri.fromFile(file).getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String newFilepath, Uri uri) {
                    //tempFile = new File(uri.getPath());
                }
            });*/
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean  isImageFromUri(String path){

        if (TextUtils.isEmpty(path))
            return  false;
        return path.contains(".jpg") ||
                path.contains(".jpeg") ||
                path.contains(".png") ||
                path.contains(".JPG") ||
                path.contains(".PNG") ||
                path.contains(".JPEG") ||
                path.contains("gif");
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.indexOf("image") == 0;
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.indexOf("video") == 0;
    }

    public static String getMediaPathFromSDCard(String filePath, String folderPath) {
        File file = new File(filePath);
        File path = new File(folderPath);
        if (!path.exists()){
            path.mkdirs();
        }
        String mediaPath ="";
        if (file.exists()) {
            mediaPath = file.getAbsolutePath();
        } else {
            mediaPath = "";
        }
        return mediaPath;
    }

    public static void loadImageFromPath(ImageView ivAds, String url, String appName, TextView tvAds, Activity activity) {
        if (tvAds != null && ivAds != null) {
            tvAds.setText(appName);
            try {
                Glide.with(activity)
                        .load(url)
                        .centerCrop()
                        .dontAnimate()
                        .thumbnail(0.2f)
                        .into(ivAds);
            } catch (Exception e) {   e.printStackTrace();         }
        }
    }


    public static void writeJsonFileToStoreAdResponse(Context context, String mJsonResponse) {
        try {
            FileWriter file = new FileWriter("/data/data/" + context.getApplicationContext().getPackageName() + "/" + AD_FILE_NAME);
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readeDataFromFile(Context context) {

        String mResponse ="";
        try {
            File file = new File("/data/data/" + context.getPackageName() + "/" + AD_FILE_NAME);
            FileInputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            mResponse = new String(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mResponse;
    }

    public static boolean deleteDataFile(Context context) {

        File file = new File("/data/data/" + context.getPackageName() + "/" + AD_FILE_NAME);

        if (file.exists()){
            file.delete();
            return true;
        }
        return false;
    }
}
