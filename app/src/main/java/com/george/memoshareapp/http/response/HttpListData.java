package com.george.memoshareapp.http.response;

import java.util.List;

public class HttpListData<T>  {
    /** 当前页码 */
    private int pageIndex;
    /** 页大小 */
    private int pageSize;
    /** 总数量 */
    private int totalNumber;
    private Boolean lastPage;
    /** 数据 */
    private List<T> items;
    /** 是否最后一页 */

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        lastPage = lastPage;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<T> getItems() {
        return items;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
