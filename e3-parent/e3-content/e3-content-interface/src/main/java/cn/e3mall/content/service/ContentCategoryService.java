package cn.e3mall.content.service;

import cn.e3mall.common.pojo.EasyUIDTreeNode;
import cn.e3mall.common.utils.E3Result;

import java.util.List;


public interface ContentCategoryService {
    List<EasyUIDTreeNode> getContentCatList(long parentId);
    E3Result addContentCategory(Long parentId,String name);
}
