package com.smartlab411.netty.handler;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartlab411.netty.constant.NettyClientConst;
import com.smartlab411.utils.ByteBufUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 
 * @ClassName: HeartBeatClientHandler
 * @Description: 客户端心跳包处理
 * @Author zhangwenhao
 * @DateTime 2022年4月15日
 */
@Sharable
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(HeartBeatClientHandler.class);

	@Autowired
	private SendMessageHandler sendMessageHandler;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 接收信息
		ByteBuf buf = (ByteBuf) msg;
		byte[] b = ByteBufUtil.byteBufToBytes(buf);
		log.info("heart: {}", b);
		log.info("true heart: {}", NettyClientConst.RETURN_HEART_BEAT);
		// 判断客户端发送的消息是否为心跳
		if (!Arrays.equals(NettyClientConst.RETURN_HEART_BEAT, b)) {
			/*
			 * 这里的客户端只关心服务端是否回复，回复心跳后过滤
			 */
			// 向后传递消息,让业务handler处理
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 触发读写空闲，客户端开始发送心跳
		if (evt instanceof IdleStateEvent) {
			// 判断当前空闲状态
			IdleState state = ((IdleStateEvent) evt).state();
			// 当客户端一段时间内未收到服务端消息, 发送心跳, 判断服务端是否在线
			if (state == IdleState.READER_IDLE) {
				// 发送心跳
				sendMessageHandler.sendHeartBeat(ctx);
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

}
