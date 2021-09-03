package org.example.windowpra;

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
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.example.utils.FlinkUtils;

import java.time.Duration;

public class Window03 {
    public static void main(String[] args) throws Exception{
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
                .process(new ProcessWindowFunction<Tuple2<String, Long>, Object, String, TimeWindow>() {
                        ValueState<Tuple2> startBus;
                        ValueState<Tuple2> endBus;

                        @Override
                        public void open(Configuration parameters) throws Exception {
                            startBus = getRuntimeContext().getState(new ValueStateDescriptor<>("startBus", Tuple2.class));
                            endBus = getRuntimeContext().getState(new ValueStateDescriptor<>("endBus", Tuple2.class));
                        }

                        @Override
                        public void process(String s, Context context, Iterable<Tuple2<String, Long>> iterable, Collector<Object> collector) throws Exception {


                    }
                });

        streamEnv.execute("reduce test");
    }
}
