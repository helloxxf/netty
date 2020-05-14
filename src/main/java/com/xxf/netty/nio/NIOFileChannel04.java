package com.xxf.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {

    public static void main(String[] args) throws IOException {
        //创建相关流
        FileInputStream fileInputStream = new FileInputStream("F://a.jpg");

        FileOutputStream fileOutputStream = new FileOutputStream("F://b.jpg");

        //获取流对应的fileChannel
        FileChannel sourceChannel = fileInputStream.getChannel();

        FileChannel desChannel = fileOutputStream.getChannel();
        //使用transferFrom完成快速复制
        desChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

        //关闭相关流
        sourceChannel.close();
        desChannel.close();
        fileInputStream.close();
        fileOutputStream.close();


    }
}
