package com.jxj.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.jxj.netty.util.Response;
import com.jxj.netty.handler.param.ServerRequest;
import com.jxj.netty.medium.Media;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @Description
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
//    private AtomicInteger i =new AtomicInteger(0);
    private static final Executor exec = Executors.newFixedThreadPool(10);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("服务器Handler:"+msg.toString());
//        ServerRequest request = JSONObject.parseObject(msg.toString(), ServerRequest.class);
//        Media media = Media.newInstance();
//        Response response = media.process(request);
//        ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
//        ctx.channel().writeAndFlush("\r\n");
////        Response response = new Response();
////        response.setId(request.getId());
////        response.setResult("is ok");
        exec.execute(new Runnable() {

            @Override
            public void run() {
                ServerRequest serverRequest = JSONObject.parseObject(msg.toString(), ServerRequest.class);
//                System.out.println(serverRequest.getCommand()+"," + i.incrementAndGet());
                System.out.println(serverRequest.getCommand());
                Media media = Media.newInstance();//生成中介者模式

                Response response = media.process(serverRequest);

                //向客户端发送Resonse
                ctx.channel().writeAndFlush(JSONObject.toJSONString(response)+"\r\n");
            }
        });

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

//        System.out.println("======================");
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
