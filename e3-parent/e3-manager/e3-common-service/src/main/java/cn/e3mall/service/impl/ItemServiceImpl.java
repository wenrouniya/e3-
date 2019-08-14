package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

@Autowired
private TbItemMapper itemMapper;
@Autowired
private TbItemDescMapper itemDescMapper;


    @Override
    public TbItem getItemById(Long itemId) {
        //根据主键查询
       // TbItem tbItem=itemMapper.selectByPrimaryKey(itemId);
     //根据条件查询
        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        //查询条件
        criteria.andIdEqualTo(itemId);
        //执行查询
        List<TbItem> list=itemMapper.selectByExample(example);
        if(list!=null &&list.size()>0)

        return list.get(0);
        return null;

    }

    @Override
    public EasyUIDataGridResult getItem(int page, int rows) {
//        设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example=new TbItemExample();
        List<TbItem> list= itemMapper.selectByExample(example);
        //创建一个返回值对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        //取分页信息
        PageInfo<TbItem> pageInfo =new PageInfo<>(list);
        //取总记录条数
        long total =pageInfo.getTotal();
        result.setTotal(total);
        return  result;
    }

    @Override
    public E3Result addItem(TbItem tbItem, String desc) {
        //生成商品id
        long  itemId=IDUtils.genItemId();
        tbItem.setId(itemId);
        //1-正常  2-下架 3-删除
        tbItem.setStatus((byte)1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //向商品表中插入数据
        itemMapper.insert(tbItem);
        //创建一个商品表对应的pojo对象
        TbItemDesc itemDesc=new TbItemDesc();
        //补全属性
        itemDesc.setItemId(itemId);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDesc.setItemDesc(desc);
        //向商品描述表拆入数据
        itemDescMapper.insert(itemDesc);
        return E3Result.ok();
    }
}
