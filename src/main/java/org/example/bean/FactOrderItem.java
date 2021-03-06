package org.example.bean;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;

//关联结果
public class FactOrderItem {
    private String itemId;
    private String goodsId;
    private String goodsName;
    private BigDecimal count;
    private BigDecimal totalMoney;

    public FactOrderItem() {
    }

    public FactOrderItem(String goodsId, String goodsName, BigDecimal count, BigDecimal totalMoney) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.count = count;
        this.totalMoney = totalMoney;
    }

    public FactOrderItem(String itemId, String goodsId, String goodsName, BigDecimal count, BigDecimal totalMoney) {
        this.itemId = itemId;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.count = count;
        this.totalMoney = totalMoney;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public BigDecimal getCount() {
        return count;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
