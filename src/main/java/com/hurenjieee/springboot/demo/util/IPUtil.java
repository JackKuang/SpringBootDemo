package com.hurenjieee.springboot.demo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Jack
 * @date 2019/5/5 11:14
 */
public class IPUtil {

    public static void main(String[] args) {
        System.out.println(getIP());
    }

    private static final String REQUEST_URL = "http://www.baidu.com/s?wd=IP";

    private static final Pattern patternForIp = Pattern.compile("fk=\"(.*?)\"");

    public static String getIP() {
        String ip = "";

        StringBuilder inputLine = new StringBuilder();
        String read;
        URL url;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(REQUEST_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n");
            }
            Matcher m = patternForIp.matcher(inputLine.toString());
            if (m.find()) {
                String ipstr = m.group(1);
                ip = ipstr;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return ip;
        }
    }
}