package org.example.windowpra;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.example.utils.FlinkUtils;

import java.time.Duration;

public class Window04 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment streamEnv = FlinkUtils.getStreamEnv();

        streamEnv.setParallelism(1);

//        nc -lp 9000
        DataStreamSource<String> sockSour = streamEnv.socketTextStream("127.0.0.1", 9000, "\n");

        SingleOutputStreamOperator<Tuple2<String, Long>> sMap = sockSour.map(new MapFunction<String, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(String s) throws Exception {
                String[] sArr = s.split(",");
                return new Tuple2<String, Long>(sArr[0], Long.parseLong(sArr[1]));
            }
        });

        SerializableTimestampAssigner<Tuple2<String, Long>> t2Assign = new SerializableTimestampAssigner<Tuple2<String, Long>>() {
            @Override
            public long extractTimestamp(Tuple2<String, Long> t2, long l) {
                return t2.f1;
            }
        };

        sMap.assignTimestampsAndWatermarks(
                WatermarkStrategy.<Tuple2<String, Long>>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner(t2Assign))
                .keyBy(data -> data.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .apply(new RichWindowFunction<Tuple2<String, Long>, Object, String, TimeWindow>() {

                    ValueState<JSONObject> startBus;
                    ValueState<JSONObject> endBus;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        startBus = getRuntimeContext().getState(new ValueStateDescriptor<>("startBus", JSONObject.class));
                        endBus = getRuntimeContext().getState(new ValueStateDescriptor<>("endBus", JSONObject.class));
                    }

                    @Override
                    public void apply(String key, TimeWindow timeWindow, Iterable<Tuple2<String, Long>> iterable, Collector<Object> collector) throws Exception {
                        System.out.println(key);
                        System.out.println(iterable);
                        for (Tuple2<String, Long> t : iterable
                        ) {
                            JSONObject countJson = startBus.value();
                            if(countJson == null){
                                countJson = new JSONObject();
                            }
                            Object count = countJson.get(key);
                            if (count != null) {
                                Long nowValue = Long.valueOf(count.toString()) + 1;
                                countJson.put(key, nowValue);
                                startBus.update(countJson);
                            } else {
                                countJson.put(key, 1);
                                startBus.update(countJson);
                            }
                            collector.collect(t);

                        }
                        collector.collect(startBus.value());
                    }
                }).print();

        streamEnv.execute("reduce test");
    }
}
