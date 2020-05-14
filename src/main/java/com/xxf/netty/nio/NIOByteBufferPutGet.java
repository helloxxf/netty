package com.xxf.netty.nio;

import java.nio.ByteBuffer;

/**
 * ByteBuffer类型化put和get,put什么类型，get就应该适用相同类型取出
 */
public class NIOByteBufferPutGet {

    public static void main(String[] args) {

        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //类型化放入数据
        buffer.putInt(10);
        buffer.putLong(10L);
        buffer.putDouble(10.0);

        //取出
        buffer.flip();

        System.out.println("=============================");

        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getDouble());


    }
}
