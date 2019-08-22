package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.EasyUIDTreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public E3Result addContentCategory(Long parentId, String name) {
        //创建一个item_content_category表对应的pojo
        TbContentCategory category=new TbContentCategory();
        //为pojo设置属性
        category.setParentId(parentId);
        category.setName(name);
        category.setIsParent(false);
        //1正常 2删除
        category.setStatus(1);
        //默认排序为1
        category.setSortOrder(1);
        category.setUpdated(new Date());
        category.setCreated(new Date());
        //插入到数据库
        contentCategoryMapper.insert(category);
        //根据parentid查询父节点
        TbContentCategory parent=contentCategoryMapper.selectByPrimaryKey(parentId);
        //判断父节点的isParent
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            //更新到数据库
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果 返回E3Result  包含pojo
        return E3Result.ok(category);
    }


}
