package com.hurenjieee.springboot.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hurenjieee.springboot.demo.service.IUpdateIpService;
import com.hurenjieee.springboot.demo.util.HttpRequestUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jack
 * @date 2019/5/5 16:33
 */
@Service
public class UpdateIpService implements IUpdateIpService {


    private Marker errorMarker = MarkerManager.getMarker("ERROR");
    private Marker otherMarker = MarkerManager.getMarker("OTHER");

    private Logger logger = LogManager.getLogger(getClass());

    /**
     * HTTP请求类别
     */
    private String HTTP_METHOD = "GET";
    /**
     * 阿里云提供AccessKeyId
     */
    @Value("${aliyun.accessKeyId}")
    private String ACCESS_KEY_ID;
    /**
     * 域名名称
     */
    @Value("${aliyun.dimainName}")
    private String DOMAIN_NAME;
    /**
     * 阿里云提供秘钥
     */
    @Value("${aliyun.key}")
    private String KEY;
    /**
     * 时间格式刷
     */
    private String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    /**
     * 编码类别
     */
    private String ENCODING = "UTF-8";
    /**
     * HTTP的GET请求分割符号
     */
    private String SEPARATOR = "&";

    @Override
    public void updateIp(String ipString) {
        try {
            Map<String, String> parameters = new HashMap<>();
            // 加入公共请求参数
            addPublicParameter(parameters);
            // 获取云解析列表
            describeDomainRecords(parameters);
            String response;
            response = sendRequest(parameters);
            JSONObject jsonObject = JSONObject.parseObject(response);
            JSONObject jsonObject2 = (JSONObject) jsonObject.get("DomainRecords");
            JSONArray records = (JSONArray) jsonObject2.get("Record");
            for (int i = 0; i < records.size(); i++) {
                JSONObject j = (JSONObject) records.get(i);
                String RecordId = j.getString("RecordId");
                String RR = j.getString("RR");
                String Value = j.getString("Value");
                boolean needUpdate = "server".equals(RR) && !ipString.equals(Value);
                if (needUpdate) {
                    parameters.clear();
                    // 加入公共请求参数
                    addPublicParameter(parameters);
                    // 更新云解析
                    updateDomainRecord(parameters, RecordId, RR, ipString);
                    response = sendRequest(parameters);
                }
            }

        } catch (Exception e) {
            logger.error(errorMarker, "【请求异常】", e);
        } finally {

        }

    }


    /**
     * 增加公共参数
     *
     * @param parameters
     */
    private void addPublicParameter(Map<String, String> parameters) {

        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        String time = df.format(new Date());
        String uuid = UUID.randomUUID().toString();

        parameters.put("Format", "JSON");
        parameters.put("Version", "2015-01-09");
        parameters.put("AccessKeyId", ACCESS_KEY_ID);
        parameters.put("SignatureMethod", "HMAC-SHA1");
        parameters.put("Timestamp", time);
        parameters.put("DomainName", DOMAIN_NAME);
        parameters.put("SignatureNonce", uuid);
        parameters.put("SignatureVersion", "1.0");
        parameters.put("Timestamp", time);

    }


    /**
     * 获取到到域名下记录
     *
     * @param parameters
     */
    private void describeDomainRecords(Map<String, String> parameters) {
        parameters.put("Action", "DescribeDomainRecords");
        parameters.put("DomainName", DOMAIN_NAME);
        parameters.put("PageNumber", "1");
        parameters.put("PageSize", "100");
    }


    /**
     * @param parameters
     * @param RecordId   阿里云传回Id
     * @param RR         主机记录
     * @param Value      服务器IP
     */
    private void updateDomainRecord(Map<String, String> parameters, String RecordId, String RR, String Value) {
        parameters.put("Action", "UpdateDomainRecord");
        parameters.put("RecordId", RecordId);
        parameters.put("RR", RR);
        parameters.put("Type", "A");
        parameters.put("Value", Value);
        //线路
        //parameters.put("Line","telecom");
    }


    /**
     * 编码转换
     *
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     */
    private String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, ENCODING).replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;
    }

    /**
     * 构建签名
     *
     * @param parameters
     * @return
     * @throws IllegalStateException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private String getSignature(Map<String, String> parameters)
            throws IllegalStateException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {

        // 对参数进行排序，注意严格区分大小写
        String[] sortedKeys = parameters.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);

        // 生成stringToSign字符串
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(HTTP_METHOD).append(SEPARATOR);
        stringToSign.append(percentEncode("/")).append(SEPARATOR);

        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            // 这里注意对key和value进行编码
            canonicalizedQueryString.append("&").append(percentEncode(key)).append("=").append(percentEncode(parameters.get(key)));
        }

        // 这里注意对canonicalizedQueryString进行编码
        stringToSign.append(percentEncode(canonicalizedQueryString.toString().substring(1)));
        //System.out.println(stringToSign);

        final String ALGORITHM = "HmacSHA1";
        final String ENCODING = "UTF-8";

        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(KEY.getBytes(ENCODING), ALGORITHM));
        byte[] signData = mac.doFinal(stringToSign.toString().getBytes(ENCODING));

        String signature = new String(Base64.encodeBase64(signData));

        //System.out.println(signature);
        return signature;
    }


    /**
     * 发送请求
     *
     * @param parameters
     * @return
     * @throws InvalidKeyException
     * @throws IllegalStateException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private String sendRequest(Map<String, String> parameters)
            throws InvalidKeyException, IllegalStateException, UnsupportedEncodingException, NoSuchAlgorithmException {
        parameters.put("Signature", getSignature(parameters));
        Iterator<Map.Entry<String, String>> iterator = parameters.entrySet().iterator();
        StringBuffer s = new StringBuffer();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            s.append(entry.getKey() + "=" + percentEncode(entry.getValue()) + "&");
        }
        s.deleteCharAt(s.length() - 1);

        logger.info(otherMarker, "Thread:{};Request:{}", Thread.currentThread().getName(), s.toString());
        String response = HttpRequestUtil.sendGet("http://alidns.aliyuncs.com/", s.toString());
        logger.info(otherMarker, "Thread:{};Response:{}", Thread.currentThread().getName(), response);

        return response;
    }
}
