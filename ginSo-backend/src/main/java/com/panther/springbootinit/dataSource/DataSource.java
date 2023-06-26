package com.panther.springbootinit.dataSource;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 数据源接口
 *
 * @author Gin 琴酒
 * @data 2023/6/18 22:53
 */
public interface DataSource<T> {
    /**
     * 统一规范数据源
     *
     * @param searchText 搜索关键字
     * @param current 当前页
     * @param size 返回数据大小 一般限制20条
     * @return 返回分页数据
     */
    Page<T> doSearch(String searchText , long current , long size);
}
