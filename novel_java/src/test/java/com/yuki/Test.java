package com.yuki;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static com.yuki.Utils.HtmlExtractUtils.getUrlBase64Image;

@RunWith(SpringRunner.class)//以Spring启动类的方式运行测试类
@SpringBootTest(classes= NovelApplication.class)//测试启动类入口，这里填写自己的
public class Test {

    @org.junit.Test
    public void testSearchBook(){
        System.out.println("单元测试");
    }

    @org.junit.Test
    public void testUrlBase64(){
        String imageBase64 = getUrlBase64Image("https://lain.bgm.tv/pic/cover/c/63/85/192063_jp.jpg");
        System.out.println("------------------------------");
        System.out.println(imageBase64);
        System.out.println("------------------------------");
    }

}
