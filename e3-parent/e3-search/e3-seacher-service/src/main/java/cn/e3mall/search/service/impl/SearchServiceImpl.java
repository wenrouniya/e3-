package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchItemDao;
import cn.e3mall.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchItemDao searchItemDao;

    @Override
    public SearchResult search(String keyword, int page, int row) throws Exception{
        //创建一个SolrQuey对象
        SolrQuery query=new SolrQuery();
        //设置查询条件
        query.setQuery(keyword);
        //设置分页条件
        if(page<=0) page=1;
        query.setStart((page-1)*row);
        query.setRows(row);
        //设置默认搜索域
        query.set("df","item_title");
        //开启高亮显示
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePre("</em>");
        //调用dao执行查询
       SearchResult result=searchItemDao.searchItemSolr(query);
        System.out.println(result);
        //计算总页数
        long pages=result.getTotalPages();
        int totalPages=page/row;
        if(page%row!=0)
            totalPages++;
        //将总页数添加至返回结果
        result.setTotalPages(totalPages);
        return result;
    }
}
