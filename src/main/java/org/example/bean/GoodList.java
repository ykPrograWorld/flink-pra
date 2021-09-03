package org.example.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodList {
    public static List<Goods> getGoodList(Long evnTime){
        ArrayList<Goods> goods_List = new ArrayList<>();
        if(evnTime!=0){
            goods_List.add(new Goods("1", "小米12", new BigDecimal(4890),evnTime));
            goods_List.add(new Goods("2", "iphone12", new BigDecimal(4890),evnTime));
            goods_List.add(new Goods("3", "macbookpro", new BigDecimal(4890),evnTime));
            goods_List.add(new Goods("4", "thinkpadx1", new BigDecimal(6890),evnTime));
            goods_List.add(new Goods("5", "meizu", new BigDecimal(8890),evnTime));
            goods_List.add(new Goods("6", "mate40", new BigDecimal(9890),evnTime));
        }
        return goods_List;
    }
}
