package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUIDTreeNode;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品分类管理controller
 */
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService catService;
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUIDTreeNode> findItemCatList(@RequestParam(name ="id",
            defaultValue = "0") Long  parentId){

        List<EasyUIDTreeNode> list= catService.getItemCatList(parentId);
        return  list;

    }


}
