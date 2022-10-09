package com.smartlab411.netty.protocol;

import com.smartlab411.utils.ByteBufUtil;

import io.netty.buffer.ByteBuf;

/**
 * 
 * @ClassName: DataFrame
 * @Description: 数据报类
 * @Author zhangwenhao
 * @DateTime 2022年4月4日
 */
public class DataFrame {

	/**
	 * 包长
	 */
	private int length;

	/**
	 * 包体
	 */
	private ByteBuf message;

	/**
	 * 
	 * @param message 数据
	 */
	public DataFrame(ByteBuf message) {
		if (message == null) {
			return;
		}
		this.message = message;
		this.length = ByteBufUtil.getByteBufLength(message);
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public ByteBuf getMessage() {
		return message;
	}

	public void setMessage(ByteBuf message) {
		this.message = message;
	}
}
