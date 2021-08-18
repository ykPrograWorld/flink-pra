package org.example.join;

import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.example.bean.FactOrderItem;
import org.example.bean.Goods;
import org.example.bean.OrderItem;
import org.example.source.GoodsSource;
import org.example.source.OrderSource;
import org.example.utils.FlinkUtils;

import java.math.BigDecimal;

/**
 * cogroup pra  一个流|数据集中有没有找到与另一个匹配的数据都会输出
 */
public class Join08 {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment streamEnv = FlinkUtils.getStreamEnv();
        streamEnv.setParallelism(1);
        DataStreamSource<Goods> goodSour1 = streamEnv.addSource(new GoodsSource());
        DataStreamSource<OrderItem> orderSour2 = streamEnv.addSource(new OrderSource());

        DataStream<FactOrderItem> goodOrderCogr = goodSour1.coGroup(orderSour2).where(new KeySelector<Goods, String>() {
            @Override
            public String getKey(Goods goods) throws Exception {
                return goods.getGoodsId();
            }
        }).equalTo(new KeySelector<OrderItem, String>() {
            @Override
            public String getKey(OrderItem orderItem) throws Exception {
                return orderItem.getGoodsID();
            }
        }).window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .apply(new CoGroupFunction<Goods, OrderItem, FactOrderItem>() {
                    FactOrderItem factOrderItem = null;

                    @Override
                    public void coGroup(Iterable<Goods> sour1, Iterable<OrderItem> sour2, Collector<FactOrderItem> collector) throws Exception {
                        factOrderItem = new FactOrderItem();
                        int i = 0;
                        int j = 0;
                        for (Goods g : sour1
                        ) {
                            i++;
                            System.out.println("good:"+g.getGoodsId()+"出现"+i+"次");
                            factOrderItem.setGoodsId(g.getGoodsId());
                            factOrderItem.setGoodsName(g.getGoodsName());
                        }

                        for (OrderItem o : sour2
                        ) {
                            j++;
                            System.out.println("order:"+o.getGoodsID()+"出现"+j+"次");
                            factOrderItem.setCount(new BigDecimal(o.getCount()));
                        }
                        collector.collect(factOrderItem);
                    }
                });

        goodSour1.print("good:");
        orderSour2.print("orderItem:");
        goodOrderCogr.print("cogroup:");

        streamEnv.execute("cogroup pra");
    }
}
