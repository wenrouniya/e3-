package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUIDTreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理的controller
 */
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUIDTreeNode> getContentCatList(
            @RequestParam(name= "id", defaultValue = "0") Long parentId){

    List<EasyUIDTreeNode> list=contentCategoryService.getContentCatList(  parentId);
    return list;
    }

    /**
     * 添加分类节点
     *
     */
    @RequestMapping(value = "/content/category/create",method = RequestMethod.POST)
    @ResponseBody
    public E3Result createContentCategory(Long parentId,String name){
        E3Result result=contentCategoryService.addContentCategory(parentId,name);
        return result;
    }
}
