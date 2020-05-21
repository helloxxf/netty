package com.xxf.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;

/**
 * 1、自定义一个handler需要继承netty规定好的HandlerAdapter.
 * 2、这时我们自定义一个Handler，才能称为一个Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据
     *
     * @param ctx 上下文对象，含有管道pipeline, 通道，地址
     *            2、object msg: 客户端发送的数据
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("sever ctx=" + ctx);
        //ByteBuf
        //byteBuffer是netty提供的，不是NIO的ByteBuffer
        ByteBuf buffer = (ByteBuf) msg;
        System.out.println("客户端发送的消息是：" + buffer.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush = write + flush, 将数据写入到缓冲区并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，客户端", CharsetUtil.UTF_8)); //对发送的数据进行编码

    }

    /**
     * 发生异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
