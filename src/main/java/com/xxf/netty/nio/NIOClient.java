package com.xxf.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 客户端
 */
public class NIOClient {

    public static void main(String[] args) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        //设置非阻塞模式
        socketChannel.configureBlocking(false);

        //提供服务端的ip + port
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("以为连接需要时间，客户端不会阻塞，可以做其他工作。。。");
            }
        }

        System.out.println("客户端连接成功");
        //连接成功就发送数据
        String str = "Hello Server, I'm Client";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        //发送数据，将buffer数据写入channel
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
