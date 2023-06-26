package com.panther.springbootinit.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.springbootinit.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索
 *
 * @author Gin 琴酒
 * @data 2023/6/18 18:52
 */
@Data
public class SearchVO implements Serializable {

    private List<UserVO> userVOList;
    private List<PostVO> postVOList;
    private List<Picture> pictureList;

    private List<Object> dataList;

    private static final long serialVersionUID = 1L;

}
