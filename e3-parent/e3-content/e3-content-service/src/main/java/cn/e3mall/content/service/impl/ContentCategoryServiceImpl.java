package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.EasyUIDTreeNode;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUIDTreeNode> getContentCatList(long parentId) {
        //根据parentid查询子节点列表
        TbContentCategoryExample example=new TbContentCategoryExample();
        Criteria critera=example.createCriteria();
        //设置查询条件
        critera.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> catList=contentCategoryMapper.selectByExample(example);
        //转换为EasyUIDTreeNode
        List<EasyUIDTreeNode> nodeList=new ArrayList<>();
        for (TbContentCategory tbContentCategory:catList){
            EasyUIDTreeNode node=new EasyUIDTreeNode();
            node.setId(tbContentCategory.getId());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            node.setText(tbContentCategory.getName());
            nodeList.add(node);
        }
        return nodeList;

    }


}
