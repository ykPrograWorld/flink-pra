package org.example.bean;

import com.alibaba.fastjson.JSON;

//订单明细类
public class OrderItem {
    private String itemId;
    private String goodsID;
    private Integer count;
    private Long evnTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setGoodsID(String goodsID) {
        this.goodsID = goodsID;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getItemId() {
        return itemId;
    }

    public String getGoodsID() {
        return goodsID;
    }

    public Integer getCount() {
        return count;
    }

    public void setEvnTime(Long evnTime) {
        this.evnTime = evnTime;
    }

    public Long getEvnTime() {
        return evnTime;
    }
}
