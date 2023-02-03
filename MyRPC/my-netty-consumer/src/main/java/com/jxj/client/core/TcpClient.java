package com.jxj.client.core;

import com.alibaba.fastjson.JSONObject;
import com.jxj.client.constant.Constants;
import com.jxj.client.handler.SimpleClientHandler;
import com.jxj.client.param.ClientRequest;
import com.jxj.client.param.Response;
import com.jxj.client.zk.ZookeeperFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.Watcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description
 */
public class TcpClient {

    static final Bootstrap bootstrap = new Bootstrap();
    static  ChannelFuture future = null;

//    static Set<String> realServerPath =  new HashSet<>();

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

        CuratorFramework client = ZookeeperFactory.create();
        try {
            List<String> serverPaths = client.getChildren().forPath(Constants.ServerPATH);

            CuratorWatcher watcher = new ServerWatcher();
            // 加上zk监听服务器变化
            client.getChildren().usingWatcher(watcher).forPath(Constants.ServerPATH);

            for (String serverPath : serverPaths) {
                String[] str = serverPath.split("#");
                ChannelManager.realServerPath.add(str[0] + "#" + str[1]);
                ChannelFuture channelFuture = TcpClient.bootstrap.connect(str[0], Integer.valueOf(str[1]));
                ChannelManager.add(channelFuture);
            }
            if (ChannelManager.realServerPath.size() > 0) {
                String[] hostAndPort = ChannelManager.realServerPath.toArray()[0].toString().split("#");
                host = hostAndPort[0];
                port = Integer.valueOf(hostAndPort[1]);
            }
            future = bootstrap.connect(host, port).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static int i = 0;
    // 每一个请求都是同一个连接，需要考虑并发问题
    // 发送数据
    public static Response send(ClientRequest request) {

        future = ChannelManager.get(ChannelManager.position);
        future.channel().writeAndFlush(JSONObject.toJSONString(request));
        future.channel().writeAndFlush("\r\n");

        DefaultFuture df = new DefaultFuture(request);
        return df.get();
    }
}
