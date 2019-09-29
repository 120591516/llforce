package com.llvision.face.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页工具
 * @Author th
 * @Date 2018年07月09日 下午5:26
 */
public final class PageUtil {
	
	/**
	 * 获分页信息
	 * @param size
	 * @param index
	 * @return
	 */
    public static Map<String,Integer> getPageInfo(String size,String index){
    	Map<String,Integer> result = new HashMap<>();
    	if(size != null && index != null){
    		int pageSize = Integer.valueOf(size);
    		int pageIndex = Integer.valueOf(index);
    		if(pageSize < 1){
    			pageSize = 10;
    		}else if(pageSize > 2000){
    			pageSize = 2000;
    		}
    		if(pageIndex < 1){
    			pageIndex = 1;
    		}
    		result.put("pageSize", pageSize);
    		result.put("pageIndex", pageIndex);
    	}
		return result;
    }
}
