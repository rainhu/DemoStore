package com.ryan.demostore.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Button;

import java.io.FileOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.ryan.demostore.R;

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

    public static byte[] StringToAsciiBytes(String content) {
        int max = content.length();
        byte[] result = new byte[max];
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            result[i] = (byte)(c);
        }
        return result;
    }

    private void writefile(){
        String filename = "/proc/breathled";
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(filename);
            //echo 1 2 0 2 0 0 0 1 2 > proc/breathled
            String command = "2 2 0 2 0 0 0 1 3";
            byte[] bytes = StringToAsciiBytes(command);
            fos.write(bytes);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.alertDialogBtn)
    public void onALertDialogBtnClicked(View v) {

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification ;
//        notification.set
//        setContentText（）
//        setContentTitle（）
//        setSmallIcon（）

//        notification.flags = Notification.FLAG_SHOW_LIGHTS;

        notification = new Notification.Builder(mContext)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle("aaa")
                .setContentText("bbb")
                .setColor(0xFFff0000)
                //.setLights(0xFFff0000,350,300)
                .build();
        notification.ledARGB = 0xFFff0000;
        notification.ledOnMS = 350;
		notification.ledOffMS = 300;
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        nm.notify(112, notification);

//        File file = Environment.getExternalStorageDirectory();
//        FileOutputStream fo = null;
//
//        try {
//            fo = new FileOutputStream("sdcard/1.txt");
//            fo.write(2);
//        } catch (IOException e){
//            Log.i("Ryan",e.toString());
//            Toast.makeText(mContext,"file:"+file.toString() + "1.txt",Toast.LENGTH_SHORT).show();
//        } finally {
//            if (fo != null) {
//                try {
//                    fo.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
    //    }
        //FileOutputStream fo = new FileOutputStream("")

        //writefile();
        /*
        try {
            RecoverySystem.installPackage(this, new File("/data/update.zip"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

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



//        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//        try {
//            final LinkedBlockingQueue<String> availableEventQueue = new LinkedBlockingQueue<>();
//            final LinkedBlockingQueue<String> unavailableEventQueue = new LinkedBlockingQueue<>();
//
//            CameraManager.AvailabilityCallback ac = new CameraManager.AvailabilityCallback(){
//                @Override
//                public void onCameraAvailable(@NonNull String cameraId) {
//                    availableEventQueue.offer(cameraId);
//                }
//
//                @Override
//                public void onCameraUnavailable(@NonNull String cameraId) {
//                    unavailableEventQueue.offer(cameraId);
//                }
//            };
//            cameraManager.registerAvailabilityCallback(ac, mHandler);
//
//            String [] cameraIdList = cameraManager.getCameraIdList();
//            for (String s : cameraIdList){
//                Log.i("ryan", "cameraId:"+s);
//            }
//            Log.i("ryan","availableEventQueueSize:"+availableEventQueue.size()+" unavailableEventQueueSize:"+unavailableEventQueue.size());
//
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
    }
}
