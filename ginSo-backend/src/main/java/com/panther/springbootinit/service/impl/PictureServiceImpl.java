package com.panther.springbootinit.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.springbootinit.common.ErrorCode;
import com.panther.springbootinit.exception.BusinessException;
import com.panther.springbootinit.model.entity.Picture;
import com.panther.springbootinit.service.PictureService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Gin 琴酒
 * @data 2023/6/18 16:49
 */
@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public Page<Picture> searchPicture(String searchText, long pageNum, long pageSize) {
        long current = pageNum == 1 ? 1 : (pageNum - 1) * pageSize;
        if(StringUtils.isBlank(searchText)){
            searchText = "zerotwo";
        }
        //https://cn.bing.com/images/search?q=zerotwo&first=8&ensearch=1&form=BESBTB
        String url = String.format("https://cn.bing.com/images/search?q=%s&first=%d&ensearch=1&form=BESBTB",searchText,current );
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常！");
        }
        Elements elements = doc.select(".iuscp.varh.isv");
        if(elements.size() == 0){
            elements = doc.select(".iuscp.isv");
        }
        List<Picture> pictures = new ArrayList<>();
        pictures = elements.stream().map( item ->{
            String m = item.select(".iusc").get(0).attr("m");
            Map<String , String> map = JSONUtil.toBean(m, Map.class);
            Picture picture = new Picture();
            picture.setUrl( map.get("murl") );
            picture.setTitle(item.select(".inflnk").get(0).attr("aria-label"));
            return picture;
        }).limit(pageSize).collect(Collectors.toList()); // 保证pageSize的图片数量
        Page<Picture> picturePage = new Page<>(pageNum ,pageSize);
        picturePage.setRecords(pictures);
        return picturePage;
    }
}
