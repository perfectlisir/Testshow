package com.example.administrator.testshow.Task;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.administrator.testshow.infer.GetVideodataListener;
import com.example.administrator.testshow.bean.VideoInfo;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */

public class VideoDataAnsyTask extends AsyncTask<Void,Integer,List<String>> {


    private List<VideoInfo> videoInfos=new ArrayList<VideoInfo>();
    private List<String> videopath=new ArrayList<String>();
    private GetVideodataListener listener;

    public VideoDataAnsyTask(GetVideodataListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        videoInfos=getVideoFile(videoInfos, Environment.getExternalStorageDirectory());
        videoInfos=filterVideo(videoInfos);
//        Log.i("tga","最后的大小"+videoInfos.size());
        for (int i = 0; i <videoInfos.size() ; i++) {
            videopath.add(videoInfos.get(i).getPath());
//            Log.i("tga","每一次遍历的名字"+videoInfos.get(i).getPath());
        }
        return videopath;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<String> videopath) {
        super.onPostExecute(videopath);
        listener.getdata(videopath);

    }

    /**
     * 获取视频文件
     * @param list
     * @param file
     * @return
     */
    private List<VideoInfo> getVideoFile(final List<VideoInfo> list, File file) {

        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                String name = file.getName();
                int i = name.indexOf('.');//str1.IndexOf("字")查找“字”在str1中的索引值（位置）
                if (i != -1) {
                    name = name.substring(i); // 从指定索引处的字符开始，直到此字符串末尾。
                    if (name.equalsIgnoreCase(".mp4")
                            || name.equalsIgnoreCase(".3gp")
                            || name.equalsIgnoreCase(".wmv")
                            || name.equalsIgnoreCase(".rmvb")
                            || name.equalsIgnoreCase(".avi")
                            || name.equalsIgnoreCase(".mov")
                            || name.equalsIgnoreCase(".m4v")
                            || name.equalsIgnoreCase(".avi")
                            || name.equalsIgnoreCase(".m3u8")
                            || name.equalsIgnoreCase(".3gpp")
                            || name.equalsIgnoreCase(".3gpp2")
                            || name.equalsIgnoreCase(".mkv")
                            || name.equalsIgnoreCase(".flv")
                            || name.equalsIgnoreCase(".divx")
                            || name.equalsIgnoreCase(".f4v")
                            || name.equalsIgnoreCase(".rm")
                            || name.equalsIgnoreCase(".asf")
                            || name.equalsIgnoreCase(".ram")
                            || name.equalsIgnoreCase(".v8")
                            || name.equalsIgnoreCase(".swf")
                            || name.equalsIgnoreCase(".m2v")
                            || name.equalsIgnoreCase(".asx")
                            ) {
                        VideoInfo video = new VideoInfo();
                        file.getUsableSpace();
                        video.setDisplayName(file.getName());
                        video.setPath(file.getAbsolutePath());
                        Log.i("tga","name"+video.getPath());
                        list.add(video);
                        return true;
                    }
                    //判断是不是目录
                } else if (file.isDirectory()) {
                    getVideoFile(list, file);
                }
                return false;
            }
        });

        return list;
    }

    /**10M=10485760 b,小于10m的过滤掉
     * 过滤视频文件
     * @param videoInfos
     * @return
     */
    private List<VideoInfo> filterVideo(List<VideoInfo> videoInfos){
        List<VideoInfo> newVideos=new ArrayList<VideoInfo>();
        for(VideoInfo videoInfo:videoInfos){
            File f=new File(videoInfo.getPath());
            if(f.exists()&&f.isFile()&&f.length()>15485760){
                newVideos.add(videoInfo);
//                Log.i("TGA","文件大小"+f.length());
            }else {
//                Log.i("TGA","文件太小或者不存在");
            }
        }
        return newVideos;
    }
}