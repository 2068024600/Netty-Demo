package com.smartlab411.netty.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartlab411.netty.config.NettyClient;
import com.smartlab411.netty.constant.NettyClientConst;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class ConnectionListener implements ChannelFutureListener {

	private static final Logger log = LoggerFactory.getLogger(ConnectionListener.class);

	private NettyClient nettyClient;

	public ConnectionListener(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

	/**
	 * 重连方法
	 */
	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if (future.isSuccess()) {
			log.info("NETTY 客户端连接服务端 >>>> 服务端IP: {} PORT: {}, 成功！", NettyClientConst.IP, NettyClientConst.PORT);
		} else {
			log.error("NETTY 客户端连接服务端 >>>> 服务端IP: {} PORT: {}, 失败！ >>>>> {}秒后再次连接", NettyClientConst.IP, NettyClientConst.PORT, NettyClientConst.RECONNECT_TIME);
			Thread.sleep( NettyClientConst.RECONNECT_TIME * 1000);
			nettyClient.connect(NettyClientConst.IP, NettyClientConst.PORT);
		}
	}

}
