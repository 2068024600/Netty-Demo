package com.smartlab411.netty.protocol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartlab411.netty.constant.NettyServerConst;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

/**
 * 
 * @ClassName: DataFrameProtocol
 * @Description: 编解码器
 * @Author zhangwenhao
 * @DateTime 2022年4月6日
 */
public class DataFrameProtocol extends ByteToMessageCodec<DataFrame> {

	private static final Logger log = LoggerFactory.getLogger(DataFrameProtocol.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		switch (NettyServerConst.DECODE_WAY) {
		// 不做任何解码
		case 0: {
			DecodeTools.decodeByNothing(in, out);
			break;
		}
		// 字节流解码
		case 1: {
			DecodeTools.decodeByBytes(in, out);
			break;
		}
		// 字符流解码
		case 2: {
			DecodeTools.decodeByString(in, out);
			break;
		}
		// 默认不做任何操作
		default:
			log.info("接收到消息，但是默认不做任何操作");
			break;
		}

	}

	@Override
	protected void encode(ChannelHandlerContext ctx, DataFrame msg, ByteBuf out) throws Exception {
		switch (NettyServerConst.DECODE_WAY) {
		// 不做任何编码
		case 0: {
			EncodeTools.encodeByNothing(msg, out);
			break;
		}
		// 字节流编码
		case 1: {
			EncodeTools.encodeByBytes(msg, out);
			break;
		}
		// 字符流编码
		case 2: {
			EncodeTools.encodeByString(msg, out);
			break;
		}
		// 默认不做任何操作
		default:
			log.info("检测到有消息发送，但是默认不做任何操作");
			break;
		}

	}

}
