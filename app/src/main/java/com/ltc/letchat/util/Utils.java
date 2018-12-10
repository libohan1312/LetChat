package com.ltc.letchat.util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonToken;
import com.ltc.letchat.contacts.data.Contact;
import com.ltc.letchat.net.response.GetContactsResp;

import org.json.JSONException;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class Utils {

    public static final String TYPE = "type";

    public static String objectToJson(@NonNull Object object){
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(object);
        return json;
    }



    public static String getProtocolType(@NonNull String msg) throws IOException, JSONException {
        if(!isGoodJson(msg)){
            return null;
        }

        return getStringValueFromJson(msg,"type");
    }

    public static String getStringValueFromJson(String msg,String key) throws IOException{
        System.out.println("json paras is work to :"+msg+" , key is "+key);
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

}
