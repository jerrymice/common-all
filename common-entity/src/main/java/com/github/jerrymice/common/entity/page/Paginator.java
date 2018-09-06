package com.github.jerrymice.common.entity.page;

import com.github.jerrymice.common.entity.annotation.ApiField;
import com.google.gson.annotations.Expose;


import java.util.List;

/**
 * 分页的基类
 *
 * @author zhoudengwang
 */
public class Paginator<E> {
    /**
     * 上一页
     */
    @ApiField(comment = "上一页", jdbcType = "int", length = 8)
    @Expose()
    private int prePage;

    /**
     * 当前页
     */
    @ApiField(comment = "当前页", jdbcType = "int", length = 8)
    @Expose()
    private int curPage;

    /**
     * 下一页
     */
    @ApiField(comment = "下一页", jdbcType = "int", length = 8)
    @Expose()
    private int nextPage;

    /**
     * 总页数
     */
    @ApiField(comment = "总页数", jdbcType = "int", length = 8)
    @Expose()
    private int totalPages;

    /**
     * 总条数
     */
    @ApiField(comment = "总条数", jdbcType = "int", length = 8)
    @Expose()
    private int totalCount;

    /**
     * 每页显示记录数
     */
    @ApiField(comment = "每页显示记录数", jdbcType = "int", length = 8)
    @Expose()
    private int pageSize = 10;

    /**
     * 查询起始位置
     */
    @ApiField(comment = "查询其实位置", jdbcType = "int", length = 8)
    @Expose()
    private int start;

    /**
     * 存放查询的数据
     */
    @ApiField(comment = "存放查询的数据", jdbcType = "array")
    @Expose()
    private List<E> datas;

    public Paginator() {
    }

    /**
     * 构造方法
     *
     * @param curPage    当前页
     * @param totalCount 总记录数
     * @param pageSize   每页多少条数据
     */
    public Paginator(int curPage, int totalCount, int pageSize) {
        super();
        this.curPage = curPage;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
    }

    /**
     * 构造方法
     *
     * @param curPage    当前页
     * @param totalCount 总记录数
     */
    public Paginator(int curPage, int totalCount) {
        super();
        this.curPage = curPage;
        this.totalCount = totalCount;
    }

    /**
     * 构造方法
     *
     * @param curPage 当前页
     */
    public Paginator(int curPage) {
        super();
        this.curPage = curPage;
    }

    /**
     * @return 获取每页的记录条数
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize 设置每页的记录条数
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return 获取上一页编号
     */
    public int getPrePage() {
        if (this.getCurPage() - 1 <= 0) {
            return 1;
        }
        return this.getCurPage() - 1;
    }

    /**
     * @param prePage 设置上一页编号
     */
    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    /**
     * @return 获取当前页编号
     */
    public int getCurPage() {
        if (this.curPage > this.getTotalPages()) {
            //this.curPage = this.getTotalPages();
        }
        if (this.curPage < 1) {
            this.curPage = 1;
        }
        return curPage;
    }

    /**
     * @param curPage 设置当页编号
     */
    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    /**
     * @return 获取下一页编号
     */
    public int getNextPage() {
        if (this.getCurPage() + 1 >= this.getTotalPages()) {
            return this.getTotalPages();
        } else {
            return this.getCurPage() + 1;
        }
    }

    /**
     * @param nextPage 设置下一页编号
     */
    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    /**
     * @return 获取分页总页数
     */
    public int getTotalPages() {
        if (this.getTotalCount() % pageSize == 0) {
            return this.getTotalCount() / pageSize;
        } else {
            return this.getTotalCount() / pageSize + 1;
        }
    }

    /**
     * @param totalPages 设置分页总页数
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return 获取总记录数
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount 设置总记录数
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return 获取当前页的数据集
     */
    public List<E> getDatas() {
        return datas;
    }

    /**
     * @param datas 设置当前面的数据集
     */
    public void setDatas(List<E> datas) {
        this.datas = datas;
    }

    /**
     * @return 获取开始页的offset
     */
    public int getStart() {
        this.totalPages = this.getTotalPages();
//		return (this.getCurPage() - 1) * pageSize + 1;
        return (this.getCurPage() - 1) * pageSize;
    }

    /**
     *
     * @param start 设置开始页的offset
     */
    public void setStart(int start) {
        this.start = start;
    }
}
