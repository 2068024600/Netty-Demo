package com.smartlab411.netty.run;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartlab411.netty.config.NettyServer;
import com.smartlab411.netty.constant.NettyServerConst;

/**
 * 
 * @ClassName: NettyServerRun
 * @Description: NettyServer启动类
 * @Author zhangwenhao
 * @DateTime 2022年4月4日
 */
@Component
public class NettyServerRun implements CommandLineRunner {
	
	@Autowired
	private NettyServer nettyServer;
	
	@Override
	public void run(String... args) throws Exception {
		nettyServer.run(NettyServerConst.PORT);
	}

}
