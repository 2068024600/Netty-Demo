package com.smartlab411.utils;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertFactory {

	private static final Logger log = LoggerFactory.getLogger(ConvertFactory.class);

	/**
	 * 16进制字符串转浮点数(4个字节)
	 * 
	 * @param hexString
	 * @return float
	 */
	public static float hexString2Float(String hexString) {
		int intVal = Integer.valueOf(hexString.trim(), 16);
		float floatVal = Float.intBitsToFloat(intVal);
		return floatVal;
	}

	/**
	 * 浮点数转16进制字符串
	 * 
	 * @param floatVal
	 * @return String
	 */
	public static String float2HexString(float floatVal) {
		int intVal = Float.floatToIntBits(floatVal);
		String hexString = Integer.toHexString(intVal).trim().toUpperCase();
		return hexString;
	}

	/**
	 * 
	 * @Title intToByteArray
	 * @Description 整型转字节数组
	 * @param i
	 * @return byte[]
	 */
	public static byte[] intToByteArray(int num, int len) {
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[len - 1 - i] = (byte) ((num >> i * 8) & 0xFF);
		}
		return result;
	}

	/**
	 * 
	 * @Title shortToBytes
	 * @Description 短整型转字节数组
	 * @param n
	 * @return byte[]
	 */
	public static byte[] shortToBytes(short n) {
		byte[] b = new byte[2];
		b[1] = (byte) (n & 0xff);
		b[0] = (byte) ((n >> 8) & 0xff);
		return b;
	}

	/**
	 * 
	 * @Title bytesToShort
	 * @Description 字节转短整型
	 * @param b
	 * @return short
	 */
	public static short bytesToShort(byte[] b) {
		return (short) (b[1] & 0xff | (b[0] & 0xff) << 8);
	}

	/**
	 * 
	 * @Title intToBytes
	 * @Description 整型转字节数组
	 * @param num
	 * @return byte[]
	 */
	public static byte[] intToBytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

	/**
	 * 
	 * @Title bytes2int
	 * @Description 双字节数组转int
	 * @param b
	 * @return int
	 */
	public static int bytes2int(byte[] b) {

		int mask = 0xff;
		int temp = 0;
		int res = 0;
		for (int i = 0; i < 4; i++) {
			res <<= 8;
			temp = b[i] & mask;
			res |= temp;
		}
		return res;
	}

	/**
	 * 
	 * @Title longToBytes
	 * @Description long转字节数组
	 * @param num
	 * @return byte[]
	 */
	public static byte[] longToBytes(long num) {
		byte[] b = new byte[8];
		for (int i = 0; i < 8; i++) {
			b[i] = (byte) (num >>> (56 - i * 8));
		}
		return b;
	}

	/**
	 * 
	 * @Title str2HexStr
	 * @Description 二进制串转16进制
	 * @param str
	 * @return String
	 */
	public static String str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	/**
	 * 
	 * @Title bytesToHexString
	 * @Description 字节数组转16进制串
	 * @param bArray
	 * @return String
	 */
	public static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 
	 * @Title byteToHexString
	 * @Description 字节转16进制串
	 * @param b
	 * @return String
	 */
	public static String byteToHexString(byte b) {
		String sTemp = Integer.toHexString(0xFF & b);
		if (sTemp.length() % 2 != 0) {
			sTemp = "0" + sTemp;
		}
		return sTemp.toUpperCase();
	}

	/**
	 * 
	 * @Title hexStringToBytes
	 * @Description 16进制串转字节数组
	 * @param hexString
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		String chars = "0123456789ABCDEF";
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (chars.indexOf(hexChars[pos]) << 4 | chars.indexOf(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * 
	 * @Title hexToInt
	 * @Description 16进制串转int
	 * @param strHex
	 * @return int
	 */
	public static int hexToInt(String strHex) {
		int nResult = 0;
		if (!isHex(strHex))
			return nResult;
		String str = strHex.toUpperCase();
		if (str.length() > 2) {
			if (str.charAt(0) == '0' && str.charAt(1) == 'X') {
				str = str.substring(2);
			}
		}
		int nLen = str.length();
		for (int i = 0; i < nLen; ++i) {
			char ch = str.charAt(nLen - i - 1);
			try {
				nResult += (getHex(ch) * getPower(16, i));
			} catch (Exception e) {
				log.error("error", e);
			}
		}
		return nResult;
	}

	/**
	 * 计算16进制对应的数值
	 * 
	 * @param ch 十六进制数
	 * @return
	 * @throws Exception
	 */
	private static int getHex(char ch) throws Exception {
		if (ch >= '0' && ch <= '9')
			return (int) (ch - '0');
		if (ch >= 'a' && ch <= 'f')
			return (int) (ch - 'a' + 10);
		if (ch >= 'A' && ch <= 'F')
			return (int) (ch - 'A' + 10);
		throw new Exception("error param");
	}

	/**
	 * 计算幂
	 * 
	 * @param nValue
	 * @param nCount
	 * @return
	 * @throws Exception
	 */
	private static int getPower(int nValue, int nCount) throws Exception {
		if (nCount < 0)
			throw new Exception("nCount can't small than 1!");
		if (nCount == 0)
			return 1;
		int nSum = 1;
		for (int i = 0; i < nCount; ++i) {
			nSum = nSum * nValue;
		}
		return nSum;
	}

	/**
	 * 判断是否是16进制数
	 * 
	 * @param strHex
	 * @return
	 */
	private static boolean isHex(String strHex) {
		int i = 0;
		if (strHex.length() > 2) {
			if (strHex.charAt(0) == '0' && (strHex.charAt(1) == 'X' || strHex.charAt(1) == 'x')) {
				i = 2;
			}
		}
		for (; i < strHex.length(); ++i) {
			char ch = strHex.charAt(i);
			if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f'))
				continue;
			return false;
		}
		return true;
	}

	/**
	 * @Description 获取long的低位上后3个字节，返回16进制的字节数组
	 * @param
	 * @return
	 * @throws
	 */
	public static byte[] longTo3HexByte(long number) {
		byte[] res = new byte[3];
		byte[] temp = ConvertFactory.longToBytes(number);
		for (int i = 5, j = 0; i <= 7; i++, j++) {
			res[j] = temp[i];
		}
		return res;
	}

	/**
	 * 16进制字符串->字节数组
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	/**
	 * 
	 * @Title toByte
	 * @Description 字符转字节
	 * @param c
	 * @return int
	 */
	private static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 将byte转化为2位16进制的字符
	 * 
	 * @param b
	 * @return white
	 */
	public static String toHexString2(byte b) {
		String s = Integer.toHexString(b & 0xFF);
		if (s.length() == 1) {
			return "0" + s;
		} else {
			return s;
		}
	}

	/**
	 * 
	 * @Title addBytes
	 * @Description 合并字节数组
	 * @param data1
	 * @param data2
	 * @return byte[]
	 */
	public static byte[] addBytes(byte[] data1, byte[] data2) {
		byte[] data3 = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, data3, 0, data1.length);
		System.arraycopy(data2, 0, data3, data1.length, data2.length);
		return data3;
	}

	/**
	 * 
	 * @Title hexadecimal16Conversion
	 * @Description 判断十六进制数正负
	 * @param hexadecimalStr
	 * @return int
	 */
	public static Integer hexadecimal16Conversion(String hexadecimalStr) {
		Integer getDataDecimal = 0;// 转化得到的目标数据
		// 16进制代表数据 4位数字
		if (hexadecimalStr.length() == 4) {
			int bit1Num = Integer.parseInt(hexadecimalStr.substring(0, 1), 16);// 获取第一位。判断是正数还是负数
			if (bit1Num < 8) { // 小于8是正数
				getDataDecimal = Integer.parseInt(hexadecimalStr, 16);
			} else { // 负数
				hexadecimalStr = "FFFF" + hexadecimalStr; // 先补全八位
				getDataDecimal = new BigInteger(hexadecimalStr, 16).intValue();
			}
			return getDataDecimal;
		}
		return 0;
	}

}
