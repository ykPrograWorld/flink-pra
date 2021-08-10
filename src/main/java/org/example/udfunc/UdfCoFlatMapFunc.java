package org.example.udfunc;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;
import org.example.bean.FactOrderItem;
import org.example.bean.Goods;
import org.example.bean.OrderItem;
import scala.Int;
import scala.Tuple3;

public class UdfCoFlatMapFunc extends RichCoFlatMapFunction<Goods, OrderItem, Tuple3<String, Integer,Integer>> {

    private ValueState<Tuple2<String,Integer>> goodState;
    private ValueState<Tuple2<String,Integer>> orderState;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void flatMap1(Goods goods, Collector<Tuple3<String, Integer, Integer>> collector) throws Exception {

    }

    @Override
    public void flatMap2(OrderItem orderItem, Collector<Tuple3<String, Integer, Integer>> collector) throws Exception {

    }
}
