package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.EasyUIDTreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private  TbItemCatMapper itemCatMapper;
    @Override
    public List<EasyUIDTreeNode> getItemCatList(Long parentId) {
       //根据parentId查询子节点列表
        TbItemCatExample example =new TbItemCatExample();
        Criteria criteria =example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat>  list=itemCatMapper.selectByExample(example);
        //创建返回结果list
        List<EasyUIDTreeNode> resultList=new ArrayList<>();
        //将结果转换为树状列表
        for (TbItemCat tbItemCat:list){
            EasyUIDTreeNode node =new EasyUIDTreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent()?"closed":"open");
            resultList.add(node);
         }


        return resultList;
    }
}
