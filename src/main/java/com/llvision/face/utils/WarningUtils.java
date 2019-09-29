package com.llvision.face.utils;

import com.alibaba.fastjson.JSON;
import com.llvision.face.constants.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 安防中涉及的警示信息"warning"的处理
 *
 * @author shihy
 */
public class WarningUtils {

    /**
     * 人员警示
     * 通过前端传过来的警示信息代号数组拼成数据库警示字符串 16为 例如“0101000000000000”
     *
     * @param values
     * @author shihy
     */
    public static String getPersonWarningStr(Integer[] values) {
        StringBuffer str = new StringBuffer ("0000000000000000");
        for (int val : values) {
            switch (val) {
                case Constants.PERSON_WARNING_ZTRY:
                    str.setCharAt (0, '1');
                    break;
                case Constants.PERSON_WARNING_ZDRY:
                    str.setCharAt (1, '1');
                    break;
                case Constants.PERSON_WARNING_XDRY:
                    str.setCharAt (2, '1');
                    break;
                case Constants.PERSON_WARNING_KBFZ:
                    str.setCharAt (3, '1');
                    break;
                case Constants.PERSON_WARNING_SDRY:
                    str.setCharAt (4, '1');
                    break;
                case Constants.PERSON_WARNING_SZPRY:
                    str.setCharAt (5, '1');
                    break;
                case Constants.PERSON_WARNING_XSFZ:
                    str.setCharAt (6, '1');
                    break;
                case Constants.PERSON_WARNING_SWRY:
                    str.setCharAt (7, '1');
                    break;
			case Constants.PERSON_WARNING_SKRY:
				str.setCharAt(8, '1');
				break;
			case Constants.PERSON_WARNING_ZDSFRY:
				str.setCharAt(9, '1');
				break;
			case Constants.PERSON_WARNING_ZSZHJSBR:
				str.setCharAt(10, '1');
				break;
			case Constants.PERSON_WARNING_LY_XF:
				str.setCharAt(11, '1');
			case Constants.PERSON_WARNING_LY_JNZD:
				str.setCharAt(12, '1');
			case Constants.PERSON_WARNING_LY_WFQK:
				str.setCharAt(13, '1');
			case Constants.PERSON_WARNING_LY_WWB:
				str.setCharAt(14, '1');
			case Constants.PERSON_WARNING_LY_BKJL:
				str.setCharAt(15, '1');
                default:
                    break;
            }
        }
        return str.toString ();
    }


    /**
     * 车辆警示
     * 通过前端传过来的警示信息代号数组拼成数据库警示字符串 16为 例如“0101000000000000”
     *
     * @param values
     * @author shihy
     */
    public static String getCarWarningStr(Integer[] values) {
        StringBuffer str = new StringBuffer ("0000000000000000");
        for (int val : values) {
            switch (val) {
                case Constants.CAR_WARNING_TP:
                    str.setCharAt (0, '1');
                    break;
                case Constants.CAR_WARNING_WZ:
                    str.setCharAt (1, '1');
                    break;
                case Constants.CAR_WARNING_DQCL:
                    str.setCharAt (2, '1');
                    break;
                case Constants.CAR_WARNING_XCCF:
                    str.setCharAt (3, '1');
                    break;
                case Constants.CAR_WARNING_FXCCF:
                    str.setCharAt (4, '1');
                    break;
				case Constants.CAR_WARNING_JYXXGQ:
					str.setCharAt(5, '1');
				case Constants.CAR_WARNING_BKJL:
					str.setCharAt(7, '1');
				
				break;
                default:
                    break;
            }
        }
        return str.toString ();
    }

    /**
     * 通过数据库警示串数据获得有那些警示信息
     *
     * @param warningStr
     * @author shihy
     */
    public static List<Integer> getWarningList(String warningStr) {
        List<Integer> list = new ArrayList<Integer> ();
        if (warningStr != null) {
            for (int i = 0; i <= warningStr.length () - 1; i++) {
                if (warningStr.substring (i, i + 1).equals ("1")) {
                    list.add (i + 1);
                }
            }
            return list;
        }
		return list;
    }
    
	/**
	 * 车辆警示 通过前端传过来的警示信息代号数组拼成数据库警示字符串 16为 例如“0101000000000000”
	 *
	 * @param values
	 * @author shihy
	 */
	public static String getGDCarWarningStr(Integer[] values) {
		StringBuffer str = new StringBuffer("0000000000000000");
		for (int val : values) {
			str.setCharAt(val - 1, '1');
		}
		return str.toString();
	}

	/**
	 * 通过数据库警示字符串数据获得有那些警示信息
	 *
	 * @param warningStr
	 * @author shihy
	 */


