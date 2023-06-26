package com.panther.springbootinit.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.springbootinit.model.entity.Picture;

import java.util.List;

public interface PictureService {

    /**
     * 获取图片
     *
     * @param searchText 查询关键字
     * @param pageNum 当前页
     * @param pageSize 返回图片数量
     */
    Page<Picture> searchPicture(String searchText , long pageNum , long pageSize);

}
