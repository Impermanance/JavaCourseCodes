package com.java.jvm.gateway;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackendServer {
    public static void main(String[] args)throws IOException{
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()+2
        );
        ServerSocket serverSocket = new ServerSocket(8088);
        System.out.println("开启BackendServer服务器，监听地址和端口为 http://127.0.0.1:8088/");
        while(true){
            try {
                final Socket socket = serverSocket.accept();
                executorService.execute(() -> service(socket));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void service(Socket socket) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello,netty";
            printWriter.println("Content-Length:"+body.getBytes().length);
            printWriter.println("Connection:Keep-Alive");
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
            socket.close();
        }catch (IOException e){

        }
    }
}
