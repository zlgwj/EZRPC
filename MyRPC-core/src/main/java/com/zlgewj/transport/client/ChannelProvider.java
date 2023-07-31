package com.zlgewj.transport.client;

import io.netty.channel.Channel;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 22:12
 */
public class ChannelProvider {

    private final Map<String, Channel> channelMap;

    public ChannelProvider() {
        channelMap = new ConcurrentHashMap<>();
    }

    public Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            }else{
                channelMap.remove(key);
            }
        }
        return null;
    }

    public void set(InetSocketAddress inetSocketAddress, Channel channel) {
        String key = inetSocketAddress.toString();
        channelMap.put(key, channel);
    }

    public void remove(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        channelMap.remove(key);
    }

}
