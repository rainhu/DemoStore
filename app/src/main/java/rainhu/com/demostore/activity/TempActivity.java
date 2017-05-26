package rainhu.com.demostore.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RecoverySystem;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rainhu.com.demostore.R;

/**
 * Created by huzhengyu on 17-1-17.
 */

public class TempActivity extends Activity {
    Context mContext;

    @InjectView(R.id.alertDialogBtn)
    Button alertDialogBtn;

    private Handler mHandler;
    private HandlerThread mHandlerTHread;

//    @OnClick(R.id.factoryData)
//    public void onFactoryDataBtnClicked(){
//        File fileName = new File("/dev/block/mmcblk0p3");
//        String factorydata  = null;
//        try {
//            FileInputStream fis = new FileInputStream(fileName);
//            fis.read(factorydata.getBytes());
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @OnClick(R.id.alertDialogBtn)
    public void onALertDialogBtnClicked() {
        try {
            RecoverySystem.installPackage(this, new File("/data/update.zip"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.alert_dialog, null);
        Button btn = (Button) view.findViewById(R.id.openDialogBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder alert2 = new AlertDialog.Builder(mContext,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                alert2.setTitle("dialog 2");
//                alert2.setMessage("bbb");
//                //alert2.show();
//
//                AlertDialog dialog = alert2.create();
//                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
//                dialog.setOnDismissListener( new DialogInterface.OnDismissListener() {
//                    public void onDismiss( DialogInterface dialog ) {
//                        // debugging
//                    }
//                } );
//                dialog.setOnCancelListener( new DialogInterface.OnCancelListener() {
//                    public void onCancel( DialogInterface dialog ) {
//                        // debugging
//                    }
//                } );

                //dialog.show();
                // share();
              //  startActivity(new Intent(mContext, TestActivity.class));
                share();




            }
        });
        alert.setTitle("dialog 1")
                .setMessage("aaa")
                .setView(view)
                .setPositiveButton("new dialog", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alert.show();
        */
    }

    private void share() {
        Uri imageUri = Uri.parse("content://media/external/images/media/52");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        //intent.putExtra(Intent.EXTRA_STREAM, imageUri);

        startActivity(Intent.createChooser(intent, "share"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_temp);
        ButterKnife.inject(this);
        mHandlerTHread = new HandlerThread("cameratest");
        mHandlerTHread.start();
        mHandler = new Handler(mHandlerTHread.getLooper());

        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            final LinkedBlockingQueue<String> availableEventQueue = new LinkedBlockingQueue<>();
            final LinkedBlockingQueue<String> unavailableEventQueue = new LinkedBlockingQueue<>();

            CameraManager.AvailabilityCallback ac = new CameraManager.AvailabilityCallback(){
                @Override
                public void onCameraAvailable(@NonNull String cameraId) {
                    availableEventQueue.offer(cameraId);
                }

                @Override
                public void onCameraUnavailable(@NonNull String cameraId) {
                    unavailableEventQueue.offer(cameraId);
                }
            };
            cameraManager.registerAvailabilityCallback(ac, mHandler);



            String [] cameraIdList = cameraManager.getCameraIdList();
            for (String s : cameraIdList){
                Log.i("ryan", "cameraId:"+s);
            }
            Log.i("ryan","availableEventQueueSize:"+availableEventQueue.size()+" unavailableEventQueueSize:"+unavailableEventQueue.size());


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

}
