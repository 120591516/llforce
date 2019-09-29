package com.llvision.face.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 * @author wangag
 * @ClassName: PagedModelList
 * @Description: TODO 定义分页列表类
 */
public class PagedModelList<T> {
    private int pageId; //   当前页号
    private int pageSize; // 每页显示的行数
    private int pageCount;  //分页数量

    private int totalCount; //最高数量

    private List<T> pagedModelList;

    public PagedModelList(int totalCount) {
        this.pageId = 1;
        this.pageSize = 0;
        this.totalCount = totalCount;

        this.pageCount = 1;

        // calPageCount();

        pagedModelList = new ArrayList<T> ();
    }

    public PagedModelList(int pageID, int pageSize, int totalCount) {
        this.pageId = pageID > 0 ? pageID : 1;
        this.pageSize = pageSize;
        this.totalCount = totalCount;

        calPageCount ();

        pagedModelList = new ArrayList<T> ();
    }

    public int getPageCount() {
        return pageCount;
    }

    public List<T> getPagedModelList() {
        return pagedModelList;
    }

    public Object[] getPagedModels() {
        return pagedModelList.toArray (new Object[this.pagedModelList.size ()]);
    }

    public void addModel(T model) {
        this.pagedModelList.add (model);
    }

    public void addModels(List<T> modelList) {
        if (modelList != null) {
            int n = modelList.size ();
            for (int i = 0; i < n; i++) {
                this.pagedModelList.add (modelList.get (i));
            }
        }
        // this.pagedModelList.addAll(modelList);
    }

    public int getPageId() {
        return pageId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    protected void calPageCount() {
        if (this.pageSize <= 0)
            this.pageCount = 1;
        else {
            int pn = this.totalCount / this.pageSize;

            if (this.totalCount % this.pageSize != 0)
                pn++;
            if (pn == 0)
                pn++;

            this.pageCount = pn;
        }

    }

    public int[] getPages() {
        int count = totalCount / pageSize;
        if (totalCount % pageSize > 0)
            count++;

        int[] pages = new int[count];
        for (int i = 0; i < count; i++)
            pages[i] = i;

        return pages;
    }

    public boolean hasnext() {
        if (pageCount > pageId) {
            return true;
        }
        return false;
    }

    public boolean hasback() {
        if (pageId > 1) {
            return true;
        }
        return false;
    }

}