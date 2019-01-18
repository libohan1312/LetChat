package com.ltc.letchat.util;

import com.ltc.letchat.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKUtil {
    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    private static OkHttpClient okHttpClient;
    static {
        okHttpClient = new OkHttpClient
                .Builder()
                .callTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                })
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
    }

    public static Single<JSONObject> doRequest(OKRequestEntity okRequestEntity){
        return Single.just(okRequestEntity)
                .observeOn(Schedulers.io())
                .map(okRequestEntity1 -> {
                    try {
                        if (okRequestEntity1.method.equals(METHOD_GET)) {
                            Request request = new Request.Builder()
                                    .url(okRequestEntity1.url)
//                                    .header("Upgrade-Insecure-Requests","1")
                                    .headers(Headers.of(okRequestEntity1.headers))
                                    .build();
                            Response response = okHttpClient.newCall(request).execute();
                            String body = response.body().string();
                            return new JSONObject(body);
                        }else if(okRequestEntity1.method.equals(METHOD_POST)){
                            FormBody.Builder builder = new FormBody.Builder();
                            for (Map.Entry<String, Object> entry : okRequestEntity1.params.entrySet()) {
                                if (entry.getValue() == null || entry.getKey() == null) {
                                    continue;
                                }
                                builder.add(entry.getKey(),entry.getValue().toString());
                            }
                            Request request = new Request.Builder()
                                    .url(okRequestEntity1.url)
                                    .method(METHOD_POST, builder.build())
                                    .headers(Headers.of(okRequestEntity1.headers))
                                    .build();
                            Response response = okHttpClient.newCall(request).execute();
                            String body = response.body().string();
                            return new JSONObject(body);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return new JSONObject();
                })
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Created by Anonymous on 2017/6/13.
     */

    static class SSLSocketClient {

        //获取这个SSLSocketFactory
        public static SSLSocketFactory getSSLSocketFactory() {
            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, getTrustManager(), new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //获取TrustManager
        private static TrustManager[] getTrustManager() {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };
            return trustAllCerts;
        }

        //获取HostnameVerifier
        public static HostnameVerifier getHostnameVerifier() {
            HostnameVerifier hostnameVerifier = (s, sslSession) -> true;
            return hostnameVerifier;
        }
    }


    public static class OKRequestEntity{
        public String url = Constant.http2Uri_test;
        public String method = METHOD_GET;
        public Map<String,String> headers = new HashMap<>();
        public Map<String,Object> params = new HashMap<>();
    }
}
