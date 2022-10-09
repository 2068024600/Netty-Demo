package com.smartlab411.netty.handler;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartlab411.netty.protocol.DataFrame;
import com.smartlab411.netty.session.DefaultTcpContextManager;
import com.smartlab411.utils.ByteBufUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * @ClassName: NettyServerHandler
 * @Description: 业务处理
 * @Author zhangwenhao
 * @DateTime 2022年4月4日
 */
@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(NettyServerHandler.class);

	@Autowired
	private DefaultTcpContextManager defaultTcpSessionManager;

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
		// 获取客户端地址
		InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String address = ipSocket.toString();
		// 判断当前是否有相同地址的客户端连接
		ChannelHandlerContext context = defaultTcpSessionManager.getContexts().get(address);
		if (context != null) {
			// 当前已有相同地址的客户端连接，断开前一个连接的客户端
			defaultTcpSessionManager.close(context);
		}
		defaultTcpSessionManager.setContext(address, ctx);
		log.info("NETTY TCP服务端 >>> 客户端连接 >>>> 客户端IP: {}", ipSocket);
		log.info("NETTY TCP服务端 >>> 当前客户端连接数量: {} ", defaultTcpSessionManager.getContexts().size());
		sendMessageHandler.send(ctx, "hello");
	}

	/**
	 * 断开
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 获取客户端地址
		InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
		defaultTcpSessionManager.deleteContext(ipSocket);
		log.info("NETTY TCP服务端 >>> 客户端断开 >>>> 客户端IP: {}", ipSocket);
		log.info("NETTY TCP服务端 >>> 当前客户端连接数量: {} ", defaultTcpSessionManager.getContexts().size());
	}

	/**
	 * 注销
	 */
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
	}

	/**
	 * 接收数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		DataFrame dataFrame = (DataFrame) msg;
		byte[] message = ByteBufUtil.byteBufToBytes(dataFrame.getMessage());
		log.info("NETTY TCP服务端 >>> 接收数据 >>>> type: byte[], message: {}", message);
		log.info("NETTY TCP服务端 >>> 接收数据 >>>> type: String, message: {}", ByteBufUtil.byteBufToString(dataFrame.getMessage()));
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
		InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
		ctx.close();
		log.error("NETTY TCP服务端 >>> 客户端连接服务端异常，连接即将断开 Error >>>>>>{}, 客户端IP: {}", cause.getMessage(), ipSocket);
		cause.printStackTrace();
	}

}
