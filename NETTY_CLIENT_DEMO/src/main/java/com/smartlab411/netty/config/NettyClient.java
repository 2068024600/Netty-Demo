package com.smartlab411.netty.config;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smartlab411.netty.constant.NettyClientConst;
import com.smartlab411.netty.handler.HeartBeatClientHandler;
import com.smartlab411.netty.handler.NettyClientHandler;
import com.smartlab411.netty.protocol.DataFrameProtocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 
 * @ClassName: NettyClient
 * @Description: Netty客户端配置
 * @Author zhangwenhao
 * @DateTime 2022年4月14日
 */
@Configuration
public class NettyClient {

	private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

	/**
	 * 创建worker线程组，处理已经接收的连接
	 */
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	/**
	 * 
	 * @Title: nettyClientHandler
	 * @Description: netty客户端业务处理
	 * @Author zhangwenhao
	 * @DateTime 2022年4月15日 上午11:35:48
	 * @return
	 */
	@Bean
	public NettyClientHandler nettyClientHandler() {
		return new NettyClientHandler();
	}

	/**
	 * 
	 * @Title: heartBeatClientHandler
	 * @Description: 心跳业务员处理
	 * @Author zhangwenhao
	 * @DateTime 2022年4月15日 下午7:12:18
	 * @return
	 */
	@Bean
	public HeartBeatClientHandler heartBeatClientHandler() {
		return new HeartBeatClientHandler();
	}

	/**
	 * 
	 * @Title: connect
	 * @Description: 客户端连接
	 * @Author zhangwenhao
	 * @DateTime 2022年4月14日 下午9:29:34
	 * @param host
	 * @param port
	 * @throws Exception
	 */
	public void connect(String host, int port) throws Exception {
		Bootstrap bootstrap = new Bootstrap();
		try {
			bootstrap.group(workerGroup)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NettyClientConst.CONNECT_TIMEOUT * 1000)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							// 设置空闲时间
							pipeline.addLast("idle", new IdleStateHandler(NettyClientConst.IDLE_TIME, NettyClientConst.IDLE_TIME, 0));
							// 设置读超时时间, 当一段时间未收到服务端心跳，断开连接
							pipeline.addLast("readTimeOut", new ReadTimeoutHandler(NettyClientConst.HEART_TIMEOUT));
							// 心跳检测处理
							pipeline.addLast("heart", heartBeatClientHandler());
							// 解码器
							pipeline.addLast("decoder", new DataFrameProtocol());
							// 业务处理
							pipeline.addLast("handler", nettyClientHandler());
						}
					});
			// 启动客户端连接服务端
			bootstrap.connect(new InetSocketAddress(host, port)).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						log.info("NETTY 客户端连接服务端成功！ >>>> 服务端IP: {} PORT: {}", NettyClientConst.IP,
								NettyClientConst.PORT);
					} else {
						log.error("NETTY 客户端连接服务端失败！ >>>> 服务端IP: {} PORT: {} >>>>> {}秒后再次连接", NettyClientConst.IP,
								NettyClientConst.PORT, NettyClientConst.RECONNECT_TIME);
						Thread.sleep(NettyClientConst.RECONNECT_TIME * 1000);
						connect(NettyClientConst.IP, NettyClientConst.PORT);
					}
				}
			}).sync();
		} catch (Exception e) {
			log.error("NETTY 客户端连接出错: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: shut
	 * @Description: 客户端停止，关闭线程组
	 * @Author zhangwenhao
	 * @DateTime 2022年4月4日 下午3:12:21
	 */
	@PreDestroy
	private void shut() {
		workerGroup.shutdownGracefully();
		log.info(">>>> workerGroup shutdown");
	}
}
