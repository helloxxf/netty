package com.xxf.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {

        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = channel.pipeline();
        /**加入一个Netty提供的HttpSererCodec
         * 1、HttpSererCodec 是netty提供处理http的编解码器
         *
         */
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //增加一个自定义的处理器handler
        pipeline.addLast("MyTestServerHandler", new TestServerHandler());
    }
}
