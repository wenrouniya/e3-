package cn.e3mall.controller;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传controller
 */
@Controller
public class PictureController {
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public String  uploadFile(MultipartFile uploadFile){
        try {
            //上传图片到图片服务器  使用工具类
            FastDFSClient fastDFSClient=new FastDFSClient("classpath:conf/client.conf");
            //取文件扩展名
           String originalFilename=  uploadFile.getOriginalFilename();
           String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            //得到图片地址 和文件名
            String  url=fastDFSClient.uploadFile(uploadFile.getBytes(),extName);
            //补充为完整的url
           url=IMAGE_SERVER_URL+url;
            //封装到map中 返回
            Map map=new HashMap<>();
            map.put("error",0);
            map.put("url",url);
            return JsonUtils.objectToJson(map);

        } catch (Exception e) {
            Map map=new HashMap<>();
            map.put("error",1);
            map.put("message","图片上传异常");
            return JsonUtils.objectToJson(map);        }

    }
}
