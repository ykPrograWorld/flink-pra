package org.example.bean;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;

//关联结果
public class FactOrderItem {
    private String goodsId;
    private String goodsName;
    private BigDecimal count;
    private BigDecimal totalMoney;

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
}
