package com.jxj.client.handler;

import com.alibaba.fastjson.JSONObject;
import com.jxj.client.core.DefaultFuture;
import com.jxj.client.param.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Description
 */
public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

    private static final Executor exec = Executors.newFixedThreadPool(10);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if ("ping".equals(msg.toString())) {
            ctx.channel().writeAndFlush("ping\r\n");
            return;
        }
//        System.out.println("channelRead");
//        System.out.println(msg.toString());

//        ctx.channel().attr(AttributeKey.valueOf("sssss")).set(msg);//此处是eventloop线程

//        Response response = JSONObject.parseObject(msg.toString(), Response.class);
//        DefaultFuture.receive(response);

        exec.execute(new Runnable() {

            public void run() {
                Response response = JSONObject.parseObject(msg.toString(), Response.class);
//                JSONObject.toJSONString(response);
                System.out.println("SimpleClientHandler中的Response:"+JSONObject.toJSONString(response));
                DefaultFuture.receive(response);
            }
        });




    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {


    }
}
