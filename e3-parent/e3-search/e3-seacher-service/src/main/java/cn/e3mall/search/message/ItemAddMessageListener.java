package cn.e3mall.search.message;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;


public class ItemAddMessageListener  implements  MessageListener {

    @Autowired
    private SolrServer solrServer;
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public void onMessage(Message message) {


        String text = null;
        try {
            //1.从消息中取出id
            TextMessage textmessage = (TextMessage) message;
            text = textmessage.getText();
            Long itemId = new Long(text);
            //等待事务提交
            Thread.sleep(1000);
            //根据id查询商品信息
            SearchItem searchItem = itemMapper.getById(itemId);
            //创建一个文档对象
            SolrInputDocument document = new SolrInputDocument();
            //向文档对象中添加域
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_category_name", searchItem.getCategory_name());
            //把文档对象写入索引库

                solrServer.add(document);


                //提交
                solrServer.commit();
            } catch (Exception e) {
                    e.printStackTrace();

            }
        }
    }

