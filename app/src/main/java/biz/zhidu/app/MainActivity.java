package biz.zhidu.app;
import biz.zhidu.zdsdk.FDInfo;
import biz.zhidu.zdsdk.RecordInfo;
import biz.zhidu.zdsdk.VideoStreamsView;
import biz.zhidu.zdsdk.ZhiduEye;
import biz.zhidu.zdsdk.ZhiduEyeAgent;

import android.content.Intent;
import android.net.ParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ZhiduEye _zhiduEye;

    private ZhiduEyeAgent _zhiduAgent;

    private LinearLayout _layout;

    private FDInfo[] _zhiduFDs;

    private  String  eqid="340200-340200200000100006-0001-0001";

    private VideoStreamsView _view;
    
    RecordInfo[]   recordInfos;
    long gPlayRcHandle = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonOne = (Button) findViewById(R.id.btnStart);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                _zhiduFDs = _zhiduAgent.getFDInfoList();
                if (_zhiduFDs != null && _zhiduFDs.length > 0) {
                    _zhiduAgent.startMonitorChannel(_view,
                            eqid
                            //_zhiduChannels[0].UniqueID,
                    );
                }
            }
        });

        Button buttonPTZ = (Button) findViewById(R.id.btnPTZ);
        buttonPTZ.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                _zhiduAgent.controlCamera(
                        eqid,
                        _zhiduAgent.CAMERA_CONTROL_ACTION_LEFT,
                        (byte)0
                        //_zhiduChannels[0].UniqueID,
                );
                _zhiduAgent.controlCamera(
                        eqid,
                        _zhiduAgent.CAMERA_CONTROL_ACTION_STOP,
                        (byte)0
                        //_zhiduChannels[0].UniqueID,
                );
            }
        });

        Button btnPTZ2 = (Button) findViewById(R.id.btnPTZ2);
        btnPTZ2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                _zhiduAgent.controlCamera(
                        eqid,
                        _zhiduAgent.CAMERA_CONTROL_ACTION_RIGHT,
                        (byte)0
                        //_zhiduChannels[0].UniqueID,
                );
                _zhiduAgent.controlCamera(
                        eqid,
                        _zhiduAgent.CAMERA_CONTROL_ACTION_STOP,
                        (byte)0
                        //_zhiduChannels[0].UniqueID,
                );
            }
        });

        Button btnPTZ3 = (Button) findViewById(R.id.btnPTZ3);
        btnPTZ3.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                _zhiduAgent.controlCamera(
                        eqid,
                        _zhiduAgent.CAMERA_CONTROL_ACTION_STOP,
                        (byte)0
                        //_zhiduChannels[0].UniqueID,
                );
            }
        });

        Button buttonStop = (Button) findViewById(R.id.btnStop);
        buttonStop.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                _zhiduAgent.stopMonitorChannel(
                        eqid
                        //_zhiduChannels[0].UniqueID,
                );
                Log.e("stop monitor","success");
                //startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });

        Button btnQRecord = (Button) findViewById(R.id.btnQRecord);
        btnQRecord.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                int num=  _zhiduAgent.startQueryRecord( eqid, false, getDate("2017/08/02 00:00:00"),getDate("2017/08/02 09:00:00"));
                Log.e("G","record size is "+ num);
            }
        });

        Button btnGetRercord = (Button) findViewById(R.id.btnGetRercord);
        btnGetRercord.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                recordInfos = _zhiduAgent.queryRecordList();
                if (null == recordInfos){
                    Log.e("G","get record is null");
                }
                else{
                    Log.e("G","record size is "+recordInfos.length);
            }
            }
        });

        Button btnPlayRecord = (Button) findViewById(R.id.btnPlayRecord);
        btnPlayRecord.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                for (int i=0; i< recordInfos.length; i++){
                    Log.e("Print RecordInofs", recordInfos[i].toString());
                }
               if(null != recordInfos && recordInfos.length>0){
                   gPlayRcHandle = _zhiduAgent.startPlayback(_view, recordInfos[0],1,false);
               }
            }
        });

        Button btnStopRecord = (Button) findViewById(R.id.btnStopRecord);
        btnStopRecord.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                 if(gPlayRcHandle != 0) {
                     _zhiduAgent.stopPlayback(gPlayRcHandle);
                     gPlayRcHandle = 0;
                 }
            }
        });


        _zhiduEye = new ZhiduEye(this);
        _view = _zhiduEye.createVideoView(new Point(1920, 1080));
        _layout = (LinearLayout) findViewById(R.id.activity_main);
        _layout.addView(_view);

        _zhiduAgent = _zhiduEye.createAgent();
        _zhiduAgent.startLogin("114.215.169.7", (short)5555, "zhidu004@zhidu.biz", "12345");

    }

    public static long getDate(String unixDate) {
        long unixTimestamp = 0;
        try {
            //String time = "2011/07/29 14:50:11";
            Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(unixDate);
            unixTimestamp = date.getTime()/1000;
            System.out.println(unixTimestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unixTimestamp;
    }

}
