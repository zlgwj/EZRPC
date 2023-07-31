package com.zlgewj.transport.client;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 22:12
 */
public class ChannelPool {

    private final Map<String, Channel> channelMap;

    private final Map<String, InetSocketAddress> inetSocketAddressMap;

    public ChannelPool() {
        channelMap = new ConcurrentHashMap<>();
        inetSocketAddressMap = new ConcurrentHashMap<>();
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
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
        String channelId = channel.id().toString();
        channelMap.put(key, channel);
        inetSocketAddressMap.put(channelId, inetSocketAddress);
    }

    public void removeChannel(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        channelMap.remove(key);
    }
    public InetSocketAddress getInetSocketAddress(String channelId) {
        return inetSocketAddressMap.get(channelId);
    }

}
