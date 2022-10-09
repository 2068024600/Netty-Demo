package com.smartlab411.netty.handler;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.smartlab411.netty.constant.NettyServerConst;
import com.smartlab411.netty.protocol.DataFrame;
import com.smartlab411.utils.ByteBufUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @ClassName: SendMessageHandler
 * @Description: 发送消息业务
 * @Author zhangwenhao
 * @DateTime 2022年4月7日
 */
@Service
public class SendMessageHandler {

	/**
	 * 
	 * @Title: sendHeartBeat
	 * @Description: 发送心跳包
	 * @Author zhangwenhao
	 * @DateTime 2022年4月8日 下午12:38:36
	 * @param channelContext
	 * @throws Exception
	 */
	public boolean sendHeartBeat(ChannelHandlerContext ctx) throws Exception {
		// 获取心跳包
		ByteBuf heartBeat = Unpooled.buffer(NettyServerConst.HEART_BEAT.length);
		heartBeat.writeBytes(NettyServerConst.HEART_BEAT);
		ChannelFuture future = ctx.writeAndFlush(heartBeat);
		return future.isSuccess();
	}

	/**
	 * 
	 * @Title: send
	 * @Description: 发送字符串类信息
	 * @Author zhangwenhao
	 * @DateTime 2022年4月8日 下午12:44:52
	 * @param channelContext
	 * @param message
	 */
	public boolean send(ChannelHandlerContext channelContext, String message) {
		ByteBuf buf = ByteBufUtil.StringToByteBuf(message);
		DataFrame dataFrame = new DataFrame(buf);
		ChannelFuture future = channelContext.writeAndFlush(dataFrame);
		return future.isSuccess();
	}

	/**
	 * 
	 * @Title: send
	 * @Description: 发送字节数组类信息
	 * @Author zhangwenhao
	 * @DateTime 2022年4月8日 下午7:42:18
	 * @param channelContext
	 * @param bytes
	 */
	public boolean send(ChannelHandlerContext ctx, byte[] bytes) {
		ByteBuf buf = ByteBufUtil.byteToByteBuf(bytes);
		DataFrame dataFrame = new DataFrame(buf);
		ChannelFuture future = ctx.writeAndFlush(dataFrame);
		return future.isSuccess();
	}
	
	/**
	 * 
	 * @Title: send
	 * @Description: 发送JSON信息
	 * @Author zhangwenhao
	 * @DateTime 2022年4月16日 下午3:49:43
	 * @param ctx
	 * @param message
	 * @return
	 */
	public boolean send(ChannelHandlerContext ctx, JSONObject message) {
		return send(ctx, message.toString());
	}

}
