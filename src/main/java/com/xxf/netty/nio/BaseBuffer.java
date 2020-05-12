package com.xxf.netty.nio;

import java.nio.IntBuffer;

//buffer
public class BaseBuffer {

    public static void main(String[] args) {
        //举例说明buffer使用
        IntBuffer intbuffer = IntBuffer.allocate(5); //创建一个buffer，存储5个int
        //向buffer中存放数据
        for (int i = 0; i < intbuffer.capacity(); i++) {
            intbuffer.put(i);
        }
        //取出buffer
        intbuffer.flip(); //将buffer切换，读写切换

        while (intbuffer.hasRemaining()) {
            System.out.println(intbuffer.get());
        }
    }
}
