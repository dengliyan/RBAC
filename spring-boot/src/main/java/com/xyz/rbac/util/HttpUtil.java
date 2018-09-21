package com.xyz.rbac.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {

    public static String get(String url,String encoding) {
        String result = "";
        BufferedReader in = null;
        URLConnection connection =null;
        try {
            URL uri = new URL(url);
            connection = uri.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36");
            connection.connect();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),encoding));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        }
        catch (Exception ex){

        }finally {

        }
        return result;
    }


    public static void main(String[] args) {
        System.out.println(HttpUtil.get("http://127.0.0.1:8080/static/fontawesome/css/fontawesome-all.min.css", "utf-8"));
    }
}
