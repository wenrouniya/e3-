package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDTreeNode;
import cn.e3mall.common.utils.E3Result;

import java.util.List;

public interface ItemCatService {
    List<EasyUIDTreeNode> getItemCatList(Long parentId );

}
