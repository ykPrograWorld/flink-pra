package org.example.source;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.example.bean.GoodList;
import org.example.bean.Goods;
import org.example.bean.ItemId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

//实时生成商品数据流
public class GoodsSource extends RichSourceFunction<Goods> {
    private Boolean isCancel;
    public static List<Goods> goods_List;
    private Random r;
    @Override
    public void open(Configuration parameters) throws Exception {
        r = new Random();
        isCancel = false;

          goods_List = GoodList.getGoodList();
//        goods_List = new ArrayList<>();
//        goods_List.add(new Goods("1", "小米12", new BigDecimal(4890),System.currentTimeMillis()));
//        goods_List.add(new Goods("2", "iphone12", new BigDecimal(4890),System.currentTimeMillis()));
//        goods_List.add(new Goods("3", "macbookpro", new BigDecimal(4890),System.currentTimeMillis()));
//        goods_List.add(new Goods("4", "thinkpadx1", new BigDecimal(6890),System.currentTimeMillis()));
//        goods_List.add(new Goods("5", "meizu", new BigDecimal(8890),System.currentTimeMillis()));
//        goods_List.add(new Goods("6", "mate40", new BigDecimal(9890),System.currentTimeMillis()));

    }

    @Override
    public void run(SourceContext<Goods> sourceContext) throws Exception {
        while (!isCancel) {
        for (int i = 0; i < 10; i++) {
            Goods goods = goods_List.get(r.nextInt(6));
            goods.setItemId(ItemId.getItemId());
            sourceContext.collect(goods);
            TimeUnit.SECONDS.sleep(1);
        }
        }
    }

    @Override
    public void cancel() {
        isCancel = true;
    }
}
