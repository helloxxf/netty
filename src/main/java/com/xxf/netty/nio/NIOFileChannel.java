package com.xxf.netty.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel {
    public static void main(String[] args) throws FileNotFoundException {
        String str = "Hello NIO";
        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("F://test.txt");
        //通过fileOutputStream得到channel
        FileChannel channel = fileOutputStream.getChannel();
        //创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());
        //byteBuffer反转
        byteBuffer.flip();
        //byteBuffer写入channel
        try {
            channel.write(byteBuffer);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
