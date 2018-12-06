package com.ltc.letchat.net.request;

/**
 * Created by Administrator on 2016/7/31.
 */
public class Talk extends BaseRequest {
    private String content;
    private String toHow;

    public Talk(){
        setType(BaseRequest.TYPE_TALK);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToHow() {
        return toHow;
    }

    public void setToHow(String toHow) {
        this.toHow = toHow;
    }
}
