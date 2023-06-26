package com.panther.springbootinit.esdao;

import com.panther.springbootinit.model.dto.post.PostEsDTO;
import com.panther.springbootinit.model.dto.post.PostQueryRequest;
import com.panther.springbootinit.model.entity.Post;
import com.panther.springbootinit.service.PostService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 帖子 ES 操作测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
public class PostEsDaoTest {

    @Resource
    private PostEsDao postEsDao;

    @Resource
    private PostService postService;

    @Test
    void test() {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Post> page =
                postService.searchFromEs(postQueryRequest);
        System.out.println(page);
    }

    @Test
    void testSelect() {
        System.out.println(postEsDao.count());
        Page<PostEsDTO> PostPage = postEsDao.findAll(
                PageRequest.of(0, 5, Sort.by("createTime")));
        List<PostEsDTO> postList = PostPage.getContent();
        System.out.println(postList);
    }

    @Test
    void testAdd() {
        PostEsDTO postEsDTO = new PostEsDTO();
        // 不显示填充id会默认给出一个text类型的id
        // 在和数据库同步时会出现问题
        // 后续的查找和删除会受影响
        postEsDTO.setId(2L);
        postEsDTO.setTitle("添狗文章");
        postEsDTO.setContent("找你连麦时你说你在忙工作，每次聊天你都说在忙，你真是一个上进的好女孩，你真好，发现我越来越喜欢这样优秀的你。");
        postEsDTO.setTags(Arrays.asList("添狗", "真爱"));
        postEsDTO.setUserId(1L);
        postEsDTO.setCreateTime(new Date());
        postEsDTO.setUpdateTime(new Date());
        postEsDTO.setIsDelete(0);
        postEsDao.save(postEsDTO);
        System.out.println(postEsDTO.getId());
    }

    @Test
    void testFindById() {
        Optional<PostEsDTO> postEsDTO = postEsDao.findById(1L);
        System.out.println(postEsDTO);
    }

    @Test
    void testCount() {
        System.out.println(postEsDao.count());
    }

    @Test
    void testFindByCategory() {
        List<PostEsDTO> postEsDaoTestList = postEsDao.findByUserId(1L);
        System.out.println(postEsDaoTestList);
    }

    @Test
    void testDeleteById(){
        postEsDao.deleteById(2L);
    }
}
