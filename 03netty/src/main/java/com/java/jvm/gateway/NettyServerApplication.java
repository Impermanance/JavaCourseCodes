package com.java.jvm.gateway;


import com.java.jvm.gateway.inbound.HttpInboundServer;

import java.util.Arrays;

public class  NettyServerApplication {
    
    private final static String GATEWAY_NAME = "NIOGateway";
    private final static String GATEWAY_VERSION = "3.0.0";
    
    public static void main(String[] args) {

        String proxyPort = System.getProperty("proxyPort","8888");

        String proxyServers = System.getProperty("proxyServers","http://localhost:8088");
        int port = Integer.parseInt(proxyPort);
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" starting...");
        HttpInboundServer server = new HttpInboundServer(port, Arrays.asList(proxyServers.split(",")));
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" started at http://localhost:" + port + " for server:" + server.toString());
        try {
            server.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}