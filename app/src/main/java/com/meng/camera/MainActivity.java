package com.meng.camera;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    private final static int CODE_FOR_WRITE_PERMISSION=22;
    private final static String[] permissionStr={Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    private AutoFitTextureView autoFitTextureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoFitTextureView=findViewById(R.id.auto_fit_texture_view);
        checkSelfPermissions(permissionStr);
    }


    private void initCamera2(){

    }
    /**
     * 检查权限,回调的结果还
     */
    public void checkSelfPermissions(String[] permissionStr){
        if(permissionStr!=null){
            List<String> permissions= Arrays.asList(permissionStr);
            Iterator<String> permissionIterator=permissions.iterator();
            List<String> permissionList=new ArrayList<>();
            while(permissionIterator.hasNext()){
                String next=permissionIterator.next();
                int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(),
                        next);
                if(hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED){
                    permissionList.add(next);
                }
            }
            onPermissionGranted(permissionList);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //通过requestCode来识别是否同一个请求
        List<String> permissionList=new ArrayList<>();
        if (requestCode == CODE_FOR_WRITE_PERMISSION){
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    permissionList.add(permissions[i]);
                }
            }
            onPermissionGranted(permissionList);
        }
    }
    private CameraController mCameraController;
    /**
     *
     * @param permissionList
     */
    private void onPermissionGranted(List<String> permissionList) {
        if(permissionList.size()==0){
            mCameraController = CameraController.getInstance(this);
            mCameraController.setFolderPath(Environment.getExternalStorageDirectory() + "/test");
            mCameraController.initCamera(autoFitTextureView);
        }else{
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    permissionList.toArray(new String[permissionList.size()]),
                    CODE_FOR_WRITE_PERMISSION);
        }
    }

}
