package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDTreeNode;

import java.util.List;

public interface ItemCatService {
    List<EasyUIDTreeNode> getItemCatList(Long parentId );
}
