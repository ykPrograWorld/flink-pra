package org.example.join;

import jdk.nashorn.internal.runtime.ECMAException;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.example.bean.FactOrderItem;
import org.example.bean.Goods;
import org.example.bean.OrderItem;
import org.example.source.GoodsSource;
import org.example.source.OrderSource;
import org.example.utils.FlinkUtils;
import org.omg.PortableInterceptor.INACTIVE;

import java.math.BigDecimal;

/**
 * join之 joinFunc
 */
public class Join07 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamEnv = FlinkUtils.getStreamEnv();
        streamEnv.setParallelism(1);
        DataStreamSource<Goods> goodSour = streamEnv.addSource(new GoodsSource());
        DataStreamSource<OrderItem> ordSour = streamEnv.addSource(new OrderSource());

        DataStream<FactOrderItem> windowJoin = goodSour.join(ordSour)
                .where(new KeySelector<Goods, String>() {
                    @Override
                    public String getKey(Goods goods) throws Exception {
                        return goods.getGoodsId();
                    }
                }).equalTo(new KeySelector<OrderItem, String>() {
                    @Override
                    public String getKey(OrderItem orderItem) throws Exception {
                        return orderItem.getGoodsID();
                    }
                }).window(TumblingProcessingTimeWindows.of(Time.milliseconds(5)))
                .apply(new JoinFunction<Goods, OrderItem, FactOrderItem>() {
                    @Override
                    public FactOrderItem join(Goods goods, OrderItem orderItem) throws Exception {
                        return new FactOrderItem(goods.getGoodsId(), goods.getGoodsName(), new BigDecimal(orderItem.getCount()), new BigDecimal(orderItem.getCount()).multiply(goods.getGoodsPrice()));
                    }
                });

        windowJoin.print("joinFunc");
        streamEnv.execute("joinFunc...");
    }
}
