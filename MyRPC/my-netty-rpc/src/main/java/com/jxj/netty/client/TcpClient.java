package com.jxj.netty.client;

import com.alibaba.fastjson.JSONObject;
import com.jxj.netty.handler.SimpleClientHandler;
import com.jxj.netty.util.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Description
 */
public class TcpClient {

    static final Bootstrap bootstrap = new Bootstrap();
    static  ChannelFuture future = null;

    static {
        String host = "localhost";
        int port = 8080;
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0] ));
                        pipeline.addLast(new StringDecoder());

                        pipeline.addLast(new SimpleClientHandler());
                        pipeline.addLast(new StringEncoder());
                    }
                });

        try {
            future = bootstrap.connect(host, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 1 ： 每一个请求都是同一个连接，需要考虑并发问题
    // 发送数据
    public static Response send(ClientRequest request) {

        future.channel().writeAndFlush(JSONObject.toJSONString(request));
        future.channel().writeAndFlush("\r\n");

        DefaultFuture df = new DefaultFuture(request);
        return df.get();
    }
}
