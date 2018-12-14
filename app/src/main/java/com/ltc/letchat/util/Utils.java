package com.ltc.letchat.util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonToken;
import com.ltc.letchat.contacts.data.Contact;
import com.ltc.letchat.net.response.BaseResponse;
import com.ltc.letchat.net.response.GetContactsResp;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class Utils {

    public static String objectToJson(@NonNull Object object) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(object);
        return json;
    }

    public static BaseResponse getBaseResponsByJson(@NonNull String msg) {
        if(!isGoodJson(msg)){
            return null;
        }
        BaseResponse baseResponse = jsonToObject(msg,BaseResponse.class);
        return baseResponse;
    }

    public static String getStringValueFromJson(String msg,String key) throws IOException{
        com.google.gson.stream.JsonReader jsonReader = new com.google.gson.stream.JsonReader(new StringReader(msg));
        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            String name = jsonReader.nextName();
            if(key.equals(name)){
                if(jsonReader.peek() == JsonToken.NULL){
                    return null;
                }
                return jsonReader.nextString();
            }else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        jsonReader.close();
        return null;
    }

    public static List<Contact> getContacts(String msg){
        GetContactsResp resp = jsonToObject(msg,GetContactsResp.class);
        if(resp != null){
            return resp.getContactList();
        }
        return null;
    }

    public static <T> T jsonToObject(String json,Class<T> clazz){
        Gson gson = new GsonBuilder().serializeNulls().create();
        T object = gson.fromJson(json,clazz);
        return object;
    }

    public static boolean isGoodJson(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }


    public static class Test extends BaseResponse{
        public Test(){
            setType("test");
        }
        public String test;
    }
    public static void main(String[] args){
        Test test = new Test();
        test.test = "abc";
        String tt = new Gson().toJson(test);
        test = jsonToObject(tt,Test.class);
        System.out.println(test.test);
    }

}
