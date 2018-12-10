package com.ltc.letchat.net.request;

/**
 * Created by Administrator on 2016/7/31.
 */
public class Talk extends BaseRequest {
    private String content;
    private String toWho;

    public Talk(){
        setType(BaseRequest.TYPE_TALK);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToWho() {
        return toWho;
    }

    public void setToWho(String toWho) {
        this.toWho = toWho;
    }
}
