package com.xxf.netty.nio;

import java.nio.ByteBuffer;

public class NIOReadOnlyBuffer {
    public static void main(String[] args) {
        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(32);

        for(int i = 0; i< 32; i++) {
            buffer.put((byte)i);
        }

        //
        buffer.flip();

        //得到一个只读buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        //读取
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        //抛异常
        readOnlyBuffer.put((byte)100);

    }
}
