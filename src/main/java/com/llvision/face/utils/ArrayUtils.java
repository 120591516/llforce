package com.llvision.face.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArrayUtils {

	/**
	 * 求两个数组的差集
	 * 
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static String[] substract(String[] arr1, String[] arr2) {
		LinkedList<String> list = new LinkedList<String>();
		for (String str : arr1) {
			if (!list.contains(str)) {
				list.add(str);
			}
		}
		for (String str : arr2) {
			if (list.contains(str)) {
				list.remove(str);
			}
		}
		String[] result = {};
		return list.toArray(result);
	}

	/**
	 * 去除数组中的空值
	 * 
	 * @param strArray
	 * @return
	 */
	public static String[] removeArrayEmptyTextBackNewArray(String[] strArray) {
		List<String> strList = Arrays.asList(strArray);
		List<String> strListNew = new ArrayList<>();
		for (int i = 0; i < strList.size(); i++) {
			if (strList.get(i) != null && !strList.get(i).equals("")) {
				strListNew.add(strList.get(i));
			}
		}
		String[] strNewArray = strListNew.toArray(new String[strListNew.size()]);
		return strNewArray;
	}
}
