package com.smartlab411.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.util.HashSet;
import java.util.Map;

public class ByteBufUtil {

	/**
	 * 
	 * @Title: byteToByteBuf
	 * @Description: byte转ByteBuf
	 * @Author zhangwenhao
	 * @DateTime 2022年4月7日 下午8:57:11
	 * @param bytes
	 * @return
	 */
	public static ByteBuf byteToByteBuf(byte[] bytes) {
		// 创建byteBuf
		ByteBuf buf = Unpooled.wrappedBuffer(bytes);
		return buf;
	}

	/**
	 * 
	 * @Title: byteBufToBytes
	 * @Description: ByteBuf转Byte数组
	 * @Author zhangwenhao
	 * @DateTime 2022年4月7日 下午9:00:33
	 * @param buf
	 * @return
	 */
	public static byte[] byteBufToBytes(ByteBuf buf) {
		int len = ByteBufUtil.getByteBufLength(buf);
		byte[] bytes = new byte[len];
		// 将buf缓冲区的数据传到bytes中
		buf.readBytes(bytes);
		// 读取完成后，记得重置buf的reader指针
		buf.resetReaderIndex();
		return bytes;
	}

	/**
	 * 
	 * @Title: getByteBufferLength
	 * @Description: 获取byteBuffer长度
	 * @Author zhangwenhao
	 * @DateTime 2022年4月7日 下午9:02:35
	 * @param buffer
	 * @return
	 */
	public static int getByteBufLength(ByteBuf buf) {
		// 获取byteBuf长度即获取byteBuf的可读字节数
		return buf.writerIndex() - buf.readerIndex();
	}

	/**
	 * 
	 * @Title: byteBufToString
	 * @Description: byteBuf转String
	 * @Author zhangwenhao
	 * @DateTime 2022年4月8日 下午4:24:42
	 * @param buf
	 * @return
	 */
	public static String byteBufToString(ByteBuf buf) {
		if (buf == null) {
			return "";
		}
		return buf.toString(CharsetUtil.UTF_8);
	}

	/**
	 * 
	 * @Title: StringToByteBuf
	 * @Description: String转ByteBuf
	 * @Author zhangwenhao
	 * @DateTime 2022年4月8日 下午5:14:26
	 * @param message
	 * @return
	 */
	public static ByteBuf StringToByteBuf(String message) {
		if (message == null) {
			return null;
		}
		byte[] bytes = message.getBytes(CharsetUtil.UTF_8);
		return ByteBufUtil.byteToByteBuf(bytes);
	}

	Map

}
