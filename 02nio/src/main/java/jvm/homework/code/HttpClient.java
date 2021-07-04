package jvm.homework.code;

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
 * @date 2021/07/04
 */
public class HttpClient {
    public static void main(String[] args) {
        /**
         *   HttpClient使用介绍
         *   使用HttpClient发送请求主要分为以下几步骤：
         *   创建 CloseableHttpClient对象或CloseableHttpAsyncClient对象，前者同步，后者为异步
         *   创建Http请求对象
         *   调用execute方法执行请求，如果是异步请求在执行之前需调用start方法
        * */
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String baseUrl = "http://localhost:8801";
        String url = String.format("%s", baseUrl);
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println("begin to print response.entity");
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println("begin to print json response");
            System.out.println(JSON.toJSONString(response));
            System.out.println("begin to print json response.entity");
            System.out.println(JSON.toJSONString(response.getEntity()));
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            System.out.println("release client");
            httpClient = null;
        }
        System.out.println("done!");
    }
}
