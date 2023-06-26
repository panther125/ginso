package com.panther.springbootinit.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.springbootinit.model.dto.post.PostQueryRequest;
import com.panther.springbootinit.model.entity.Post;
import com.panther.springbootinit.model.vo.PostVO;
import com.panther.springbootinit.service.PostService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Gin 琴酒
 * @data 2023/6/18 22:58
 */
@Component
public class PostDataSource implements DataSource<PostVO> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, long current, long size) {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        postQueryRequest.setCurrent(current);
        postQueryRequest.setPageSize(size);
        ServletRequestAttributes servletRequestAttributes =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Page<Post> postPage = postService.searchFromEs(postQueryRequest);
        return postService.getPostVOPage(postPage, request);
    }
}
