package cn.e3mall.search.controller;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Value("${SEARCH_ROWS}")
    private int SEARCH_ROWS;

    @RequestMapping("/search")

    public String search(String keyword,
                         @RequestParam(defaultValue ="1") Integer page, Model model) throws  Exception{
       //查询商品列表
        SearchResult searchResult= searchService.search(keyword,page,SEARCH_ROWS);
        //将结果显示给页面
        model.addAttribute("query",keyword);
        model.addAttribute("totalPages",searchResult.getTotalPages());
        model.addAttribute("page",page);
        model.addAttribute("recourdCount",searchResult.getRecordCount());
        model.addAttribute("itemList",searchResult.getItemList());

        //全局异常测试
       // int  a=1/0;
        //返回逻辑视图
        return  "search";
    }
}
