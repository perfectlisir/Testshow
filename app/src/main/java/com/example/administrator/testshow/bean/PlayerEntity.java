package com.example.administrator.testshow.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class PlayerEntity {

    /**
     * play_name : 1.发现之旅-e联盟商城.mp4
     * play_url : http://download.elmsc.com/android/video/1.发现之旅-e联盟商城.mp4
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String play_name;
        private String play_url;

        public String getPlay_name() {
            return play_name;
        }

        public void setPlay_name(String play_name) {
            this.play_name = play_name;
        }

        public String getPlay_url() {
            return play_url;
        }

        public void setPlay_url(String play_url) {
            this.play_url = play_url;
        }
    }
}
