package com.panther.springbootinit.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.springbootinit.model.dto.user.UserQueryRequest;
import com.panther.springbootinit.model.vo.UserVO;
import com.panther.springbootinit.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Gin 琴酒
 * @data 2023/6/18 22:58
 */
@Component
public class UserDataSource implements DataSource<UserVO> {
    @Resource
    private UserService userService;
    @Override
    public Page<UserVO> doSearch(String searchText, long current, long size) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(current);
        userQueryRequest.setPageSize(size);
        return userService.listUserVoByPage(userQueryRequest);
    }
}
