package com.hznu.lin.project.entity;

/**
 * @author LIN
 * @date 2020/12/13 22:41
 */
public class Weather {
    private String date;
    private String high;
    private String low;
    private String type;
    private String fengxiang;
    private String fengli;

    public Weather() {
    }

    public Weather(String data, String high, String low, String type, String fengxiang, String fengli) {
        this.date = data;
        this.high = high;
        this.low = low;
        this.type = type;
        this.fengxiang = fengxiang;
        this.fengli = fengli;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    @Override
    public String toString() {
        return "时间：" + date +
                "\n天气：" + type +
                "\n" + low + ", " + high +
                "\n风向：" + fengxiang +
                ", 风力：" + fengli.charAt(9) + "级" +
                "\n"
                ;
    }
}
