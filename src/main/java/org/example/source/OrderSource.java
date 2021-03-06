package org.example.source;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.example.bean.GoodList;
import org.example.bean.Goods;
import org.example.bean.ItemId;
import org.example.bean.OrderItem;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

//实时生成订单数据流
public class OrderSource extends RichSourceFunction<OrderItem> {
    private boolean iscancel;
    private Random r;
    private GoodsSource goodsSource;
    @Override
    public void open(Configuration parameters) throws Exception {
        iscancel = false;
        r = new Random();
        GoodsSource goodsSource = new GoodsSource();
    }

    @Override
    public void run(SourceContext<OrderItem> sourceContext) throws Exception {
//        while (!iscancel) {
            List<Goods> goodList = GoodList.getGoodList(0l);
            Goods goods = goodList.get(r.nextInt(goodList.size()));
            OrderItem orderItem = new OrderItem();
            orderItem.setGoodsID(goods.getGoodsId());
            orderItem.setCount(r.nextInt(10) + 1);
            orderItem.setItemId(ItemId.getItemId());
            orderItem.setEvnTime(System.currentTimeMillis());
            sourceContext.collect(orderItem);
//            orderItem.setGoodsID("111");
//            sourceContext.collect(orderItem);
            TimeUnit.SECONDS.sleep(1);
//        }
    }

    @Override
    public void cancel() {
        iscancel = true;
    }
}
