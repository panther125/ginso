package com.panther.springbootinit;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.panther.springbootinit.model.entity.Picture;
import com.panther.springbootinit.model.entity.Post;
import com.panther.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Gin 琴酒
 * @data 2023/6/17 23:44
 */
//@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;

    @Test
    void FetchPicture() throws IOException {
        String url = "https://cn.bing.com/images/search?q=zerotwo&first=1&ensearch=1&form=BESBTB";
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".iuscp.varh.isv");
        List<Picture> pictures = new ArrayList<>();
        //for(Element element : elements){
        //    // 获取图片地址 m 属性的 murl
        //    String m = element.select(".iusc").get(0).attr("m");
        //    Map<String , String> map = JSONUtil.toBean(m, Map.class);
        //    String murl = map.get("murl");
        //    // 获取标题 inflnk
        //    String title =  element.select(".inflnk").get(0).attr("aria-label");
        //
        //}
        pictures = elements.stream().map( item ->{
            String m = item.select(".iusc").get(0).attr("m");
            Map<String , String> map = JSONUtil.toBean(m, Map.class);
            Picture picture = new Picture();
            picture.setUrl( map.get("murl") );
            picture.setTitle(item.select(".inflnk").get(0).attr("aria-label"));
            return picture;
        } ).collect(Collectors.toList());
    }

    @Test
    void POSTFetchPassage(){
        // 获取JSON数据
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
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
        Assertions.assertTrue(b);
    }

}
