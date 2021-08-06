package org.example.join;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.example.bean.Goods;
import org.example.bean.OrderItem;
import org.example.source.GoodsSource;
import org.example.source.OrderSource;
import org.example.utils.FlinkUtils;

import java.util.Date;

/**
 * connect 连接流  定时器
 */

public class Join05 {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment streamEnv = FlinkUtils.getStreamEnv();
//        商品流
        DataStreamSource<Goods> goodSource = streamEnv.addSource(new GoodsSource());

        KeyedStream<Goods, String> goodWmSour = goodSource.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Goods>(Time.seconds(5)) {
            @Override
            public long extractTimestamp(Goods goods) {
                return goods.getEvnTime();
            }
        }).keyBy(Goods::getGoodsId);

//        订单流
        DataStreamSource<OrderItem> orderSource = streamEnv.addSource(new OrderSource());

        KeyedStream<OrderItem, String> orderWmSour = orderSource.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<OrderItem>(Time.seconds(5)) {
            @Override
            public long extractTimestamp(OrderItem orderItem) {
                return orderItem.getEvnTime();
            }
        }).keyBy(OrderItem::getGoodsID);

//        定义侧输出流
        OutputTag<Goods> goodsTag = new OutputTag<Goods>("goods"){};
        OutputTag<OrderItem> ordersTag = new OutputTag<OrderItem>("orders"){};


        SingleOutputStreamOperator<Tuple2<Goods, OrderItem>> connPro = goodWmSour.connect(orderWmSour).process(new CoProcessFunction<Goods, OrderItem, Tuple2<Goods, OrderItem>>(

        ) {
            //            goods的状态
            ValueState<Goods> goodState;
            ValueState<OrderItem> orderState;
            //           定义用于删除定时器的状态
            ValueState<Long> timeState;

            @Override
            public void open(Configuration parameters) throws Exception {
//                初始化状态
                goodState = getRuntimeContext().getState(new ValueStateDescriptor<>("goodState", Goods.class));
                orderState = getRuntimeContext().getState(new ValueStateDescriptor<>("orderState", OrderItem.class));
                timeState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("timeState", Long.class));
            }

            @Override
            public void processElement1(Goods goods, Context context, Collector<Tuple2<Goods, OrderItem>> collector) throws Exception {
                OrderItem orderVal = orderState.value();
//                流2不为空，表示流2先来了，直接将两个流拼接发到下游
                if (orderVal != null) {
                    collector.collect(Tuple2.of(goods, orderVal));
//                清空流2对用的state信息
                    orderState.clear();
//                流2来了就可以删除定时器，把定时器的状态清除
                    context.timerService().deleteEventTimeTimer(timeState.value());
                    timeState.clear();
                } else {
//                    流2还没来，将流1放入goodstate中
                    goodState.update(goods);
//                    并注册一个1分钟的定时器，流1中的 eventTime + 60s
                    long time = goods.getEvnTime() + 6 * 1000;
                    timeState.update(time);
                    context.timerService().registerEventTimeTimer(time);
                }
            }

            @Override
            public void processElement2(OrderItem orderItem, Context context, Collector<Tuple2<Goods, OrderItem>> collector) throws Exception {
                Goods goodVal = goodState.value();
                if (goodState != null) {
                    collector.collect(Tuple2.of(goodVal, orderItem));
                    goodState.clear();
                    context.timerService().deleteEventTimeTimer(timeState.value());
                    timeState.clear();
                } else {
                    orderState.update(orderItem);
                    long time = orderItem.getEvnTime() + 6 * 1000;
                    timeState.update(time);
                    context.timerService().registerEventTimeTimer(time);
                }
            }


            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<Tuple2<Goods, OrderItem>> out) throws Exception {
//          定时器触发，即1分钟内没有收到两个流
//          流1不为空，则将流1侧切输出
                if (goodState.value() != null) {
                    ctx.output(goodsTag, goodState.value());
                }

//          流2不为空，则将流2侧切输出
                if (orderState.value() != null) {
                    ctx.output(ordersTag, orderState.value());
                }

                goodState.clear();
                orderState.clear();
            }
        });

        connPro.print("当前时间"+ new Date() +": ");
//        connPro.getSideOutput(goodsTag).print("goods侧输出流");
//        connPro.getSideOutput(ordersTag).print("order侧输出流");


        streamEnv.execute("connect test");


    }
}
