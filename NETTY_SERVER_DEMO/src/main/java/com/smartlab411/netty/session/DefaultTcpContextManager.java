package com.smartlab411.netty.session;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @ClassName: DefaultTcpContextManager
 * @Description: TCP会话控制
 * @Author zhangwenhao
 * @DateTime 2022年4月13日
 */
@Component
public class DefaultTcpContextManager {

	private ConcurrentHashMap<String, ChannelHandlerContext> contexts = new ConcurrentHashMap<>();

	/**
	 * 
	 * @Title: setContext
	 * @Description: 存放ChannelHandlerContext
	 * @Author zhangwenhao
	 * @DateTime 2022年4月13日 下午7:54:24
	 * @param context
	 */
	public void setContext(ChannelHandlerContext context) {
		// 获取客户端地址
		InetSocketAddress ipSocket = (InetSocketAddress) context.channel().remoteAddress();
		String key = ipSocket.toString();
		contexts.put(key, context);
	}

	/**
	 * 
	 * @Title: setContext
	 * @Description: 存放ChannelHandlerContext
	 * @Author zhangwenhao
	 * @DateTime 2022年4月13日 下午9:01:25
	 * @param key
	 * @param context
	 */
	public void setContext(String key, ChannelHandlerContext context) {
		contexts.put(key, context);
	}

	/**
	 * 
	 * @Title: deleteContext
	 * @Description: 根据地址删除
	 * @Author zhangwenhao
	 * @DateTime 2022年4月13日 下午9:02:28
	 * @param key
	 */
	public void deleteContext(String key) {
		contexts.remove(key);
	}

	/**
	 * 
	 * @Title: deleteContext
	 * @Description: 根据地址删除
	 * @Author zhangwenhao
	 * @DateTime 2022年4月13日 下午9:02:28
	 * @param ipSocket
	 */
	public void deleteContext(InetSocketAddress ipSocket) {
		String address = ipSocket.toString();
		contexts.remove(address);
	}

	/**
	 * 
	 * @Title: deleteContext
	 * @Description: 根据context删除
	 * @Author zhangwenhao
	 * @DateTime 2022年4月13日 下午9:08:58
	 * @param context
	 */
	public void deleteContext(ChannelHandlerContext context) {
		if (context == null) {
			return;
		}
		// 获取客户端地址
		InetSocketAddress ipSocket = (InetSocketAddress) context.channel().remoteAddress();
		contexts.remove(ipSocket.toString());
	}

	/**
	 * 
	 * @Title: close
	 * @Description: 断开客户端连接
	 * @Author zhangwenhao
	 * @DateTime 2022年4月18日 上午9:30:14
	 * @param context
	 */
	public void close(ChannelHandlerContext context) {
		// 获取客户端地址
		InetSocketAddress ipSocket = (InetSocketAddress) context.channel().remoteAddress();
		contexts.remove(ipSocket.toString());
		context.close();
	}

	public ConcurrentHashMap<String, ChannelHandlerContext> getContexts() {
		return contexts;
	}

}
