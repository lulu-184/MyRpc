package com.jxj.client.core;

import com.jxj.client.zk.ZookeeperFactory;
import io.netty.channel.ChannelFuture;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.HashSet;
import java.util.List;

/**
 * @Description
 */
public class ServerWatcher implements CuratorWatcher {

    @Override
    public void process(WatchedEvent event) throws Exception {
        CuratorFramework client = ZookeeperFactory.create();

        System.out.println("进入watcher");
        String path = event.getPath();
        client.getChildren().usingWatcher(this).forPath(path);
        List<String> serverPaths = client.getChildren().forPath(path);
        System.out.println(serverPaths);
        ChannelManager.clear();
        for (String serverPath : serverPaths) {
            String[] str = serverPath.split("#");
            ChannelManager.realServerPath.add(str[0] +"#" + str[1]);
        }
        ChannelManager.clear();
        for (String realServer : ChannelManager.realServerPath){
            String[] str = realServer.split("#");
//            ChannelManager.realServerPath.add(str[0] +"#" + str[1]);
            ChannelFuture channelFuture = TcpClient.bootstrap.connect(str[0], Integer.valueOf(str[1]));
            ChannelManager.add(channelFuture);
        }



    }
}
