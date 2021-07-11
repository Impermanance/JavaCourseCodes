package com.java.jvm.gateway.filter;

import io.netty.handler.codec.http.FullHttpResponse;

public class HeaderHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        System.out.println("HeaderHttpResponseFilter start!");
        response.headers().set("likun", "fight");
    }
}
