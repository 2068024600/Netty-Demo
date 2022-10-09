package com.smartlab411.netty.handler;

import java.net.InetSocketAddress;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartlab411.netty.constant.NettyServerConst;
import com.smartlab411.utils.ByteBufUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 * 
 * @ClassName: HeartBeatServerHandler
 * @Description: 心跳业务处理
 * @Author zhangwenhao
 * @DateTime 2022年4月7日
 */
@Sharable
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(HeartBeatServerHandler.class);
	
	@Autowired
	private SendMessageHandler sendMessageHandler;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 接收信息
		ByteBuf buf = (ByteBuf) msg;
		byte[] b = ByteBufUtil.byteBufToBytes(buf);
//		log.info("heart: {}", b);
//		log.info("true heart: {}", NettyServerConst.RETURN_HEART_BEAT);
		// 判断客户端发送的消息是否为心跳
		if (Arrays.equals(NettyServerConst.RETURN_HEART_BEAT, b)) {
			// 发送心跳
			sendMessageHandler.sendHeartBeat(ctx);
			// 忽略掉消息，不让业务handle处理
			ReferenceCountUtil.release(msg);
		} else {
			// 向后传递消息,让业务handler处理
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// NettyServerConst.IDLE_TIME秒后触发读写空闲，说明心跳已经断开
		if (evt instanceof IdleStateEvent) {
			IdleState state = ((IdleStateEvent) evt).state();
			if (state == IdleState.ALL_IDLE) {
				InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
				String address = ipSocket.getAddress().getHostAddress();
				log.error("NETTY服务端 TCP 心跳超时 关闭连接 >>>>> 客户端IP: {}", address);
				ctx.close();
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

}
