package rainhu.com.demostore.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogRecord;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rainhu.com.demostore.R;
import rainhu.com.demostore.temp.MyGcmTaskService;

/**
 * Created by huzhengyu on 17-1-17.
 */

public class TempActivity extends Activity {
    Context mContext;

    @InjectView(R.id.alertDialogBtn)
    Button alertDialogBtn;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private GcmNetworkManager mGcmNetworkManager;

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        mGcmNetworkManager = GcmNetworkManager.getInstance(this);

        GcmNetworkManager mGcmNetworkManager = GcmNetworkManager.getInstance(this);
//        PeriodicTask task = new PeriodicTask.Builder()
//                .setService(MyGcmTaskService.class)
//                .setTag(MyGcmTaskService.TAG)
//                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
//                .setPersisted(true)
//                .setPeriod(10L)
//                .build();

        Task task = new OneoffTask.Builder()
                .setService(MyGcmTaskService.class)
                .setExecutionWindow(0,30)
                .setTag(MyGcmTaskService.TAG)
                .setUpdateCurrent(false)
                .setRequiredNetwork(Task.NETWORK_STATE_UNMETERED)
                .setRequiresCharging(false)
                .build();

        mGcmNetworkManager.schedule(task);



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


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Temp Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
