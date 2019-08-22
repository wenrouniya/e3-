package cn.e3mall.service.impl;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;


@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination topicDestination;
    @Autowired
    private JedisClient jedisClient;
    @Value("${item_list_prename}")
    private String item_list_prename;
    @Value("${item_list_lifetime}")
    private Integer item_list_lifetime;

    @Override
    public TbItem getItemById(Long itemId) {
        //查询缓存
        try {
            String json = jedisClient.get(item_list_prename + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //缓存中没有  查询数据库
        //根据主键查询
        // TbItem tbItem=itemMapper.selectByPrimaryKey(itemId);
        //根据条件查询
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        //查询条件
        criteria.andIdEqualTo(itemId);
        //执行查询
        List<TbItem> list = itemMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            try {
                //添加到缓存
                jedisClient.set(item_list_prename + ":" + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)));
                //设置缓存的过期时间
                jedisClient.expire(item_list_prename + ":" + itemId + ":BASE", item_list_lifetime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list.get(0);
        }
        return null;

    }

    @Override
    public EasyUIDataGridResult getItem(int page, int rows) {
//        设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //创建一个返回值对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        //取总记录条数
        long total = pageInfo.getTotal();
        result.setTotal(total);
        return result;
    }

    @Override
    public E3Result addItem(TbItem tbItem, String desc) {
        //生成商品id
        final long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        //1-正常  2-下架 3-删除
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //向商品表中插入数据
        itemMapper.insert(tbItem);
        //创建一个商品表对应的pojo对象
        TbItemDesc itemDesc = new TbItemDesc();
        //补全属性
        itemDesc.setItemId(itemId);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDesc.setItemDesc(desc);
        //向商品描述表拆入数据
        itemDescMapper.insert(itemDesc);
        //发送商品添加消息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(itemId + "");
                return textMessage;
            }
        });
        return E3Result.ok();
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {

        try {
            String json = jedisClient.get(item_list_prename + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);

        try {
            //添加到缓存
            jedisClient.set(item_list_prename + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
            //设置缓存的过期时间
            jedisClient.expire(item_list_prename + ":" + itemId + ":DESC", item_list_lifetime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItemDesc;
    }
}
