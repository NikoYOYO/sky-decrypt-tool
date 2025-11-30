package com.zhiduoshao.tool;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.os.Build;

public class FileUtil {
    
    public static String getPathFromUri(Context context, Uri uri) {
        if (uri == null) return null;

        String scheme = uri.getScheme();
        if (scheme == null) return null;

        if ("file".equalsIgnoreCase(scheme)) {
            return uri.getPath();
        }

        if ("content".equalsIgnoreCase(scheme)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (DocumentsContract.isDocumentUri(context, uri)) {
                    return getPathFromDocumentUri(context, uri);
                }
            }
            return getPathFromContentUri(context, uri);
        }

        return null;
    }

    private static String getPathFromDocumentUri(Context context, Uri uri) {
        String docId = DocumentsContract.getDocumentId(uri);
        String[] split = docId.split(":");
        
        if (split.length >= 2) {
            String type = split[0];
            String id = split[1];

            if ("primary".equalsIgnoreCase(type)) {
                return android.os.Environment.getExternalStorageDirectory() + "/" + id;
            }
        }

        return getPathFromContentUri(context, uri);
    }

    private static String getPathFromContentUri(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
