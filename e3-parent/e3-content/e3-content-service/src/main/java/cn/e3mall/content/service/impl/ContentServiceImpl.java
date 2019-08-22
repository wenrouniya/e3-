package cn.e3mall.content.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;

import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper mapper;
   @Autowired
   private JedisClient jedisClient;
   @Value("${CONTENT_LIST}")
   private String CONTENT_LIST;
    @Override
    public E3Result addContent(TbContent content) {
        content.setUpdated(new Date());
        content.setCreated(new Date());
        //插入到数据库
        mapper.insert(content);
        //缓存同步
        jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
        return E3Result.ok();
    }

    @Override
    public List<TbContent> getContentByCid(Long cid) {
        //查询缓存
        try{
          String json=  jedisClient.hget(CONTENT_LIST,cid+"");
            if(StringUtils.isNotBlank(json)){
                List<TbContent> list=JsonUtils.jsonToList(json,TbContent.class);
                return  list;
            }
        }catch(Exception  e){
            //异常不抛出 写入日志
            e.printStackTrace();
        }
        //有 直接返回结果
        //没有直接查询数据库
        TbContentExample example=new TbContentExample();
      Criteria criteria=example.createCriteria();
      //设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        //执行查询
        List<TbContent> list=mapper.selectByExampleWithBLOBs(example);
        //将结果加入缓存
        try{
            jedisClient.hset(CONTENT_LIST,cid+"", JsonUtils.objectToJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