    public static List<String> getStringWarningList(String warningStr) {
        List<String> list = new ArrayList<String> ();
        if (warningStr != null) {
            // 12-18 为洛阳市局专属警示 与上边有重复可忽略
            for (int i = 0; i <= warningStr.length () - 1; i++) {
                if (warningStr.substring (i, i + 1).equals ("1")) {
                    if(i+1==1){
                        list.add ("在逃人员");
                    }else if(i+1==2){
                        list.add ("重点人员");
                    }else if(i+1==3){
                        list.add ("吸毒人员");
                    }else if(i+1==4){
                        list.add ("恐暴分子");
                    }else if(i+1==5){
                        list.add ("涉毒人员");
                    }else if(i+1==6){
                        list.add ("涉诈骗人员");
                    }else if(i+1==7){
                        list.add ("重点刑事犯罪前科人员");
                    }else if(i+1==8){
                        list.add ("涉稳人员");
                    }else if(i+1==9){
                        list.add ("涉恐人员");
                    }else if(i+1==10){
                        list.add ("重点上访人员");
                    }else if(i+1==11){
                        list.add ("肇事肇祸精神病人");
                    }else if(i+1==12){
                        list.add ("信访人员");
                    }else if(i+1==13){
                        list.add ("局内重点人员");
                    }else if(i+1==14){
                        list.add ("违法前科人员");
                    }else if(i+1==15){
                        list.add ("维稳办人员");
                    }else if(i+1==16){
                        list.add ("布控精灵");
                    }
                }
            }
            return list;
        }
        return null;
    }

    /**
     * 通过数据库警示字符串数据获得有那些警示信息
     *
     * @param warningStr
     * @author shihy
     */


    public static List<String> getStringCarWarningList(String warningStr) {
        List<String> list = new ArrayList<String> ();
        if (warningStr != null) {
            for (int i = 0; i <= warningStr.length () - 1; i++) {
                if (warningStr.substring (i, i + 1).equals ("1")) {
                    if(i+1==1){
                        list.add ("套牌");
                    }else if(i+1==2){
                        list.add ("违章");
                    }else if(i+1==3){
                        list.add ("盗抢车辆");
                    }else if(i+1==4){
                        list.add ("现场处罚");
                    }else if(i+1==5){
                        list.add ("非现场处罚");
                    }else if(i+1==6){
                        list.add ("检验信息过期");
                    }else if(i+1==7){
                        list.add ("布控精灵");
                    }
                }
            }
            return list;
        }
        return null;
    }

	/**
	 * 车辆警示 通过前端传过来的警示信息代号数组拼成数据库警示字符串 16为 例如“0101000000000000”
	 *
	 * @param values
	 * @author shihy
	 */
	public static String getGDCarWarningStr(String[] values) {
		StringBuffer str = new StringBuffer("0000000000000000");
		for (String val : values) {
			switch (val) {
			case Constants.GD_CAR_WARNING_STRING_DZWF:
				str.setCharAt(0, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_DZWF2:
				str.setCharAt(1, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_WNJ:
				str.setCharAt(2, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_WBF:
				str.setCharAt(3, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_YCZT:
				str.setCharAt(4, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_YZF:
				str.setCharAt(5, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_YZF2:
				str.setCharAt(7, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_ZDWF3:
				str.setCharAt(8, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_JCP:
				str.setCharAt(9, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_TPC:
				str.setCharAt(10, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_JTPC:
				str.setCharAt(11, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_HBC:
				str.setCharAt(12, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_ZDXYCL1:
				str.setCharAt(13, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_ZDXYCL:
				str.setCharAt(14, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_SJRY:
				str.setCharAt(15, '1');
				break;
			case Constants.GD_CAR_WARNING_STRING_YQWNJ:
				str.setCharAt(16, '1');
				break;
			default:
				break;
			}
		}
		return str.toString();
	}
	/**
	 * 广东定制的车辆警示信息
	 * 通过数据库警示字符串数据获得有那些警示信息
	 *
	 * @author shihy
	 */

	public static List<String> getGDCarWarningList() {
		List<String> list = new ArrayList<String>();
		list.add("多宗违法1");
		list.add("多宗违法2");
		list.add("未年检");
		list.add("未报废");
		list.add("异常状态");
		list.add("营转非1");
		list.add("营转非2");
		list.add("多宗违法3");
		list.add("假牌车");
		list.add("套牌车");
		list.add("假套牌车");
		list.add("黄标车");
		list.add("重点嫌疑车辆1");
		list.add("重点嫌疑车辆");
		list.add("失驾人员");
		list.add("逾期未年检");
		return list;
	}
    /**
     * 判断是否警示
     *
     * @param warningStr
     * @author shihy
     */
    public static boolean judgeWarning(String warningStr) {
        boolean b = false;
        List<Integer> list = new ArrayList<Integer> ();
        for (int i = 0; i <= warningStr.length () - 1; i++) {
            if (warningStr.substring (i, i + 1).equals ("1")) {
                b = true;
                break;
            }
        }
        return b;
    }

    public static void main(String[] args) {
		Integer[] ints = { 9, 3 };
        String value = getPersonWarningStr (ints);
        List<String> value1 = getStringWarningList("1111111111111111");
        System.out.println (value);
        System.out.println (value1);

        List<Integer> list = getWarningList ("1111111111111111");
        for (Integer v : list) {
            System.out.print (v + " ");
        }
		System.out.println();
		List<Object> recordList = new ArrayList<>();
		Map<String, Object> data = null;
		List<String> carWarning = WarningUtils.getGDCarWarningList();
		for (int i = 0; i < carWarning.size(); i++) {
			data = new HashMap<>();
			data.put("id", i + 1);
			data.put("content", carWarning.get(i));
			recordList.add(data);
		}
		System.out.println(JSON.toJSONString(recordList));

		String warningStr = "多宗违法1,多宗违法2,异常状态";
		String[] split = warningStr.split(",");
		String gdCarWarningStr = getGDCarWarningStr(split);
		System.out.println(gdCarWarningStr);

    }

}
