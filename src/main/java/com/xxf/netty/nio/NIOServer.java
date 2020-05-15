package com.xxf.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 服务端
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        //创建ServerSocketChannel 等价于 serverSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //获得Selector对象
        Selector selector = Selector.open();
        //绑定端口,在服务端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //把serverSocketChannel注册到Selector并关心OP_ACCEPT事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //循环等待客户端连接
        while (true) {
            if (selector.select(1000) == 0) { //没有事件发生
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            /**
             * 如果返回 >0, 就获取相关的SelectionKey 集合
             * 1、> 0 表示已经获取到关注事件了
             * 2、selector.selectedKeys() 返回关注事件集合
             * 3、通过selectionKeys获取通道
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //使用迭代器遍历Set<SelectionKey>
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                //获取selectionkey
                    SelectionKey selectionKey = iterator.next();
                //根绝key获取通道响应事件作相应处理
                if (selectionKey.isAcceptable()) { //如果是OP_ACCEPT，有新的客户端连接我
                    //给该客户端生一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel： " + socketChannel.hashCode());
                    //将socketChannel 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将当前的socketChannel也注册到selector, 关注事件为OP_READ,同时给socketChannel  关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //如果可读，发生OP_READ事件
                if (selectionKey.isReadable()) {
                    //通过key反向获取到对应的channel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //获取到该channel的buffer
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    //把当前channel的数据read到buffer
                    channel.read(byteBuffer);
                    System.out.println("从客户端发送的数据" + new String(byteBuffer.array()));
                }

                //手动从集合中移除当前的selectionKey, 防止重复操作
                iterator.remove();
            }
        }

    }
}
