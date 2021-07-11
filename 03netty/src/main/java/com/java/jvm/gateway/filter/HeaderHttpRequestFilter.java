package com.java.jvm.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

public class HeaderHttpRequestFilter implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().set("nio", "hello");
        String uri = fullRequest.uri();
        System.out.println("HeaderHttpRequestFilter接收到的请求,url: " + uri);
        if (uri.startsWith("/test")) {
            //
        } else {
            System.out.println("url is not supported!");
        }
        HttpHeaders headers = fullRequest.headers();
        if (headers == null) {
            headers = new DefaultHttpHeaders();
        }
        headers.add("filtered", "true");
    }
}
