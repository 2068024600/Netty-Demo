package com.smartlab411.netty.protocol;

import com.smartlab411.netty.constant.NettyClientConst;
import com.smartlab411.utils.ConvertFactory;

import io.netty.buffer.ByteBuf;

/**
 * 
 * @ClassName: EncodeTools
 * @Description: 编码器工具
 * @Author zhangwenhao
 * @DateTime 2022年4月16日
 */
public class EncodeTools {

	/**
	 * 
	 * @Title: encodeByNothing
	 * @Description: 不做任何编码发送
	 * @Author zhangwenhao
	 * @DateTime 2022年4月16日 上午10:46:46
	 * @param msg
	 * @param out
	 */
	public static void encodeByNothing(DataFrame msg, ByteBuf out) {
		out.writeBytes(msg.getMessage());
	}

	/**
	 * 
	 * @Title: encodeByBytes
	 * @Description: 字节流编码
	 * @Author zhangwenhao
	 * @DateTime 2022年4月16日 上午10:54:04
	 * @param msg
	 * @param out
	 */
	public static void encodeByBytes(DataFrame msg, ByteBuf out) {
		// 包头
		byte head = NettyClientConst.PACKAGE_HEAD;
		// 包长
		int length = msg.getLength() + 2 + NettyClientConst.PACKAGE_LENGTH;
		// 包体
		ByteBuf message = msg.getMessage();
		// 包尾
		byte end = NettyClientConst.PACKAGE_END;
		// 业务信息, 编码发送
		out.writeByte(head);
		out.writeBytes(ConvertFactory.intToByteArray(length, NettyClientConst.PACKAGE_LENGTH));
		out.writeBytes(message);
		out.writeByte(end);
	}

	/**
	 * 
	 * @Title: encodeByString
	 * @Description:字符流编码
	 * @Author zhangwenhao
	 * @DateTime 2022年4月16日 上午10:54:40
	 * @param msg
	 * @param out
	 */
	public static void encodeByString(DataFrame msg, ByteBuf out) {
		// 包头第一位字符
		byte headOne = NettyClientConst.HEAD_ONE;
		// 包头第二位字符
		byte headTwo = NettyClientConst.HEAD_TWO;
		// 包长
		String length = Integer.toHexString(msg.getLength() + 2 + NettyClientConst.PACKAGE_LENGTH).toUpperCase();
		// 转16进制字符串数字补全
		while (length.length() != NettyClientConst.PACKAGE_LENGTH) {
			length = "0" + length;
		}
		// 包体
		ByteBuf message = msg.getMessage();
		// 包尾第一位字符
		byte endOne = NettyClientConst.END_ONE;
		// 包尾第二位字符
		byte endTwo = NettyClientConst.END_TWO;
		// 业务信息, 编码发送
		out.writeByte(headOne);
		out.writeByte(headTwo);
		out.writeBytes(length.getBytes());
		out.writeBytes(message);
		out.writeByte(endOne);
		out.writeByte(endTwo);
	}

}
