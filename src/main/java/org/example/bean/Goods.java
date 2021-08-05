package org.example.bean;

import com.alibaba.fastjson.JSON;
import org.example.join.Join02;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//    商品类  (商品id,商品名称,商品价格)
public class Goods {
    private String goodsId;
    private String goodsName;
    private BigDecimal goodsPrice;
    public static List<Goods> goods_List;
    public static Random r;

    public Goods() {

    }

    public Goods(String goodsId, String goodsName, BigDecimal goodsPrice) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
    }


    static {
        r = new Random();
        goods_List = new ArrayList<>();
        goods_List.add(new Goods("1", "小米12", new BigDecimal(4890)));
        goods_List.add(new Goods("2", "iphone12", new BigDecimal(3890)));
        goods_List.add(new Goods("3", "macbookpro", new BigDecimal(5890)));
        goods_List.add(new Goods("4", "thinkpadx1", new BigDecimal(6890)));
        goods_List.add(new Goods("5", "meizu", new BigDecimal(8890)));
        goods_List.add(new Goods("6", "mate40", new BigDecimal(9890)));
    }

    public static Goods randomGoods() {
        int rIndex = r.nextInt(goods_List.size());
        return goods_List.get(rIndex);
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

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public static void setGoods_List(List<Goods> goods_List) {
        Goods.goods_List = goods_List;
    }

    public static void setR(Random r) {
        Goods.r = r;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public static List<Goods> getGoods_List() {
        return goods_List;
    }

    public static Random getR() {
        return r;
    }
}
