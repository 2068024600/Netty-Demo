package com.smartlab411.netty.constant;

/**
 * 
 * @ClassName: NettyServerConst
 * @Description: NettyServer常量类
 * @Author zhangwenhao
 * @DateTime 2022年4月4日
 */
public class NettyServerConst {

	/**
	 * 服务端启动绑定端口
	 */
	public static final int PORT = 8080;
	
	/**
	 * 服务端空闲时间/服务端心跳超时时间
	 */
	public static final int IDLE_TIME = 30;
	
	/**
	 * 服务端回复心跳
	 */
	public static final byte[] HEART_BEAT = new byte[]{0x12};

	/**
	 * 接收客户端心跳
	 */
	public static final byte[] RETURN_HEART_BEAT = new byte[]{0x11};

	/**
	 * 包头 使用的是字节 默认0xFE
	 */
	public static final byte PACKAGE_HEAD = -2;
	
	/**
	 * 包尾 使用的是字节 默认0x55
	 */
	public static final byte PACKAGE_END = 85;
	
	/**
	 * 包头第一个字符, 使用的是ASCII码，默认F
	 */
	public static final byte HEAD_ONE = 70;
	
	/**
	 * 包头第二个字符, 使用的是ASCII码，默认E
	 */
	public static final byte HEAD_TWO = 69;
	
	/**
	 * 包尾第一个字符，使用的是ASCII码，默认5
	 */
	public static final byte END_ONE = 53;
	
	/**
	 * 包尾第一个字符，使用的是ASCII码，默认5
	 */
	public static final byte END_TWO = 53;
	
	/**
	 * 包长字符/字节长度
	 * <br>
	 * 即包长在数据包所占的长度: 在字节流解码中,若包长字节长度为1,则这个数据包最大长度为255，因为1byte最大表示长度为255
	 * 在字符流解码中, PACKAGE_LENGTH必须为偶数! 若包长字节长度为2，则这个数据包最大长度为255，因为2个字符最大表示长度为FF, 即255
	 */
	public static final int PACKAGE_LENGTH = 4;
	
	/**
	 * 编解码方式: 0.不做任何解析 1.字节流编解码 2. 字符流编解码
	 */
	public static final int DECODE_WAY = 0;

}
