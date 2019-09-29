package com.llvision.face.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class DateUtils {
	private static final Logger log = LoggerFactory.getLogger("");
	/**
	 * 将日期格式转换成yyyy-MM-dd的字符串格式 返回值如：2010-10-06
	 * 
	 * @param time
	 *            要转换的日期
	 * @return
	 */
	public static String dateToString(Date time) {
		if(time==null){
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // 定义将日期格式要换成的格式
		String stringTime = formatter.format(time);
		return stringTime;

	}

	/**
	 * 将日期格式转换成yyyy-MM-dd的字符串格式 返回值如：2010-10-06
	 * 
	 * @param time
	 *            要转换的日期
	 * @return
	 */
	public static String dateToDateStr(Date time) {
		if (time == null) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd"); // 定义将日期格式要换成的格式
		String stringTime = formatter.format(time);
		return stringTime;

	}

	/**
	 * 将日期格式转换成HH:mm:ss的字符串格式 返回值如：10:08:56
	 * 
	 * @param time
	 *            要转换的日期
	 * @return
	 */
	public static String dateToTimeStr(Date time) {
		if (time == null) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss"); // 定义将日期格式要换成的格式
		String stringTime = formatter.format(time);
		return stringTime;

	}

	public static String parseDate(Date time) {
		if (time == null) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss"); // 定义将日期格式要换成的格式
		String stringTime = formatter.format(time);
		return stringTime;

	}

	/**
	 * 将日期格式转换成yyyy-MM-dd HH:mm:ss的字符串格式 返回值如：2010-10-06 10:00:00
	 * 
	 * @param time
	 *            要转换的日期
	 * @return
	 */
	public static String dateTimeToString(Date time) {
		if(time==null){
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义将日期格式要换成的格式
		String stringTime = formatter.format(time);
		return stringTime;

	}

	/**
	 * 将yyyy-MM-dd的字符串格式转化为日期Date
	 *
	 * @param stringTime
	 *            要转换的日期 格式如2010-10-06
	 * @return
	 */
	public static Date stringToDate(String stringTime) throws ParseException {
		if(stringTime==null){
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // 定义将日期格式要换成的格式
		Date dateTime = formatter.parse( stringTime );
		return dateTime;

	}

	public static boolean validDate(String stringTime) {
		String path="\\d{4}-\\d{1,2}-\\d{1,2}";//定义匹配规则
		Pattern p=Pattern.compile(path);//实例化Pattern
		return p.matcher(stringTime).matches();//验证字符串内容是否合法
	}

	/**
	 * 处理字符串 转化时间字符串
	 *
	 * @param stringDateTime
	 *            要转换的日期
	 * @return
	 */
	public static String dateTimeToString( String stringDateTime) {
		if(stringDateTime!=null && !stringDateTime.equals ("")) {
			Integer intDateTime = Integer.parseInt (stringDateTime.trim ());
			Date dateTime = new Date (intDateTime);
			return dateToString (dateTime);
		}
		return null;
	}

	public static String dateStrToString(String dateStr) {
		if(StringUtils.isEmpty(dateStr)){
			return null;
		}
		if(dateStr.length()<14){
			for (int i = dateStr.length(); i < 15; i++) {
				dateStr+="0";
			}
		}
		SimpleDateFormat parse = new SimpleDateFormat("yyyyMMddHHmmss"); // 定义将日期格式要换成的格式
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义将日期格式要换成的格式
		String stringTime="";
		try {
			stringTime = formatter.format(parse.parse(dateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return stringTime;

	}


	/**
	 * 获取7天内的时间
	 *
	 * @return
	 */
	public static String dateTimeToString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		//过去七天
		c.setTime(new Date());
		c.add(Calendar.DATE, - 7);
		Date d = c.getTime();
		String day = format.format(d);
		System.out.println("过去七天："+day);
		return day;
	}

	/**
	 * 得到当前时间，以字符串表示
	 * 
	 * @return
	 */
	public static String getDate() {
		Date date = new Date();
		return DateUtils.dateToString(date);
	}

	public static String getDateTime() {
		Date date = new Date();
		return DateUtils.dateTimeToString(date);
	}

	/**
	 * 获取过去第几天的日期
	 *
	 * @param past
	 * @return
	 */
	public static String getPastDate(int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(today);
		return result;
	}



	/**
	 * 时间毫秒转化成时分秒 00:00:00
	 *
	 * @param time
	 * @return
	 */
	public static String secToDateTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		int millisecond = 0;
		if (time <= 0)
			return "00:00:00";
		else {
			second = time / 1000;
			minute = second / 60;
			millisecond = time % 1000;
			if (second < 60) {
				timeStr = "00:00:" + unitFormat(second);
			} else if (minute < 60) {
				second = second % 60;
				timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
			} else {// 数字>=3600 000的时候
				hour = minute / 60;
				minute = minute % 60;
				second = second - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}
	public static String unitFormat(int i) {//时分秒的格式转换
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	public static String unitFormat2(int i) {//毫秒的格式转换
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "00" + Integer.toString(i);
		else if (i >=10 && i < 100) {
			retStr = "0" + Integer.toString(i);
		}
		else
			retStr = "" + i;
		return retStr;
	}

	/**
	 * 时间分钟转化成时分 00时00分
	 *
	 * @param time
	 * @return
	 */
	public static String minuteToDateTime(Long time) {
		String dateStr = "";
		int hours = (int) Math.floor(time / 60);
		long minute = time % 60;
		if (minute < 10) {
			dateStr = hours + "时0" + minute + "分";
		} else {
			dateStr = hours + "时" + minute + "分";
		}
		return dateStr;
	}

	/**
	 * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 * 
	 * @param nowTime
	 *            当前时间
	 * @return
	 * @author jqlin
	 */
	public static boolean isEffectiveDate(Date nowTime) {
		try {
			log.info("开始验证时间区间");
			String format = "yyyy-MM-dd";
			log.info("当前时间为：" + new SimpleDateFormat(format).format(nowTime));
			Date startTime = new SimpleDateFormat(format).parse("2019-01-16");
			Date endTime = new SimpleDateFormat(format).parse("2019-01-31");
			if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
				return true;
			}

			Calendar date = Calendar.getInstance();
			date.setTime(nowTime);

			Calendar begin = Calendar.getInstance();
			begin.setTime(startTime);

			Calendar end = Calendar.getInstance();
			end.setTime(endTime);

			if (date.after(begin) && date.before(end)) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
		}
		return false;
	}

	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	public static void main(String[] args) {
//		Date date = new Date();
//		String time = DateUtils.dateTimeToString(date);
//		System.out.println(DateUtils.getDate());
//		System.out.println(DateUtils.getDateTime());

//		MD5Code mD5Code = new MD5Code();
//		String passwordMd5 = mD5Code.getMD5ofStr("123456");
//		System.out.println ("密码： "+passwordMd5);

//		System.out.println(getPastDate(7));
//		System.out.println(getPastDate(0));
//		System.out.println("请输入毫秒："+20000);
//		Scanner input=new Scanner(System.in);
//		String s=input.next();
//		int time_ms=Integer.parseInt(s);

		System.out.println(isEffectiveDate(new Date()));

		System.out.println(validDate("2018-02-02"));
		System.out.println(validDate("2018-2-02"));
		System.out.println(validDate("2018-02-2"));
		System.out.println(validDate("2018-2-2"));
		System.out.println(validDate("20148-2-2"));
		System.out.println(validDate("2018-02-33"));

	}

}
