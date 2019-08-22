package cn.e3mall.item.message;


import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class listener implements MessageListener {

    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${LOCAL_STATIC_WEB}")
    private String  LOCAL_STATIC_WEB;
    @Override
    public void onMessage(Message message) {

        try {
            //创建一个模板，参考jsp  即将jsp页面改为ftl  语法遵循ftl
            //从消息中取商品id
            TextMessage textmessage=(TextMessage)message;
            String text=textmessage.getText();
            Long itemId=new Long(text);
            //根据商品id查询商品信息 商品基本信息和商品描述
            TbItem tbItem=itemService.getItemById(itemId);
            Item item =new Item(tbItem);
            //取商品描述
            TbItemDesc itemDesc =itemService.getItemDescById(itemId);
            //创建一个数据集 把商品数据封装
            Map data=new HashMap<>();
            data.put("item",item);
            data.put("itemDesc",itemDesc);
            //加载模板对象
            Configuration configuration=freeMarkerConfigurer.getConfiguration();
            Template template=configuration.getTemplate("item.ftl");
            //创建一个输出页面  指定输出的目录及文件名
            Writer out=new FileWriter(LOCAL_STATIC_WEB+itemId+".html");
            //生成静态页面
            template.process(data,out);
            //关闭流
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
