package go.mutrans.driver.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtils {


    public static void saveImageToGallery(Context context, Bitmap bitmap, String folderName, String fileName) {
        String savedImagePath = saveImageToInternalStorage(context, bitmap, folderName, fileName);
        if (savedImagePath != null) {
            addToGallery(context, savedImagePath);
            Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }

    private static String saveImageToInternalStorage(Context context, Bitmap bitmap, String folderName, String fileName) {
        String savedImagePath = null;

        String imageFileName = "IMG_" + fileName + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), folderName);

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }

        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
                savedImagePath = null;
            }
        } else {
            savedImagePath = null;
        }

        return savedImagePath;
    }

    private static void addToGallery(Context context, String imagePath) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, imagePath.hashCode());
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, "Image");
        values.put("_data", imagePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
