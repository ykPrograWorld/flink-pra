package org.example.wm;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.example.utils.FlinkUtils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * BoundedOutOfOrdernessTimestampExtractor 实现了 AssignerWithPeriodicWatermarks
 * flink1.11之后使用WatermarkStrategy生成watermark
 */
public class wm02 {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment streamEnv = FlinkUtils.getStreamEnv();

        streamEnv.setParallelism(1);

        DataStreamSource<String> sockSour = streamEnv.socketTextStream("127.0.0.1", 9000, "\n");

        SingleOutputStreamOperator<Tuple2<String, Long>> sMap = sockSour.map(new MapFunction<String, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(String s) throws Exception {
                String[] sArr = s.split(",");
                return new Tuple2<String, Long>(sArr[0], Long.parseLong(sArr[1]));
            }
        });

        SerializableTimestampAssigner<Tuple2<String, Long>> t2Assign = new SerializableTimestampAssigner<Tuple2<String, Long>>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            @Override
            public long extractTimestamp(Tuple2<String, Long> t2, long l) {
                return t2.f1;
            }
        };

        SingleOutputStreamOperator<Tuple2<String, Long>> t2Wm = sMap.assignTimestampsAndWatermarks(
                WatermarkStrategy.<Tuple2<String, Long>>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner(t2Assign));

        SingleOutputStreamOperator<String> t2Window = t2Wm.keyBy(data -> data.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(3)))
                .apply(new WindowFunction<Tuple2<String, Long>, String, String, TimeWindow>() {
                    @Override
                    public void apply(String s, TimeWindow timeWindow, Iterable<Tuple2<String, Long>> input, Collector<String> output) throws Exception {
                        String key = s;
                        ArrayList<Long> arrList = new ArrayList<>();
                        Iterator<Tuple2<String, Long>> it = input.iterator();
                        while (it.hasNext()) {
                            Tuple2<String, Long> next = it.next();
                            arrList.add(next.f1);
                        }
                        Collections.sort(arrList);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String result = "key值为：" + key + "," + "窗口数据个数为：" + arrList.size() + "," + "窗口内第一条数据时间戳为:" + sdf.format(arrList.get(0)) + "," +
                                "窗口内最后一条数据时间戳为:" + sdf.format(arrList.get(arrList.size() - 1)) + "," + "窗口的开始时间为:" + sdf.format(timeWindow.getStart()) + "," +
                                "窗口的结束时间为：" + sdf.format(timeWindow.getEnd());
                        output.collect(result);
                    }
                });
        t2Window.print("窗口数据为:");

        streamEnv.execute("WatermarkStrategy之BoundedOutOfOrderness");
    }
}
