package com.xxf.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOChannelRead {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("F://test.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());

        try {
            channel.read(byteBuffer);
            System.out.println(new String(byteBuffer.array()));
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
