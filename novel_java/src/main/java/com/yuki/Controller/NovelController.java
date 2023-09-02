package com.yuki.Controller;
import com.alibaba.fastjson.JSONArray;
import com.yuki.Utils.HtmlExtractUtils;
import com.yuki.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.yuki.Utils.OkHttpUtil;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;

@CrossOrigin(origins="*")
@RestController
public class NovelController {

    /**
     *
     * @param search_text // 关键字
     * @return
     */
    @PostMapping("/search")
    public ResponseResult searchText(
            @RequestParam("search_text") String search_text
    ){
        String htmltext = "";
        try {
            htmltext = OkHttpUtil.get("http://chii.in/subject_search/"+"\""+search_text+"\""+"?cat=1");
        }catch (Exception e){
            return ResponseResult.fail("搜索失败");
        }
        JSONArray jsonArray = HtmlExtractUtils.GetTrueString(htmltext);
        return ResponseResult.success(jsonArray);
    }


}
