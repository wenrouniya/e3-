package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;


public interface ItemService {

    TbItem  getItemById(Long itemId);
    EasyUIDataGridResult  getItem(int page,int rows);
    E3Result addItem(TbItem tbItem,String  desc);
    TbItemDesc getItemDescById(Long  itemId);
}
