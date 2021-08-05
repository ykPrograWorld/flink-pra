package org.example.join;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.example.utils.FlinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Join01 {

    private static final Logger log = LoggerFactory.getLogger(Join01.class);

    private static final String[] type = {"a","b","c","d"};

    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = FlinkUtils.getStreamEnv();
        DataStreamSource<Tuple2<String, Integer>> ordersource1 = env.addSource(new SourceFunction<Tuple2<String, Integer>>() {
            private volatile boolean isRunning = true;
            private final Random random = new Random();

            @Override
            public void run(SourceContext<Tuple2<String, Integer>> out) throws Exception {
                while (isRunning) {
                    TimeUnit.SECONDS.sleep(1);
                    Tuple2<String, Integer> tuple2 = Tuple2.of(type[random.nextInt(type.length)], random.nextInt(10));
                    System.out.println(new Date() + ",ordersource1提交元素：" + tuple2);
                    out.collect(tuple2);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        }, "ordersource1");


        DataStreamSource<Tuple2<String, Integer>> ordersource2 = env.addSource(new SourceFunction<Tuple2<String, Integer>>() {
            private volatile boolean isRunning = true;
            private final Random random = new Random();

            @Override
            public void run(SourceContext<Tuple2<String, Integer>> out) throws Exception {
                while (isRunning) {
                    TimeUnit.SECONDS.sleep(1);
                    Tuple2<String, Integer> tuple2 = Tuple2.of(type[random.nextInt(type.length)], random.nextInt(10));
                    System.out.println(new Date() + ",ordersource2提交元素：" + tuple2);
                    out.collect(tuple2);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        }, "ordersource2");

        ordersource1.join(ordersource2).where(new KeySelector<Tuple2<String, Integer>, String>() {
            @Override
            public String getKey(Tuple2<String, Integer> input) throws Exception {
                return input.f0;
            }
        }).equalTo(new KeySelector<Tuple2<String, Integer>, String>() {
            @Override
            public String getKey(Tuple2<String, Integer> input) throws Exception {
                return input.f0;
            }
        }).window(TumblingProcessingTimeWindows.of(Time.seconds(5))).apply(new JoinFunction<Tuple2<String, Integer>, Tuple2<String, Integer>, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String, Integer> join(Tuple2<String, Integer> first, Tuple2<String, Integer> second) throws Exception {
                return Tuple2.of(first.f0,first.f1 + second.f1);
            }
        }).print();

        FlinkUtils.getStreamEnv().execute("Flink JoinTest");
    }
}
