package com.smartlab411.netty.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.smartlab411.netty.constant.NettyServerConst;
import com.smartlab411.netty.handler.HeartBeatServerHandler;
import com.smartlab411.netty.handler.NettyServerHandler;
import com.smartlab411.netty.protocol.DataFrameProtocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 * @ClassName: NettyServerConfig
 * @Description: NettyServer服务端配置
 * @Author zhangwenhao
 * @DateTime 2022年4月4日
 */
@Configuration
public class NettyServer {

	private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

	/**
	 * 创建boos线程组, 处理接收进来的连接
	 */
	EventLoopGroup bossGroup = new NioEventLoopGroup();
	
	/**
	 *  创建worker线程组，处理已经接收的连接
	 */
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	/**
	 * 
	 * @Title: HeartBeatServerHandler
	 * @Description: 心跳处理
	 * @Author zhangwenhao
	 * @DateTime 2022年4月8日 下午5:32:07
	 * @return
	 */
	@Bean
	public HeartBeatServerHandler heartBeatServerHandler() {
		return new HeartBeatServerHandler();
	}
	
	/**
	 * 
	 * @Title: NettyServerHandler
	 * @Description: 业务处理
	 * @Author zhangwenhao
	 * @DateTime 2022年4月8日 下午5:30:56
	 * @return
	 */
	@Bean
	public NettyServerHandler nettyServerHandler() {
		return new NettyServerHandler();
	}

	/**
	 * 
	 * @Title: run
	 * @Description: NettyServer启动
	 * @Author zhangwenhao
	 * @DateTime 2022年4月4日 下午3:02:01
	 * @param port
	 * @throws Exception
	 */
	public void run(int port) throws Exception {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		try {
			serverBootstrap.group(bossGroup, workerGroup)
			        .channel(NioServerSocketChannel.class) // 使用NioServerSocketChannel来接收连接
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							// 设置空闲时间 
							pipeline.addLast("idle", new IdleStateHandler(0, 0, NettyServerConst.IDLE_TIME, TimeUnit.SECONDS));
							// 心跳检测处理
							pipeline.addLast("heart", heartBeatServerHandler());
							// 解码器
							pipeline.addLast("decoder", new DataFrameProtocol());
							// 业务处理
							pipeline.addLast("handler", nettyServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128) // 设置服务端最大连接数量
					.childOption(ChannelOption.SO_REUSEADDR, true);  // 重用端口，当服务端非正常退出时，需要一定时间关闭端口才能正常使用，不设置SO_REUSEADDR就无法正常使用

			// 绑定端口启动连接
			ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
			boolean isSuccess = channelFuture.isSuccess();
			if (isSuccess) {
				log.info("NETTY 服务端启动成功 >>>>>> PORT: {}", port);
			} else {
				log.error("NETTY 服务端启动失败 >>>>>> PORT: {}", port);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("NETTY 服务端启动出错: {}", e.getMessage());
		}
	}

	/**
	 * 
	 * @Title: shut
	 * @Description: 服务端停止，关闭线程组
	 * @Author zhangwenhao
	 * @DateTime 2022年4月4日 下午3:12:21
	 */
	@PreDestroy
	private void shut() {
		workerGroup.shutdownGracefully();
		log.info(">>>> workerGroup shutdown");
		bossGroup.shutdownGracefully();
		log.info(">>>> bossGroup shutdown");
	}
}
