package org.example.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodList {
    public static List<Goods> getGoodList(){
        ArrayList<Goods> goods_List = new ArrayList<>();
        goods_List.add(new Goods("1", "小米12", new BigDecimal(4890),System.currentTimeMillis()));
        goods_List.add(new Goods("2", "iphone12", new BigDecimal(4890),System.currentTimeMillis()));
        goods_List.add(new Goods("3", "macbookpro", new BigDecimal(4890),System.currentTimeMillis()));
        goods_List.add(new Goods("4", "thinkpadx1", new BigDecimal(6890),System.currentTimeMillis()));
        goods_List.add(new Goods("5", "meizu", new BigDecimal(8890),System.currentTimeMillis()));
        goods_List.add(new Goods("6", "mate40", new BigDecimal(9890),System.currentTimeMillis()));

        return goods_List;
    }
}
