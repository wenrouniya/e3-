package cn.e3mall.content.service;

import cn.e3mall.common.pojo.EasyUIDTreeNode;

import java.util.List;


public interface ContentCategoryService {
    List<EasyUIDTreeNode> getContentCatList(long parentId);

}
