package com.jxj.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.jxj.netty.util.Response;
import com.jxj.netty.handler.param.ServerRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * @Description
 */
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
    private AtomicInteger i =new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        i.incrementAndGet();
        System.out.println(msg.toString()+"," +i.get());
//            ctx.channel().writeAndFlush("is ok \r\n");
        ServerRequest request = JSONObject.parseObject(msg.toString(), ServerRequest.class);

        Response resp = new Response();
        resp.setId(request.getId());
        resp.setResult("is ok");
        ctx.channel().writeAndFlush(JSONObject.toJSONString(resp));
        ctx.channel().writeAndFlush("\r\n");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        System.out.println("======================");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    System.out.println("读空闲--");
                    ctx.channel().close();
                    break;
                case WRITER_IDLE:
                    System.out.println("写空闲---");
                    break;
                case ALL_IDLE:
                    System.out.println("都空闲---");
                    ctx.channel().writeAndFlush("ping\r\n");
                    break;

            }
        }
    }
}
