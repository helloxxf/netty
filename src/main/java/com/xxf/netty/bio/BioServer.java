package com.xxf.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {
    public static void main(String[] args) throws IOException {
        //创建一个线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        //创建serverSocket
        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动");

        while (true) {
            //监听，等待客户端连接
            final Socket accept = serverSocket.accept();
            System.out.println("有个客户端进行连接");
            //启动一个线程与之通信
            executorService.execute(() -> {
                handler(accept);
            });
        }

    }

    //编写一个和客户端通讯的handler
    public static void handler(Socket socket) {
        byte[] bytes = new byte[1024];
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            //循环读取客户端发送的数据
            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    //输出客户端发送的数据
                    System.out.println(new String(bytes, 0, read));
                } else {
                    System.out.println("读取完毕");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    System.out.println("关闭客户端连接");
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
