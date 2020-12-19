package com.hznu.lin.project.entity;

/**
 * @author LIN
 * @date 2020/12/13 20:15
 */
public class LoginBean {
    private int pictureId;
    private int ret;
    private String msg;
    private String data;

    public LoginBean() {
    }

    public LoginBean(int pictureId, int ret, String msg, String data) {
        this.pictureId = pictureId;
        this.ret = ret;
        this.msg = msg;
        this.data = data;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "pictureId=" + pictureId +
                ", ret=" + ret +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
