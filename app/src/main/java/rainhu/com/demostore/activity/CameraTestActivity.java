package rainhu.com.demostore.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Size;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rainhu.com.demostore.R;

/**
 * Created by rainhu on 17-4-14.
 */

public class CameraTestActivity extends Activity {
    public static String TAG = "CameraTestActivity";
    final int CAMERA_PERMISSION_REQUEST_CODE = 91;
    CameraManager mCamaraManager;
    Handler mBackgroundHandler, mMainHandler;
    HandlerThread mBackgroundThread;
    CameraDevice mCameraDevice;
    CameraCaptureSession mCameraCaptureSession;
    SurfaceHolder mSurfaceHolder = null;
    ImageReader mImageReader;
    boolean isPreviewMode = false;
    String mCameraId;

    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    CaptureRequest.Builder previewRequesrBuilder;

    private SurfaceHolder.Callback2 mSurfaceHolderCallback = new SurfaceHolder.Callback2(){
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            openCamera();
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if(null != mCameraDevice){
                mCameraDevice.close();
                CameraTestActivity.this.mCameraDevice = null;
            }
        }
        @Override
        public void surfaceRedrawNeeded(SurfaceHolder holder) {
        }
    };


    private ImageReader.OnImageAvailableListener mImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
             //执行mCameraCaptureSession.capture，数据会输出到mImageView
            Log.i(TAG, "onImageAvailable");
            mSurfaceView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);

            Image image = reader.acquireNextImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes); //将缓冲区的数据存到字节数组中
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if(null != bitmap){
                mImageView.setImageBitmap(bitmap);
            } else {
                Toast.makeText(CameraTestActivity.this, "获取照片失败！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    ///为了使照片竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @InjectView(R.id.btn_switch_camera)
    Button switch_camera;

    @InjectView(R.id.btn_capture)
    Button capture;

    @InjectView(R.id.sv_camera)
    SurfaceView mSurfaceView; //用来显示camera preview

    @InjectView(R.id.iv_show_camera)
    ImageView mImageView; //用来显示拍摄的照片

    @InjectView(R.id.btn_preview)
    Button preview;

    @OnClick(R.id.btn_switch_camera)
    public void onSwitchCamaraBtnClicked(){
        Log.i(TAG , "onSwitchCamaraBtnClicked");
        if(!isPreviewMode){
            return;
        }
        if(mCameraDevice == null) {
            return;
        }
        if(mCameraDevice.getId().equals("1")){
            mCameraId = "0";
        }else{
            mCameraId = "1";
        }
        mCameraDevice.close();
        openCamera();
    }

    @OnClick(R.id.btn_capture)
    public void onCaptureBtnClicked(){
        Log.i(TAG, "onCaptureBtnClicked");
        takePicture();
    }

    @OnClick(R.id.sv_camera)
    public void onSurfaceCameraClicked(){
        Log.i(TAG, "onSurfaceCameraClicked");
        takePicture();
    }

    @OnClick(R.id.btn_preview)
    public void onPreviewBtnClicked(){
        Log.i(TAG, "onPreviewBtnClicked");

        if(isPreviewMode) return;
        mImageView.setVisibility(View.GONE);
        mSurfaceView.setVisibility(View.VISIBLE);
        takePreview();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameratest);
        ButterKnife.inject(this);
        mCamaraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
        startBackgroundThread();
        if(mSurfaceHolder == null){
            Log.i(TAG, "mSurfaceHolder == null");
            //surface还没有被创建
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(mSurfaceHolderCallback);
        } else {
            //surface已经被创建
            Log.i(TAG, "mSurfaceHolder != null");
            openCamera();

        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("cameraBackgroud");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        mMainHandler = new Handler(getMainLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause");
        closeCamera();
        stopBackgroundThread();
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
            mMainHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void initCameraOutput(){
        Log.i(TAG,"initCameraOutput");
        try {
            CameraCharacteristics characteristics = mCamaraManager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
            Log.i(TAG, "MAX size : ["+largest.getWidth()+", "+largest.getHeight()+"]");
            //mImageReader = ImageReader.newInstance(largest.getWidth(),largest.getHeight(), ImageFormat.JPEG, 2);

            mImageReader = ImageReader.newInstance(1080,1920, ImageFormat.JPEG, 1);
            mImageReader.setOnImageAvailableListener(mImageAvailableListener, mMainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void requestCameraPermission(){
        Log.i(TAG, "requestCameraPermission");
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            //用户曾经拒绝过权限的请求，而且没有勾选不再询问，可以在这里添加为什么程序无法正常使用
            Toast.makeText(this, "请勾选Camera权限", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void openCamera() {
        Log.i(TAG, "openCamera");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }
        initCameraOutput();

        try {
            if(!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)){
                throw new RuntimeException("Time out waiting to lock camera openging.");
            }
            mCamaraManager.openCamera(mCameraId, deviceStateCallback, mMainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void closeCamera(){
        Log.i(TAG,"closeCamera");
        try {
            mCameraOpenCloseLock.acquire();
            if(null != mCameraCaptureSession){
                mCameraCaptureSession.close();
                mCameraCaptureSession = null;
            }

            if(null != mCameraDevice){
                mCameraDevice.close();
                mCameraDevice = null;
            }

            if(null != mImageReader){
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       switch (requestCode){
           case CAMERA_PERMISSION_REQUEST_CODE: {
               // 如果请求被取消了，那么结果数组就是空的
               if(grantResults.length > 0
                       && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   //获得了权限

                   //openCamera(mCameraId, stateCallback ,mainHandler); will be do in onResume
               }else{
                   Toast.makeText(this, "没有拍照权限", Toast.LENGTH_SHORT).show();
               }
               return;
           }
       }

    }

    //用来获取CameraDevice打开的状态的回调函数
    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraOpenCloseLock.release();
            mCameraDevice = camera;
            takePreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            mCameraOpenCloseLock.release();

            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }
        @Override
        public void onError(CameraDevice camera, int error) {
            mCameraOpenCloseLock.release();
            mCameraDevice = null;
            Toast.makeText(CameraTestActivity.this, "开启摄像头失败", Toast.LENGTH_SHORT).show();
        }

    };


    private CameraCaptureSession.StateCallback captureSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            if (null == mCameraDevice){
                return;
            }
            Log.i(TAG, session == null ? "session is null" : "session is not null");

            //mCameraDevice.close();
            mCameraCaptureSession = session;
            //自动对焦
            previewRequesrBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            CaptureRequest previewRequest = previewRequesrBuilder.build();
            try {
                mCameraCaptureSession.setRepeatingRequest(previewRequest, null, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Toast.makeText(CameraTestActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
        }
    };

    private void takePreview() {
        Log.i(TAG, "takePreview");
        if (null == mCameraDevice) {
            return;
        }
        isPreviewMode = true;
        try {
            //设立一个CaptureRequest.Builder
            previewRequesrBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequesrBuilder.addTarget(mSurfaceHolder.getSurface());

            //创建CaptureSession，该对象负责处理预览请求和拍照请求
            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReader.getSurface()), captureSessionStateCallback, mBackgroundHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        private void takePicture(){
            if(mCameraDevice == null ) {
                return;
            }
            isPreviewMode = false;
            // 创建拍照需要的CaptureRequest.Builder
            final CaptureRequest.Builder captureRequestBuilder;
            try {
                captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                // 将imageReader的surface作为CaptureRequest.Builder的目标
                captureRequestBuilder.addTarget(mImageReader.getSurface());
                // 自动对焦
                captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                // 自动曝光
                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                // 获取手机方向
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                // 根据设备方向计算设置照片的方向
                captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
                //拍照
                CaptureRequest mCaptureRequest = captureRequestBuilder.build();
                mCameraCaptureSession.capture(mCaptureRequest, null, mMainHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCameraDevice != null) {
            mCameraDevice.close();
        }
    }

    static class CompareSizesByArea implements Comparator<Size>{

        @Override
        public int compare(Size o1, Size o2) {
            return Long.signum((long)o1.getWidth()*o1.getHeight() - (long)o2.getWidth()*o2.getHeight()) ;
        }
    }
}
