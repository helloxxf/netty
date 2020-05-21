package com.xxf.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 1、创建两个线程组BossGroup和WorkerGroup
         * 2、BossGroup只处理连接请求，WorkerGroup 客户端业务处理
         * 3、两个都是无限循环
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器端的启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //使用链式编程进行设置
            serverBootstrap.group(bossGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //设置通道类型，NioServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到连接数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //todo
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    }); //给我们的WorkerGroup的EventLoop对应的管道设置处理器
            System.out.println("服务器已准备好。。。");
            //绑定端口,并且同步处理
            ChannelFuture sync = serverBootstrap.bind(8888).sync();
            //对关闭通道进行监听
            sync.channel().closeFuture().sync();
        }  finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
