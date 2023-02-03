package com.jxj.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.jxj.netty.client.DefaultFuture;
import com.jxj.netty.util.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if ("ping".equals(msg.toString())) {
            ctx.channel().writeAndFlush("ping\r\n");
            return;
        }
//        System.out.println("channelRead");
//        System.out.println(msg.toString());

//        ctx.channel().attr(AttributeKey.valueOf("sssss")).set(msg);//此处是eventloop线程
        Response response = JSONObject.parseObject(msg.toString(), Response.class);
        DefaultFuture.receive(response);
//        ctx.channel().close();

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {


    }
}
