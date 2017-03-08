package com.example.administrator.testshow.bean;

/**
 * Created by Administrator on 2016/9/28.
 */
public class Banben {

    /**
     * succeed : 1
     * errCode :
     * msg :
     */

    private StatusBean status;
    /**
     * version : 2
     * name : exhibition1.0.1
     * url : http://download.elmsc.com/android/apk/elmsc.apk
     * desc : 2</version>exhibition1.0.1</name>http://download.elmsc.com/android/apk/elmsc.apk</url>2.3.4版本更新\n解决部分BUG\n</desc></update>
     */

    private DataBean data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class StatusBean {
        private int succeed;
        private String errCode;
        private String msg;

        public int getSucceed() {
            return succeed;
        }

        public void setSucceed(int succeed) {
            this.succeed = succeed;
        }

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class DataBean {
        private int version;
        private String name;
        private String url;
        private String desc;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
