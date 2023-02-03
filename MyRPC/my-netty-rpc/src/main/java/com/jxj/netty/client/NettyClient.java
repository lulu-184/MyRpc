package com.jxj.netty.client;

import com.jxj.netty.handler.SimpleClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 */
public class NettyClient {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        String host = "localhost";
        int port = 8080;
        EventLoopGroup group = new NioEventLoopGroup();
        try {

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

            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush("hello server");
            future.channel().writeAndFlush("\r\n");
            future.channel().closeFuture().sync();
            Object result = future.channel().attr(AttributeKey.valueOf("sssss")).get();// 异步获取响应
            System.out.println("获取到服务器返回数据:" + result.toString());
        } catch (Exception e ) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
