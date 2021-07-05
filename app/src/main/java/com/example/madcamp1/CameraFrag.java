package com.example.madcamp1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class CameraFrag extends Fragment {
    private View view;

    private static final String TAG = "camera";
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private ImageView imageView;
    private boolean inProgress; //process flag

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.camera_frag,container,false);
        imageView = view.findViewById(R.id.imageView);
        surfaceView = view.findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceListener);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //permission checkbox
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},1);
        }



        Button shootButton = view.findViewById(R.id.button3);
        shootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camera!=null && inProgress==false){
                    camera.takePicture(null,null, takePicture);
                    inProgress=true;
                }
            }
        });
        return view;
    }


    private  Camera.PictureCallback takePicture = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera){
            Log.i(TAG, "shoot button pressed");
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotated_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            imageView.setImageBitmap(rotated_bitmap);

            //save image to local storage
            String imageSaveUri = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),rotated_bitmap, "image save", " saved");
            Uri uri = Uri.parse(imageSaveUri);
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.makeText(getContext(), "image save success!", Toast.LENGTH_SHORT).show();

            //preview image
            camera.startPreview();
            inProgress = false;
        }
    };

    private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback(){

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(width, height);
            camera.stopPreview();
            camera.setDisplayOrientation(90);
            camera.startPreview();
            Log.i(TAG, "camera preview on");
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder){
            camera = Camera.open();
            Log.i(TAG, "camera start");
            try{
                camera.setPreviewDisplay(holder);

                //해당도 setting
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

                camera.setParameters(parameters);

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder){
            camera.release();
            camera = null;
            Log.i(TAG, "camera off");
        }
    };
}
