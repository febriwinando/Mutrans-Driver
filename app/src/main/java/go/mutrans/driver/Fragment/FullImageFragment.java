package go.mutrans.driver.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.mbms.DownloadRequest;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import go.mutrans.driver.R;

public class FullImageFragment extends Fragment {

    private Context context;

    private String chat_id;
    private ProgressBar progressBar;

    private ProgressDialog progressDialog;
    private DownloadRequest prDownloader;

    private File fullpath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View getView = inflater.inflate(R.layout.fragment_full_image, container, false);
        context = getContext();

        Checkstoragepermision();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        String imageUrl = requireArguments().getString("image_url");
        assert getArguments() != null;
        chat_id = getArguments().getString("chat_id");
        Button savebtn2 = getView.findViewById(R.id.savebtn2);

        ImageView closeGallery = getView.findViewById(R.id.close_gallery);
        closeGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(context, R.style.DialogStyle);
        progressDialog.setMessage("Please Wait");

//        PRDownloader.initialize(getActivity().getApplicationContext());

        fullpath = new File(Environment.getExternalStorageDirectory() + "/ourride/" + chat_id + ".jpg");

        Button savebtn = getView.findViewById(R.id.savebtn);
        if (fullpath.exists()) {
            savebtn.setVisibility(View.GONE);
            savebtn2.setVisibility(View.VISIBLE);
        }

        File direct = new File(Environment.getExternalStorageDirectory() + "/ourride/");
//
//        prDownloader = PRDownloader.download(imageUrl, direct.getPath(), chat_id + ".jpg")
//                .build()
//                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
//                    @Override
//                    public void onStartOrResume() {
//
//                    }
//                })
//                .setOnPauseListener(new OnPauseListener() {
//                    @Override
//                    public void onPause() {
//
//                    }
//                })
//                .setOnCancelListener(new OnCancelListener() {
//                    @Override
//                    public void onCancel() {
//
//                    }
//                })
//                .setOnProgressListener(new OnProgressListener() {
//                    @Override
//                    public void onProgress(Progress progress) {
//
//                    }
//                });



        progressBar = getView.findViewById(R.id.progress);
        ImageView singleImage = getView.findViewById(R.id.single_image);

        if (fullpath.exists()) {
//            Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse(fullpath.getAbsolutePath());
            singleImage.setImageURI(uri);
        } else {
//            Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(context).load(imageUrl).placeholder(R.drawable.image_placeholder)
                    .into(singleImage);

            progressBar.setVisibility(View.GONE);

        }


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, imageUrl, Toast.LENGTH_SHORT).show();
                try {
                    URL url = new URL(imageUrl);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    saveImageToExternal(chat_id, image);
                } catch(IOException e) {
                    System.out.println(e);
                }
//                Savepicture(false);
            }
        });

        ImageButton sharebtn = getView.findViewById(R.id.sharebtn);
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePicture();
            }
        });


        return getView;
    }

    public void saveImageToExternal(String imgName, Bitmap bm) throws IOException {
        //Create Path to save Image
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/Mutrans"); //Creates app specific folder
        path.mkdirs();
        File imageFile = new File(path, imgName+".png"); // Imagename.png
        FileOutputStream out = new FileOutputStream(imageFile);
        try{
            bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
            out.flush();
            out.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(context,new String[] { imageFile.getAbsolutePath() }, null,new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
        } catch(Exception e) {
            throw new IOException();
        }
    }

    private void SharePicture() {
        if (Checkstoragepermision()) {
            Uri bitmapuri;
            if (fullpath.exists()) {
                bitmapuri = Uri.parse(fullpath.getAbsolutePath());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, bitmapuri);
                startActivity(Intent.createChooser(intent, ""));
            } else {
                Savepicture(true);
            }

        }
    }

    private void Savepicture(final boolean isfromshare) {
        if (Checkstoragepermision()) {

            final File direct = new File(Environment.getExternalStorageDirectory() + "/DCIM/ourride/");
            progressDialog.show();
//            prDownloader.start(new OnDownloadListener() {
//                @Override
//                public void onDownloadComplete() {
//                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    intent.setData(Uri.parse(direct.getPath() + chat_id + ".jpg"));
//                    context.sendBroadcast(intent);
//                    progressDialog.dismiss();
//                    if (isfromshare) {
//                        SharePicture();
//                    } else {
//                        new AlertDialog.Builder(context, R.style.DialogStyle)
//                                .setTitle("Image Saved")
//                                .setMessage(fullpath.getAbsolutePath())
//                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                    }
//                                })
//                                .show();
//                    }
//                }
//
//                @Override
//                public void onError(Error error) {
//                    progressDialog.dismiss();
//                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
//
//                }
//
//
//            });
        }
        progressDialog.dismiss();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Click Again", Toast.LENGTH_LONG).show();
        }
    }

    private boolean Checkstoragepermision() {
        if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;

        } else {

            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }


}


