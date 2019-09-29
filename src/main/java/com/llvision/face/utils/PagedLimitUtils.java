package com.llvision.face.utils;

public class PagedLimitUtils {


    /**
     * 获取数据库limit 查询开始数字
     * @param pageID
     * @param pageSize
     * @return
     */
    public static int getPagedIndex(int pageID, int pageSize) {
        int index = (pageID - 1) * pageSize;
        if (index < 0) index = 0;
        return index;
    }

    /**
     * 获取数据库limit 查询数量
     * @param pageID
     * @param pageSize
     * @return
     */
    public static int getPagedLimit(int pageID, int pageSize) {
        int limit = pageSize;
        if (limit <= 0) limit = 0;
        return limit;
    }
    public static int getPagedEnd(int pageID, int pageSize) {
    	int end = pageID * pageSize;
        if (end <= 0) end = 0;
        return end;
    }
}