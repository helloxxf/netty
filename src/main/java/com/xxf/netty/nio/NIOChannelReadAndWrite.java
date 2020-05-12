package com.xxf.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOChannelReadAndWrite {
    public static void main(String[] args) throws Exception {
        File file = new File("F://test.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());

        FileOutputStream fileOutputStream = new FileOutputStream("F://test2.txt");
        FileChannel channel1 = fileOutputStream.getChannel();



        while (true) {

            //清空buffer
            byteBuffer.clear();

            int read = channel.read(byteBuffer);
            if(read == -1) {
                break;
            }
            byteBuffer.flip();
            channel1.write(byteBuffer);
        }


        fileInputStream.close();
        fileOutputStream.close();
    }
}
