package com.xxf.netty.nio;

import io.netty.channel.ServerChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * NIO支持多个buffer读写
 * Scattering , 将数据写入buffer，可以使用buffer数组依次写入(分散）
 * Gethering: 从buffer读取数据时候，可以使用buffer数字一次读取(聚合）
 */
public class ScatteringAndGethering {

    public static void main(String[] args) throws IOException {

        //使用
        ServerSocketChannel open = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        System.out.println("启动服务 port： " + 7000);

        //绑定端口到socket，并启动
        open.socket().bind(inetSocketAddress);

        //创建buffe数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];

        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接
        SocketChannel accept = open.accept();

        int messageLen = 10; //假定从客户端接受8个字节

        //循环的读取
        while (true) {
            int byteRead = 0;

            while (byteRead < messageLen) {
                long read = accept.read(byteBuffers);
                //累计读取字节数
                byteRead += 1;
                System.out.println("byteRead" + byteRead);

                Arrays.asList(byteBuffers).stream().map(buffer -> "position" + buffer.position() + ", limit" + buffer.limit()).forEach(System.out::println);
            }

            //将所有buffer进行flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            //将数据读出显示在客户端
            long byteWrite = 0;
            while (byteWrite < messageLen) {
                accept.write(byteBuffers);
                byteWrite += 1;
            }

            //将所有的buffer进行clear
            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.clear();
            });

            System.out.println("byteRead:=" + byteRead + "byteWrite=" + byteWrite + "meaageLen=" + messageLen);
        }

    }
}
