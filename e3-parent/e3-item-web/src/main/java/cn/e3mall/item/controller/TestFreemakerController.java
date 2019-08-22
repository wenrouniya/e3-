package cn.e3mall.item.controller;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


@Controller
public class TestFreemakerController {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/freemaker")
    @ResponseBody
    public String fraamaker() throws IOException, TemplateException {
    Configuration configuration=freeMarkerConfigurer.getConfiguration();
    //加载模板对象
        Template template = configuration.getTemplate("hello.ftl");
        //创建一个数据集
        Map data=new HashMap<>();
        data.put("hello",123456);
        //指定文件输出的路径及文件名
       Writer out= new FileWriter(new File("H:\\e3-parent\\ftl/hello.html"));
        //输出文件
        template.process(data,out);
        //关闭流
        out.close();

        return "ok";
    }

}
