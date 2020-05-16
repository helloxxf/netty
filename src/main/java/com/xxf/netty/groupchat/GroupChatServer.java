package com.xxf.netty.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 聊天服务端
 */
public class GroupChatServer {
    //定义相关属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 9999;

    //构造器初始化
    public GroupChatServer() {
        try {
            //获取选择器
            selector = Selector.open();
            //获取通道
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将listenChannel 注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 监听方法
     */
    public void listen() {
        try {
            //循环处理
            while (true) {
                int count = selector.select(2000);
                if (count > 0) { //有事件要处理
                    //遍历得到selectionKey集合
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey selectionKey = keyIterator.next();
                        //监听到的事件
                        if (selectionKey.isAcceptable()) {
                            SocketChannel accept = listenChannel.accept();
                            accept.configureBlocking(false);
                            //将accept注册到seletor
                            accept.register(selector, SelectionKey.OP_READ);
                            //提示
                            System.out.println(accept.getRemoteAddress() + "上线");
                        }
                        if (selectionKey.isReadable()) { //通道可读
                            //处理读的方法
                            readData(selectionKey);
                        }
                        //当前的key删除，防止重复操作
                        keyIterator.remove();
                    }
                } else {
                    System.out.println("等待事件。。。");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 读取客户端消息
     */
    private void readData(SelectionKey selectionKey) {
        //定义一个socketChannel
        SocketChannel socketChannel = null;
        try {
            //取到关联的channel
            socketChannel = (SocketChannel) selectionKey.channel();
            //创建一个buffer
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            int read = socketChannel.read(allocate);
            //根据read值判断
            if (read > 0) {
                String msg = new String(allocate.array());
                //输出消息
                System.out.println("来自客户端的消息： " + msg);
                //向其他客户端转发消息
                sendMsgToOther(msg, socketChannel);
            }
        } catch (Exception e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了");
                //关闭注册
                selectionKey.cancel();
                //关闭通道
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 转发消息
     */
    public void sendMsgToOther(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中。。。");
        //遍历注册到selector上的SocketChannel,并排除self
        for (SelectionKey selectionKey : selector.keys()) {
            //取出通道
            SelectableChannel targetChannel = selectionKey.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();

    }
}
