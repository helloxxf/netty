package com.xxf.netty.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 聊天客户端
 */
public class GroupChatClient {
    //定义相关属性
    private final String HOST = "127.0.0.1";
    private static final int PORT = 9999;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    //构造器初始化
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        //设置非阻塞模式
        socketChannel.configureBlocking(false);
        //将通道注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到userName
        userName = socketChannel.getRemoteAddress().toString().substring(1);
        System.out.println(userName + "is ok。。。");
    }

    /**
     * 向服务器发送消息
     */
    public void sendMsg(String info) {
        info = userName + "说： " + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收服务端回复的消息
     */
    public void acceptInfo() {
        try {
            int read = selector.select(100); //等待100ms
            if (read > 0) { //有可用的通道
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                //迭代
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    //判断是否可读
                    if (selectionKey.isReadable()) {
                        //得到相关通道
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        //得到buffer
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        //读取
                        channel.read(byteBuffer);
                        //把读取到的缓冲区的数据转换成字符串
                        String s = new String(byteBuffer.array());
                        System.out.println("收到服务端的消息： " + s);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //启动客户端
        GroupChatClient groupChatClient = new GroupChatClient();
        //创建线程,每隔三秒读取服务端的消息
        new Thread(() -> {
            while (true) {
                groupChatClient.acceptInfo();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //发送数据给服务端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            groupChatClient.sendMsg(s);
        }
    }
}
