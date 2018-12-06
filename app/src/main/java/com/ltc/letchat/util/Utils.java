package com.ltc.letchat.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.ltc.letchat.contacts.data.Contact;
import com.ltc.letchat.net.response.GetContactsResp;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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



    public static String getProtocalType(@NonNull String msg) throws IOException, JSONException {
        if(!isGoodJson(msg)){
            return null;
        }

//        JSONObject jsonObject = new JSONObject(msg);
//
//        return jsonObject.getString(TYPE);

//        if(!isGoodJson(msg)){
//            return null;
//        }
        JsonReader jsonReader = new JsonReader(new StringReader(msg));
//        jsonReader.setLenient(true);
        jsonReader.beginObject();

        while (jsonReader.hasNext()){
            String name = jsonReader.nextName();
            if("type".equals(name)){
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
