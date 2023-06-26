package com.panther.springbootinit.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.springbootinit.common.BaseResponse;
import com.panther.springbootinit.common.ErrorCode;
import com.panther.springbootinit.common.ResultUtils;
import com.panther.springbootinit.exception.ThrowUtils;
import com.panther.springbootinit.model.dto.picture.PictureQueryRequest;
import com.panther.springbootinit.model.dto.post.PostQueryRequest;
import com.panther.springbootinit.model.entity.Picture;
import com.panther.springbootinit.model.entity.Post;
import com.panther.springbootinit.model.vo.PostVO;
import com.panther.springbootinit.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取图片接口
 *
 * @author Gin 琴酒
 * @data 2023/6/18 16:39
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPostVOByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                       HttpServletRequest request) {
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, size);

        return ResultUtils.success(picturePage);
    }

}
