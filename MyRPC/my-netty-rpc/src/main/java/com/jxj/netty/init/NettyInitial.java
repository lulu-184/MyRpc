package com.jxj.netty.init;

import com.jxj.netty.constant.Constants;
import com.jxj.netty.factory.ZookeeperFactory;
import com.jxj.netty.handler.ServerHandler;
import com.jxj.netty.handler.SimpleServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 */
@Component
public class NettyInitial implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.start();
    }

    public void start() {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup,workGroup)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, false)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()[0]));
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new IdleStateHandler(60, 45, 20, TimeUnit.SECONDS));
                            pipeline.addLast(new ServerHandler());
                            pipeline.addLast(new StringEncoder());
                        }
                    });

            int port = 8080;

            ChannelFuture future = bootstrap.bind(8080).sync();
            CuratorFramework client = ZookeeperFactory.create();
            InetAddress netAddress = InetAddress.getLocalHost();
//            client.create().withMode(CreateMode.EPHEMERAL).forPath(Constants.ServerPATH + netAddress.getHostAddress());
            client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).
                    forPath(Constants.ServerPATH + "/" + netAddress.getHostAddress()
                            + "#" + port + "#");
            future.channel().closeFuture().sync();
        } catch (Exception e ) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}
