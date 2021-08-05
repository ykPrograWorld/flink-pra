package org.example.join;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;
import org.example.bean.FactOrderItem;
import org.example.bean.Goods;
import org.example.bean.OrderItem;
import org.example.source.GoodsSource;
import org.example.source.OrderSource;
import org.example.utils.FlinkUtils;

import java.math.BigDecimal;

/**
 * connect 失败案例
 */

public class join04 {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment streamEnv = FlinkUtils.getStreamEnv();
//        商品数据流
        DataStreamSource<Goods> goodsDs = streamEnv.addSource(new GoodsSource());
//        订单数据流
        DataStreamSource<OrderItem> orderSource = streamEnv.addSource(new OrderSource());

        ConnectedStreams<Goods, OrderItem> connect = goodsDs.connect(orderSource);

        SingleOutputStreamOperator<FactOrderItem> coProcess = connect.process(new CoProcessFunction<Goods, OrderItem, FactOrderItem>() {

            private transient FactOrderItem factOrderItem;
            @Override
            public void open(Configuration parameters) throws Exception {
                factOrderItem = new FactOrderItem();
            }



            @Override
            public void processElement1(Goods goods, Context context, Collector<FactOrderItem> out) throws Exception {
                factOrderItem.setGoodsId(goods.getGoodsId());
                factOrderItem.setGoodsName(goods.getGoodsName());
                out.collect(factOrderItem);
            }

            @Override
            public void processElement2(OrderItem orderItem, Context context, Collector<FactOrderItem> out) throws Exception {
                factOrderItem.setCount(new BigDecimal(orderItem.getCount()));
                out.collect(factOrderItem);
            }
        });

//        SingleOutputStreamOperator<Object> coProcess = connect.process(new CoProcessFunction<Goods, OrderItem, Object>() {
//            @Override
//            public void processElement1(Goods goods, Context context, Collector<Object> collector) throws Exception {
//                collector.collect(goods);
//            }
//
//            @Override
//            public void processElement2(OrderItem orderItem, Context context, Collector<Object> collector) throws Exception {
//                collector.collect(orderItem);
//            }
//        });

        coProcess.print();

        streamEnv.execute("connect test");
    }
}
