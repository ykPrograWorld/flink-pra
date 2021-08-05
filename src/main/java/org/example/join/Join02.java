package org.example.join;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.example.bean.FactOrderItem;
import org.example.bean.Goods;
import org.example.bean.OrderItem;
import org.example.source.GoodsSource;
import org.example.source.OrderSource;
import org.example.utils.FlinkUtils;
import org.example.watermark.GoodsWaterMark;
import org.example.watermark.OrderItemWaterMark;

import java.math.BigDecimal;

/**
 * window join 滚动窗口
 */

public class Join02 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = FlinkUtils.getStreamEnv();

//        商品数据流
        DataStreamSource<Goods> goodsDs = env.addSource(new GoodsSource());
//        订单数据流
        DataStreamSource<OrderItem> orderSource = env.addSource(new OrderSource());

        SingleOutputStreamOperator<Goods> goodsDsWithWatermark = goodsDs.assignTimestampsAndWatermarks(new GoodsWaterMark());
        SingleOutputStreamOperator<OrderItem> orderItemDsWithWatermark = orderSource.assignTimestampsAndWatermarks(new OrderItemWaterMark());

        DataStream<FactOrderItem> resultDS = goodsDsWithWatermark.join(orderItemDsWithWatermark)
                .where(Goods::getGoodsId)
                .equalTo(OrderItem::getGoodsID)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .apply(new JoinFunction<Goods, OrderItem, FactOrderItem>() {
                    @Override
                    public FactOrderItem join(Goods goods, OrderItem orderItem) throws Exception {
                        FactOrderItem result = new FactOrderItem();
                        result.setGoodsId(goods.getGoodsId());
                        result.setGoodsName(goods.getGoodsName());
                        result.setCount(new BigDecimal(orderItem.getCount()));
                        result.setTotalMoney(new BigDecimal(orderItem.getCount()).multiply(goods.getGoodsPrice()));
                        return result;
                    }
                });
        resultDS.print();

        env.execute("window join");


    }


}
