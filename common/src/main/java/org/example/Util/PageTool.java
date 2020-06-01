package org.example.Util;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: houlintao
 * @Date:2020/6/1 下午5:42
 * @email 437547058@qq.com
 * @Version 1.0
 * 查询分页工具类，内部封装的是每一次查询的分页信息，这个类的对象是被使用在当从数据库查询
 * 出结果的时候（其实就是在controller中返回前端之前）。
 */
public class PageTool implements Serializable {
    public static final long serialVersionUID=1L;

    //一个查询的总记录数
    private int totalCount;
    //每页记录数
    private int pageSize;
    //总页数
    private int totalPage;
    //当前页数
    private int currPage;
    //查询结果的列表数据，例如queryList后返回的List<SysUserEnriry>
    private List<?> list;

    /**
     *@date: 2020/6/1 下午5:57
     *@param list 每个查询返回的实体列表
     *@param totalCount list中总共包含的entity有几个
     *@param pageSize 每页可以容纳多少个
     *@param currPage 当前是第几页
     *@return:
     *@Description:
     */
    public PageTool(List<?> list,int totalCount,int pageSize,int currPage){
        this.list=list;
        this.totalCount=totalCount;
        this.pageSize=pageSize;
        this.currPage=currPage;
        this.totalPage = (int) Math.ceil(totalCount/pageSize);
    }

    public PageTool(PageInfo pageInfo){
        this.list = pageInfo.getList();
        this.totalCount = (int) pageInfo.getTotal();
        this.pageSize = pageInfo.getPageSize();
        this.currPage = pageInfo.getPageNum();
        this.totalPage = pageInfo.getPages();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public List<?> getList() {
        return list;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
