package cn.e3mall.item.controller;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商品详情页面展示
 */

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public  String ShowItemInfo(@PathVariable Long itemId,Model model){
    //调用商品服务取基本信息
        TbItem tbItem=itemService.getItemById(itemId);
        Item item=new Item(tbItem);
        //取商品描述
        TbItemDesc itemDesc=itemService.getItemDescById(itemId);
        //信息传递给页面
        model.addAttribute("tbItem",tbItem);
        model.addAttribute("itemDesc",itemDesc);
        //返回逻辑视图
        return  "item";

    }


}
