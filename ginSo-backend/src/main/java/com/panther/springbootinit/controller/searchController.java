package com.panther.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.springbootinit.common.BaseResponse;
import com.panther.springbootinit.common.ErrorCode;
import com.panther.springbootinit.common.ResultUtils;
import com.panther.springbootinit.dataSource.DataSource;
import com.panther.springbootinit.dataSource.PictureDataSource;
import com.panther.springbootinit.dataSource.PostDataSource;
import com.panther.springbootinit.dataSource.UserDataSource;
import com.panther.springbootinit.exception.BusinessException;
import com.panther.springbootinit.exception.ThrowUtils;
import com.panther.springbootinit.model.dto.search.searchRequest;
import com.panther.springbootinit.model.enums.SearchTypeEnum;
import com.panther.springbootinit.model.vo.SearchVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gin 琴酒
 * @data 2023/6/18 18:54
 */
@RestController
@RequestMapping("/search")
public class searchController implements Serializable {

    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PostDataSource postDataSource;
    @Resource
    private PictureDataSource pictureDataSource;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody searchRequest searchRequest , HttpServletRequest request){
        String type = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        String searchText = searchRequest.getSearchText();
        if(searchTypeEnum == null) {
            searchTypeEnum = SearchTypeEnum.POST;
            type = searchTypeEnum.getValue();
        }
        ThrowUtils.throwIf(StringUtils.isBlank(searchTypeEnum.getValue()) , new BusinessException(ErrorCode.SYSTEM_ERROR,"查询失败"));
        long current = searchRequest.getCurrent() == 0 ? 1 : searchRequest.getCurrent();
        long size = searchRequest.getPageSize() == 0 ? 10 : searchRequest.getPageSize();

        // 根据Type进行反射调用对应的DataSource(可优化成反射)
        SearchVO searchVO = new SearchVO();
        Map<String , DataSource> typeDataSource = new HashMap<>();
        typeDataSource.put(SearchTypeEnum.POST.getValue() , postDataSource);
        typeDataSource.put(SearchTypeEnum.PICTURE.getValue() , pictureDataSource);
        typeDataSource.put(SearchTypeEnum.USER.getValue() , userDataSource);

        DataSource dataSource = typeDataSource.get(type);
        Page page = dataSource.doSearch(searchText, current, size);
        // 搜索图片
        //Page<Picture> picturePage = pictureDataSource.doSearch(searchText, current, size);
        // 搜索文章
        //Page<PostVO> postVOPage = postDataSource.doSearch(searchText, current, size);
        // 搜索用户
        //Page<UserVO> userVOPage = userDataSource.doSearch(searchText, current, size);

        // 聚合返回
        searchVO.setDataList(page.getRecords());
        return ResultUtils.success(searchVO);
    }
}
