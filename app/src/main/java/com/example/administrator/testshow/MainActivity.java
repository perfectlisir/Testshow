package com.example.administrator.testshow;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.administrator.testshow.Task.VideoDataAnsyTask;
import com.example.administrator.testshow.bean.Banben;
import com.example.administrator.testshow.infer.GetVideodataListener;
import com.example.administrator.testshow.util.AutoInstall;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements GetVideodataListener {
    private List<String> videoname=new ArrayList<String>();
    private List<String> videoname1=new ArrayList<String>();
    private VideoView mVideoView;
    private String url="http://www.elmsc.com/index.php?app=androidvideo";
    private String vsionurl="http://www.elmsc.com/mobileapi/index.php?act=exhibition_version";
    private String Apkurl;

    private int i,oldversion,newversion;
    private Intent intent;
    private WebView webview;
    private ImageView imageview;
    private ImageButton show_player,show_web,show_qr;
    private TextView tv_show_player,tv_show_web,tv_show_qr;
    private HttpUtils httpUtils;
    private HttpHandler h;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initProgress();
        inData();


        VideoDataAnsyTask task=new VideoDataAnsyTask(this);
        task.execute();

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
                i++; //
                if(i<videoname.size()) {
                    play(i);
                }else if(i>=videoname.size()){
                    i=0;
                    play(i);
                }

            }
        });

    }

    private void initProgress() {
        progressDialog=new ProgressDialog(MainActivity.this);
//设置进度样式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("有新的安装包更新");
        progressDialog.setMessage("下载中......");
        progressDialog.setMax(0);
        progressDialog.setButton(ProgressDialog.BUTTON1, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (h != null) {
                    h.cancel();//取消下载

                }
            }
        });

    }

    /**
      * 获取版本号
       * @return 当前应用的版本号
       */
     public int getVersion() {

             PackageManager manager = this.getPackageManager();
         PackageInfo info = null;
         try {
             info = manager.getPackageInfo(this.getPackageName(), 0);
         } catch (PackageManager.NameNotFoundException e) {
             e.printStackTrace();
         }
         int version = info.versionCode;
         return version;
     }

    //获取网络数据
    public void inData() {
        OkHttpClient okhttp=new OkHttpClient();
        Request request=new Request.Builder().url(vsionurl).build();
        Call call=okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final Banben banban=new Gson().fromJson(response.body().string(),Banben.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Apkurl=banban.getData().getUrl();
                        Log.i("tga",Apkurl);
                        newversion=banban.getData().getVersion();

                        oldversion=getVersion();
                        if(oldversion<newversion){//判断是否更新
                            Toast.makeText(getApplicationContext(),"更新新版本",Toast.LENGTH_LONG).show();

                            downloadFileData(Apkurl);
                            Log.i("tga",Apkurl+"=========");

                        }
                        VideoDataAnsyTask task=new VideoDataAnsyTask(MainActivity.this);
                        task.execute();
                    }
                });
            }
        });


    }

    //第一次做版本更新设置
        //下载新版本
    public void downloadFileData(String url) {
        //String url, String target,boolean autoResume, boolean autoRename, RequestCallBack<File> callback
        /**
         * 参数1.网络资源地址
         * 参数2.本地存储位置
         * 参数3，是否断点续传
         * 参数4.下载后是否重命名
         * 参数5.异步请求的回调接口
         */
        Log.v("eeeeee","an zhuang zhong"+url);
        h= httpUtils.download(url, Environment.getExternalStorageDirectory()+"/emlsc"+System.currentTimeMillis()+".apk", true, true, new RequestCallBack<File>() {
            //开始
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }
            //取消
            @Override
            public void onCancelled() {
                super.onCancelled();
                progressDialog.dismiss();
            }

            //正在下载中
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                if (progressDialog.getMax() == 0){
                    progressDialog.setMax((int) total);
                }
                //设置下载进度
                progressDialog.setProgress((int)(current*100/total));

            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                File file = responseInfo.result;
                Log.v("eeeeee","an zhuang zhong"+(file == null));
                if (file != null) {
                        //找到文件路径，自动安装
                    Log.v("eeeeee","an zhuang zhong");
                    AutoInstall.setUrl(file.getAbsolutePath());
                    AutoInstall.install(MainActivity.this);

                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
             Log.v("eeeeee",msg);
            }
        });
    }

    private void initView() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        webview = (WebView) findViewById(R.id.webview);
        show_web= (ImageButton) findViewById(R.id.show_web);
        show_player= (ImageButton) findViewById(R.id.show_player);
        tv_show_player= (TextView) findViewById(R.id.tv_show_player);
        tv_show_web= (TextView) findViewById(R.id.tv_show_web);

        httpUtils=new HttpUtils();

        webview.setVisibility(View.GONE);
        intent = new Intent();
//        BitmapUtils bitmapUtils=new BitmapUtils(MainActivity.this);
        // 方式一,直接展示图片
//        bitmapUtils.display(imageview,"http://image.elmsc.com/mall/settings/code.png");
//        webview.loadUrl("http://www.elmsc.com/");
//        webview.loadUrl("http://baidu.com");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
//        webview.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // TODO Auto-generated method stub
//                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                view.loadUrl(url);
////                Log.i("tga","==============112343=");
//                return true;
//            }
//
//        });

    }

    public void showWeb(){
        webview.loadUrl("http://www.elmsc.com/");
//        webview.loadUrl("http://baidu.com");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
//                Log.i("tga","==============112343=");
                return true;
            }

        });

    }
      //停止播放
    public void stop() {
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }
    }

    //初始化控件
    public void diji(){
        show_player.setImageResource(R.mipmap.player);
        tv_show_player.setTextColor(getResources().getColor(R.color.text_on));
        show_web.setImageResource(R.mipmap.logo);
        tv_show_web.setTextColor(getResources().getColor(R.color.text_on));


    }
    //异步接口回调
    @Override
    public void getdata(List list) {
        videoname.addAll(list);
        play(i);

    }

    public void play(int i) {
        //设置视频地址
//        Log.i("tga","==============="+videoname.get(i).toString());
        mVideoView.setVideoPath(videoname.get(i).toString());
//      mVideoView.setVideoPath("/storage/emulated/0/3renmin.avi");
//      mVideoView.setVideoPath("http://download.elmsc.com/android/video/5.爱心公益.avi");
        //视频控制器
        MediaController mediaController=new MediaController(MainActivity.this);
        //绑定控制器
        mVideoView.setMediaController(mediaController);

        mVideoView.start();

    }




    //点击事件
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.show_player:
                diji();
                show_player.setImageResource(R.mipmap.player_on);
                tv_show_player.setTextColor(getResources().getColor(R.color.text));
                webview.setVisibility(View.GONE);
                mVideoView.setVisibility(View.VISIBLE);
                i=0;
                play(i);
                break;
            case R.id.show_web:
                stop();
                showWeb();
                diji();

                show_web.setImageResource(R.mipmap.logo_on);
                tv_show_web.setTextColor(getResources().getColor(R.color.text));
                mVideoView.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
                break;

        }

    }
}
