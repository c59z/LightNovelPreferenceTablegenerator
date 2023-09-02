package com.yuki.Utils;

import com.alibaba.fastjson.JSONArray;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
import com.yuki.domain.vo.Book;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class HtmlExtractUtils {


    public static JSONArray GetTrueString(String HtmlText){
        List<String> titles = new ArrayList<>();
        List<String> covers = new ArrayList<>();
        List<String> infos = new ArrayList<>();
        List<Book> books = new ArrayList<>();

        // 解析HTML
        Document document = Jsoup.parse(HtmlText);
        // 书名
        Elements elements_title = document.select("li>div>h3>a");
        // 原书名
        Elements elements_stitle = document.select("li>div>h3>small");
        // 封面
        Elements elements_cover = document.select("li>a>span.image>img");
        // 书籍信息
        Elements elements_info = document.select("li>div>p.info.tip");

        for(int i=0;i<elements_cover.size();i++)
        {
            String title = "";
            String stitle = "";
            String src = "";
            String cover = "";
            String info = "";
            String base64Cover = "";
            try {
                title = elements_title.get(i).text(); // 获取小说名称
            }catch (Exception e){
                title = "";
            }
            try {
                stitle = elements_stitle.get(i).text(); // 获取小说原名
            }catch (Exception e){
                stitle = "";
            }
            try {
                src = elements_cover.get(i).attr("src"); // 获取封面url
            }catch (Exception e){
                cover = "";
            }
            cover = "https:"+src; // 拼接url
            try {
                info = elements_info.get(i).text(); // 获取书籍简介
            }catch (Exception e){
                info = "";
            }
            base64Cover = getUrlBase64Image(cover);
            Book book = new Book();
            book.setTitle(title);
            book.setCover(cover);
            book.setInfo(info);
            book.setCoverBase64(base64Cover);
            book.setStitle(stitle);
            books.add(book);
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(books);



        return jsonArray;
    }

    public static String  getUrlBase64Image(String coverUrl){
        String base64Data = "";
        try{
            URL url = new URL(coverUrl);
            InputStream inputStream = url.openStream();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = outputStream.toByteArray();

            base64Data = Base64.getEncoder().encodeToString(imageBytes);
            base64Data = "data:image/jpeg;base64," + base64Data;

            inputStream.close();
            outputStream.close();
        }catch (Exception e){
            System.out.println("错误>>>"+e.getMessage());
        }
        return base64Data;
    }

}
