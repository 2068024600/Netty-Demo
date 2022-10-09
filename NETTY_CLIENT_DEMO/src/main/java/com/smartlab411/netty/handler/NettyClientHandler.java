package com.smartlab411.netty.handler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartlab411.netty.config.NettyClient;
import com.smartlab411.netty.constant.NettyClientConst;
import com.smartlab411.netty.protocol.DataFrame;
import com.smartlab411.utils.ByteBufUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * @ClassName: NettyClientHandler
 * @Description: Netty客户端业务处理
 * @Author zhangwenhao
 * @DateTime 2022年4月4日
 */
@Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(NettyClientHandler.class);
	
	private ChannelHandlerContext ctx = null;
	
	@Autowired
	private NettyClient nettyClient;
	
	@Autowired
	private SendMessageHandler sendMessageHandler;

	/**
	 * 注册
	 */
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
	}

	/**
	 * 激活
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		ctx.executor().scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				sendMessageHandler.send(ctx, "111");
			}
		}, 2, 1, TimeUnit.SECONDS);
	}

	/**
	 * 断开
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("NETTY TCP客户端连接断开 >>>> 服务端地址: {}:{}", NettyClientConst.IP, NettyClientConst.PORT);
		// 重连
		nettyClient.connect(NettyClientConst.IP, NettyClientConst.PORT);
	}

	/**
	 * 注销
	 */
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
	}

	/**
	 * 接收数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		DataFrame dataFrame = (DataFrame) msg;
		byte[] message = ByteBufUtil.byteBufToBytes(dataFrame.getMessage());
		log.info("NETTY TCP客户端 >>> 接收数据 >>>> type: byte[], message: {}", message);
		log.info("NETTY TCP客户端 >>> 接收数据 >>>> type: String, message: {}", ByteBufUtil.byteBufToString(dataFrame.getMessage()));
	}

	/**
	 * 接收数据完成
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
	}

	/**
	 * 可写状态变更
	 */
	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelWritabilityChanged(ctx);
	}

	/**
	 * 连接异常
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		log.error("NETTY TCP客户端发生异常，连接断开 Error >>>>>>{}", cause.getMessage());
		cause.printStackTrace();
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	
}
