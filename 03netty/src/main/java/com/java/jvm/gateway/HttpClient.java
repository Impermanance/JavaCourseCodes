package com.java.jvm.gateway;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * HttpClient class
 *
 * @author likun
 * @date 2021/07/11
 */
public class HttpClient {
    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String baseUrl = "http://localhost:8888";
        String url = String.format("%s", baseUrl);
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println("begin to print response.entity");
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println("begin to print json response");
            System.out.println(JSON.toJSONString(response));
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            System.out.println("release client");
            httpClient = null;
        }
        System.out.println("done!");
    }
}
