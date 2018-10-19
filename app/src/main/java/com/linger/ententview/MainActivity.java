package com.linger.ententview;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.value;
import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn;//登录按钮
    private EditText etName, etPassword;
    private LinearLayout layout;
    private MyVideoView videoView;//背景播放的视频
    private Timer timer;//计时器
    private static final String VIDEO_NAME = "PeppaPig.mp4";
    private int i=3;//用来倒计时
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*使播放的视频充满屏幕，包括上方的状态栏也进行充满*/
        if(Build.VERSION.SDK_INT>=Build.VERSION.SDK_INT){
            Window window=getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//隐藏系统的标题栏
        init();//控件的初始化
        getVideo();//获取视频
    }

    private void init() {
        btn = (Button) findViewById(R.id.btn_enter);
        etName = (EditText) findViewById(R.id.et_name);
        etPassword = (EditText) findViewById(R.id.et_password);
        layout= (LinearLayout) findViewById(R.id.layout);
        videoView = (MyVideoView) findViewById(R.id.video_view);
        btn.setOnClickListener(this);
        timer=new Timer();//初始化计时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                message.obj=i;
                handler.sendMessage(message);//发送消息给Handler
            }
        },0,1000);//0:无延迟，1000，每隔1秒执行一次
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what==1){
                if (i==0){
                   layout.setVisibility(View.VISIBLE); //显示布局
                    timer.cancel();
                }
                i--;//自减
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        switch(v.getId()){//点击登录按钮的操作
            case R.id.btn_enter:
                 Toast.makeText(MainActivity.this,
                         "用户名:"+etName.getText().toString()+"\r\n"+"密码："+etPassword.getText().toString(),Toast.LENGTH_SHORT).show();
                break;
             default:
                 break;

        }
    }

    /*显示视频*/
    public void getVideo() {
        File videoFile = getFileStreamPath(VIDEO_NAME);//获取视频的路径
        if (!videoFile.exists()) {//判断文件是否存在
            videoFile=readVideoFile();//读取视频文件
        }
        playVideo(videoFile);//播放视频
    }

    private void playVideo(File videoFile) {
        videoView.setVideoPath(videoFile.getPath());
        videoView.setLayoutParams(new RelativeLayout.LayoutParams(-1,-1));//给视频设置布局
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);//设置视频循环
                mp.start();//开始播放
            }
        });
    }

    /*获取系统文件*/
    private File readVideoFile(){
        File videoFile=null;
        try {
            FileOutputStream fos=openFileOutput(VIDEO_NAME,MODE_PRIVATE);//输出字节流
            InputStream in=getResources().openRawResource(R.raw.peppapig);//输入文件字节流
            byte[] buff=new byte[1024];//转换byte字节
            int len=0;
            while ((len=in.read(buff))!=-1){
                fos.write(buff,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoFile=getFileStreamPath(VIDEO_NAME);//获取文件的路径
        if(!videoFile.exists()){
            throw new RuntimeException("视频文件有问题，你确定有peppapig文件吗？");
        }
        return videoFile;//返回视频文件
    }
}
