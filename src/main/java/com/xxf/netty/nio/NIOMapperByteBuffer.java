package com.xxf.netty.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1、MapperByteBuffer可以让文件直接在内存（堆外内存）中修改，操作系统不要copy
 */
public class NIOMapperByteBuffer {
    public static void main(String[] args) throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile("F:\\test.txt", "rw");

        //获取通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * FileChannel.MapMode.READ_WRITE 读写模式
         * 0 ：可以修改的起始位置
         * 5 ：映射到内存大小， 即将上面1.txt的多少个字节映射到内存
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte)'1');
        mappedByteBuffer.put(3, (byte)'7');

        mappedByteBuffer.put(5, (byte)'L'); //越界异常
        randomAccessFile.close();
        System.out.println("修改成功");


    }
}
