package com.llvision.face.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class NumberUtils {
	
	/**
	 * 判断当前值是否为版本号 格式：1.2.3
	 * @param value
	 * @return
	 */
	public static boolean isVersion(Object value) {
		if(StringUtils.isEmpty(value)){
			return false;
		}
		String vstr = value.toString();
		Pattern patten = Pattern.compile("^[1-9]*(\\.){1}([1-9]*|0)(\\.){1}([1-9]*|0)$");
		return patten.matcher(vstr).matches();
	}
	/** 
     * 判断当前值是否为整数 
     *  
     * @param value 
     * @return 
     */  
    public static boolean isInteger(Object value) {
        if (StringUtils.isEmpty(value)) {  
            return false;  
        }  
        String mstr = value.toString();  
        Pattern pattern = Pattern.compile("^-?\\d+{1}");  
        return pattern.matcher(mstr).matches();  
    }

	/** 
     * 判断当前值是否为数字(包括小数) 
     *  
     * @param value 
     * @return 
     */  
    public static boolean isDigit(Object value) {  
        if (StringUtils.isEmpty(value)) {  
            return false;  
        }  
        String mstr = value.toString();  
        Pattern pattern = Pattern.compile("^-?[0-9]*.?[0-9]*{1}");  
        return pattern.matcher(mstr).matches();  
    }

	/**
	 * 将数字格式化输出
	 * 
	 * @param value
	 *            需要格式化的值
	 * @param precision
	 *            精度(小数点后的位数)
	 * @return
	 */
	public static String format(Object value, Integer precision) {
		Double number = 0.0;
		if (NumberUtils.isDigit(value)) {
			number = new Double(value.toString());
		}
		precision = (precision == null || precision < 0) ? 2 : precision;
		BigDecimal bigDecimal = new BigDecimal(number);
		return bigDecimal.setScale(precision, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 将数字格式化输出
	 * 
	 * @param value
	 * @return
	 */
	public static String format(Object value) {
		return NumberUtils.format(value, 2);
	}

	/**
	 * 将值转成Integer型，如果不是整数，则返回0
	 * 
	 * @param value
	 * @param replace
	 *            如果为0或者null，替换值
	 * @return
	 */
	public static Integer parseInteger(Object value, Integer replace) {
		if (!NumberUtils.isInteger(value)) {
			return replace;
		}
		return new Integer(value.toString());
	}

	/**
	 * 将值转成Integer型，如果不是整数，则返回0
	 * 
	 * @param value
	 * @return
	 */
	public static Integer parseInteger(Object value) {
		return NumberUtils.parseInteger(value, 0);
	}

	/**
	 * 将值转成Long型
	 * 
	 * @param value
	 * @param replace
	 *            如果为0或者null，替换值
	 * @return
	 */
	public static Long parseLong(Object value, Long replace) {
		if (!NumberUtils.isInteger(value)) {
			return replace;
		}
		return new Long(value.toString());
	}

	/**
	 * 将值转成Long型，如果不是整数，则返回0
	 * 
	 * @param value
	 * @return
	 */
	public static Long parseLong(Object value) {
		return NumberUtils.parseLong(value, 0L);
	}

	/**
	 * 将值转成Double型
	 * 
	 * @param value
	 * @param replace
	 *            replace 如果为0或者null，替换值
	 * @return
	 */
	public static Double parseDouble(Object value, Double replace) {
		if (!NumberUtils.isDigit(value)) {
			return replace;
		}
		return new Double(value.toString());
	}

	/**
	 * 将值转成Double型，如果不是整数，则返回0
	 * 
	 * @param value
	 * @return
	 */
	public static Double parseDouble(Object value) {
		return NumberUtils.parseDouble(value, 0.0);
	}

	/**
	 * 将char型数据转成字节数组
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(char value) {
		byte[] bt = new byte[2];
		for (int i = 0; i < bt.length; i++) {
			bt[i] = (byte) (value >>> (i * 8));
		}
		return bt;
	}

	/**
	 * 将short型数据转成字节数组
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(short value) {
		byte[] bt = new byte[2];
		for (int i = 0; i < bt.length; i++) {
			bt[i] = (byte) (value >>> (i * 8));
		}
		return bt;
	}

	/**
	 * 将int型数据转成字节数组
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(int value) {
		byte[] bt = new byte[4];
		for (int i = 0; i < bt.length; i++) {
			bt[i] = (byte) (value >>> (i * 8));
		}
		return bt;
	}

	/**
	 * 将long型数据转成字节数组
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(long value) {
		byte[] bt = new byte[8];
		for (int i = 0; i < bt.length; i++) {
			bt[i] = (byte) (value >>> (i * 8));
		}
		return bt;
	}

	/**
	 * 将short型数据插入到指定索引的字节数组中
	 * 
	 * @param index
	 *            索引
	 * @param values
	 *            字节数组
	 * @param value
	 *            需要插入的值
	 */
	public static void insert(int index, byte[] values, short value) {
		byte[] bt = NumberUtils.toBytes(value);
		System.arraycopy(bt, 0, values, index, 2);
	}

	/**
	 * 将int型数据插入到指定索引的字节数组中
	 * 
	 * @param index
	 *            索引
	 * @param values
	 *            字节数组
	 * @param value
	 *            需要插入的值
	 */
	public static void insert(int index, byte[] values, int value) {
		byte[] bt = NumberUtils.toBytes(value);
		System.arraycopy(bt, 0, values, index, 4);
	}

	/**
	 * 将long型数据插入到指定索引的字节数组中
	 * 
	 * @param index
	 *            索引
	 * @param values
	 *            字节数组
	 * @param value
	 *            需要插入的值
	 */
	public static void insert(int index, byte[] values, long value) {
		byte[] bt = NumberUtils.toBytes(value);
		System.arraycopy(bt, 0, values, index, 8);
	}

	/**
	 * 将字节转换成整型
	 * 
	 * @param value
	 *            字节类型值
	 * @return
	 */
	public static int byteToInt(byte value) {
		if (value < 0) {
			return value + 256;
		}
		return value;
	}

	/**
	 * 将字节转换成整型
	 *
	 * @param value
	 *            字节类型值
	 * @return
	 */
	public static int getIntSimilarity(Double value) {
		double intSimilarity =0;
		if (value > 0) {
		 intSimilarity =  value.doubleValue ()*100;
		}
		return (int)intSimilarity;
	}


	public static void main(String args[]) {
		int num = ipToInt("192.168.100.203");
		System.out.println(intToIp(num));
		System.out.println(intToIp(23));
	}

	/**
	 * 通过左移位操作（<<）给每一段的数字加权
	 * 第一段的权为2的24次方
	 * 第二段的权为2的16次方
	 * 第三段的权为2的8次方
	 * 最后一段的权为1
	 *
	 * @param ip
	 * @return int
	 */
	public static int ipToInt(String ip) {
		String[] ips = ip.split("\\.");
		return (Integer.parseInt(ips[0]) << 24) + (Integer.parseInt(ips[1]) << 16)
				+ (Integer.parseInt(ips[2]) << 8) + Integer.parseInt(ips[3]);
	}

	/**
	 * 将整数值进行右移位操作（>>）
	 * 右移24位，右移时高位补0，得到的数字即为第一段IP
	 * 右移16位，右移时高位补0，得到的数字即为第二段IP
	 * 右移8位，右移时高位补0，得到的数字即为第三段IP
	 * 最后一段的为第四段IP
	 *
	 * @param i
	 * @return String
	 */
	public static String intToIp(int i) {
		return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
				+ ((i >> 8) & 0xFF) + "." + (i & 0xFF);
	}

}
