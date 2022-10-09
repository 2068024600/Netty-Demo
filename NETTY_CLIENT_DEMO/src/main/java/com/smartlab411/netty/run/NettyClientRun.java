package com.smartlab411.netty.run;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartlab411.netty.config.NettyClient;
import com.smartlab411.netty.constant.NettyClientConst;

/**
 * 
 * @ClassName: NettyClientRun
 * @Description: Netty客户端启动类
 * @Author zhangwenhao
 * @DateTime 2022年4月15日
 */
@Component
public class NettyClientRun implements CommandLineRunner {
	
	@Autowired
	private NettyClient nettyClient;
	
	@Override
	public void run(String... args) throws Exception {
		nettyClient.connect(NettyClientConst.IP, NettyClientConst.PORT);
	}

}
