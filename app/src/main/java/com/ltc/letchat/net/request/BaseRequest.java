package com.ltc.letchat.net.request;/**
 * Created by Administrator on 2016/7/31.
 */

import com.ltc.letchat.Constant;

/**
 *
 */
public class BaseRequest {
    private String type;
    private String userId;

    public BaseRequest(){
        userId = Constant.userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static final String TYPE_REGIST = "Regist";
    public static final String TYPE_GETCONTACTS = "GetContacts";
    public static final String TYPE_TALK = "Talk";
}
