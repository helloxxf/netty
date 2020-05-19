package com.xxf.netty.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOServer {

    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket socket = serverSocketChannel.socket();
        socket.bind(inetSocketAddress);
        ByteBuffer byteBuffer = ByteBuffer.allocate(5096);

        while (true) {
            SocketChannel accept = serverSocketChannel.accept();

            int readCount = 0;
            while (readCount != -1) {
                try {
                    readCount = accept.read(byteBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byteBuffer.rewind(); // 倒带position=0 mark作废
            }
        }
    }
}
