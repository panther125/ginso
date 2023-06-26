package com.panther.springbootinit.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.springbootinit.model.entity.Picture;
import com.panther.springbootinit.service.PictureService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Gin 琴酒
 * @data 2023/6/18 22:58
 */
@Component
public class PictureDataSource implements DataSource<Picture>{

    @Resource
    private PictureService pictureService;

    @Override
    public Page<Picture> doSearch(String searchText, long current, long size) {
        return pictureService.searchPicture(searchText,current,size);
    }
}
