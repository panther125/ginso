package com.panther.springbootinit.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.panther.springbootinit.model.entity.Post;
import com.panther.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取初始帖子列表
 * 每次刷新都会执行 run方法
 */
//@Component
@Slf4j
public class getFetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
        // 获取JSON数据
        String json = "{\"current\":2,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest
                .post(url)  // 接口地址
                .body(json) // 传输参数
                .execute()  // 执行
                .body();    // 获取返回体
        // JSON转对象
        Map<String , Object> map = JSONUtil.toBean(result , Map.class);
        if( (int)map.get("code") != 0){
            // 处理爬取失败
        }
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> PostList = new ArrayList<>();
        PostList = records.stream().map(item ->{
            Post post = new Post();
            post.setTitle( ((JSONObject)item).get("title").toString() );
            JSONArray tags = (JSONArray)((JSONObject) item).get("tags");
            post.setTags( tags.toList(String.class).toString() );
            post.setUserId(1669987936141877249L);
            post.setContent( ((JSONObject)item).get("content").toString() );
            return post;
        }).collect(Collectors.toList());
        boolean b = postService.saveBatch(PostList);
        if(b){
            log.info("初始化帖子列表成功，获取条数{}" , PostList.size());
        } else {
            log.info("爬取失败！！！");
        }
    }
}
