package com.smartlab411.netty.protocol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smartlab411.netty.constant.NettyServerConst;
import com.smartlab411.utils.ConvertFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 
 * @ClassName: DecodeTools
 * @Description: 解码工具
 * @Author zhangwenhao
 * @DateTime 2022年4月8日
 */
public class DecodeTools {

	private static final Logger log = LoggerFactory.getLogger(DecodeTools.class);

	/**
	 * 
	 * @Title: decodeByNothing
	 * @Description: 不做任何解析
	 * @Author zhangwenhao
	 * @DateTime 2022年4月8日 下午9:00:36
	 * @param in
	 * @return
	 */
	public static void decodeByNothing(ByteBuf in, List<Object> out) {
		// 这里要复制一份ByteBuf，不然会触发异常
		ByteBuf message = in.readBytes(in.readableBytes());
		DataFrame dataFrame = new DataFrame(message);
		out.add(dataFrame);
	}

	/**
	 * 
	 * @Title: decodeByBytes
	 * @Description: 解析字节流
	 * @Author zhangwenhao
	 * @DateTime 2022年4月9日 下午4:06:20
	 * @param ctx
	 * @param in
	 * @param out
	 */
	public static void decodeByBytes(ByteBuf in, List<Object> out) {

		while (in.isReadable()) {
			// 读取包头
			if (in.readByte() == NettyServerConst.PACKAGE_HEAD) {
				// 记录包头的位置
				int position = in.readerIndex() - 1;
				
				try {
					// 读取包头后标记，方便数据回滚
					in.markReaderIndex();
					// 读取包长(包头下一位就是包长)
					String lenString = "";
					for (int i = 0; i < NettyServerConst.PACKAGE_LENGTH; i++) {
						lenString = lenString + ConvertFactory.byteToHexString(in.readByte());
					}
					int len = ConvertFactory.hexToInt(lenString);
					// 获取长度减掉包头、包长、包尾长度即可获取包体真实长度
					len = len - 2 - NettyServerConst.PACKAGE_LENGTH;
					// 判断当前读取的包长是否正确
					if (len <= 0) {
						/*
						 * 包长小于0说明传入的包长有误,数据回滚，重新解析
						 */
						in.resetReaderIndex();
						decodeByBytes(in, out);
						return;
					}
					// 向后读取len位字节
					ByteBuf message = Unpooled.buffer(len);
					message = in.readBytes(len);
					// 数据解析完毕，验证包尾
					if (in.readByte() == NettyServerConst.PACKAGE_END) {
						// 当前数据包末尾还有数据，但是一个包已经解析完成
						if (in.isReadable()) {
							// 标记当前位置, 再次解析
							in.markReaderIndex();
							decodeByBytes(in, out);
						}
						// 将包体数据写入out
						DataFrame dataFrame = new DataFrame(message);
						out.add(dataFrame);
						assert in.refCnt() == 0;
						return;
					} else {
						// 包尾有误说明数据解析错误，回滚数据，再次解析
						in.resetReaderIndex();
						decodeByBytes(in, out);
					}
				} catch (IndexOutOfBoundsException iobExcetion) {
					/*
					 * 出现数组越界异常，说明出现了掉包 保存这次接收的包，与下一次发送的包进行拼接
					 */
					in.readerIndex(position);
					in.markReaderIndex();
					return;
				} catch (Exception e) {
					log.error("error: {}", e.getMessage());
					e.printStackTrace();
					return;
				}
			}
		}
	}

	/**
	 * 
	 * @Title: decodeByString
	 * @Description: 字符流解码
	 * @Author zhangwenhao
	 * @DateTime 2022年4月11日 上午11:08:32
	 * @param ctx
	 * @param in
	 * @param out
	 */
	public static void decodeByString(ByteBuf in, List<Object> out) {
		while (in.isReadable()) {
			// 判断包头第一位
			if (in.readByte() == NettyServerConst.HEAD_ONE) {
				// 在包头做个标记
				int position = in.readerIndex() - 1;

				try {
					// 判断后面是否还有数据 然后再次判断包头的第二位
					if (in.readByte() == NettyServerConst.HEAD_TWO) {
						// 标记，方便数据回滚
						in.markReaderIndex();
						// 获取包长
						String lenString = "";
						for (int i = 0; i < NettyServerConst.PACKAGE_LENGTH; i++) {
							lenString = lenString + String.valueOf((char) in.readByte());
						}
						int len = ConvertFactory.hexToInt(lenString);
						// 获取长度减掉包头、包长、包尾长度即可获取包体真实长度
						len = len - 4 - NettyServerConst.PACKAGE_LENGTH;
						// 判断包长是否大于0
						if (len <= 0) {
							// 小于0说明数据包有误，去掉包头再次解析
							in.resetReaderIndex();
							decodeByString(in, out);
						}
						// 向后读取len长度的包体
						ByteBuf message = in.readBytes(len);
						// 读取包体后，判断包尾
						if (in.readByte() == NettyServerConst.END_ONE && in.readByte() == NettyServerConst.END_TWO) {
							// 当前数据包末尾还有数据，但是一个包已经解析完成
							if (in.isReadable()) {
								// 标记当前位置, 再次解析
								in.markReaderIndex();
								decodeByString(in, out);
							}
							// 将包体数据写入out
							DataFrame dataFrame = new DataFrame(message);
							out.add(dataFrame);
							return;
						} else {
							// 包尾错误，说明数据包解析错误，去掉包头再次解析
							in.resetReaderIndex();
							decodeByString(in, out);
						}
					}
				} catch (IndexOutOfBoundsException e) {
					// 出现数组越界异常，说明后面已经没有数据，则等待下一个包进行拼接
					in.readerIndex(position);
					return;
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

}
