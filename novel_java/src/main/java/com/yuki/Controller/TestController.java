package com.yuki.Controller;

import com.yuki.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;

@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseResult Test(){
        return ResponseResult.success("Test");
    }

    @GetMapping("testphoto")
    public ResponseResult TestPhoto(){
        String[] cover_list= {"D:/photo/ACG/小说插图/cover1.jpg",
                "D:/photo/ACG/小说插图/cover2.jpg",
                "D:/photo/ACG/小说插图/cover3.jpg",
                "D:/photo/ACG/小说插图/cover4.jpg",
                "D:/photo/ACG/小说插图/cover5.jpg",
                "D:/photo/ACG/小说插图/cover6.jpg",
                "D:/photo/ACG/小说插图/cover7.jpg",
                "D:/photo/ACG/小说插图/cover8.jpg",
                "D:/photo/ACG/小说插图/cover9.jpg",
                "D:/photo/ACG/小说插图/p015.jpg",
                "D:/photo/ACG/小说插图/p031.jpg",
                "D:/photo/ACG/小说插图/p045.jpg",
                "D:/photo/ACG/小说插图/p085.jpg",
                "D:/photo/ACG/小说插图/p191 .jpg",
                "D:/photo/ACG/小说插图/p213.jpg",
        };
        return ResponseResult.success(cover_list);
    }


    @Autowired
    private RestTemplate restTemplate;

    /**
     *  将图片转为base64
     * @param coverUrl 图片地址
     * @return
     */
    @GetMapping("/testbase64")
    public ResponseEntity<String> getBase64Img(@RequestParam String coverUrl){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

        ResponseEntity<byte[]> imageResponse = restTemplate.exchange(
                coverUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class
        );


        if (imageResponse.getStatusCode() == HttpStatus.OK) {
            byte[] imageBytes = imageResponse.getBody();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return ResponseEntity.ok(base64Image);
        } else {
            return ResponseEntity.status(imageResponse.getStatusCode()).build();
        }

    }
}
