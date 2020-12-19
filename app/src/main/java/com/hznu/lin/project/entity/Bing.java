package com.hznu.lin.project.entity;

/**
 * @author LIN
 * @date 2020/12/14 0:06
 */
public class Bing {
    private String url;
    private String copyright;


    public Bing() {
    }

    public Bing(String url, String copyright) {
        this.url = url;
        this.copyright = copyright;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @Override
    public String toString() {
        return "Bing{" +
                "url='" + url + '\'' +
                ", copyright='" + copyright + '\'' +
                '}';
    }
}
