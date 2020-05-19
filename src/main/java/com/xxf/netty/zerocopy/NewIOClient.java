package com.xxf.netty.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("localhost", 7001));

        String fileName = "Netty.rar";
        //得到channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        //准备发送
        long startTime = System.currentTimeMillis();

        //在Linux下transferTo 方法就可以完成传输
        //在windows下一次调用transferTo 只能发送8M大小数据，就需要分段传输文件
        //transferTo就是用了0copy技术
        long count = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送的总的字节数=" + count + " 耗时：" + (System.currentTimeMillis() - startTime));

        fileChannel.close();
    }
}
