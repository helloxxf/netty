package com.xxf.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 说明
 * 1、SimpleChannelInboundHandler 是ChannelInboundHandlerAdapter的子类
 * 2、HttpObject 客户端和服务器端相互通讯的数据被封装成HttpObject
 */
public class TestServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端数据
     *
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject msg) throws Exception {
        //判断msg是不是HttpRequest请求
        if(msg instanceof HttpRequest) {

            System.out.println("pipeline hashCode:" + channelHandlerContext.hashCode() + ", TestServerHandler hashCode:" + this.hashCode());
            System.out.println("msg 类型：" + msg.getClass());
            System.out.println("客户端地址：" + channelHandlerContext.channel().remoteAddress());

            //获取uri过滤指定资源
            HttpRequest httpRequest = (HttpRequest)msg;
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了favicon.ico资源，不作响应");
                return;
            }
            //回复信息给浏览器（http协议）
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello 我是服务器", CharsetUtil.UTF_8);
            //构造一个http响应，即HttpResponse
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            //将构建好的response返回
            channelHandlerContext.writeAndFlush(httpResponse);
        }
    }
}
