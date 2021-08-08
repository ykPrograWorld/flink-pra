package org.example.wm;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.example.utils.FlinkUtils;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * with periodic watermarks:周期性的触发watermark的生成和发送
 * AssignerWithPeriodicWatermarks
 */
public class wm01 {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment streamEnv = FlinkUtils.getStreamEnv();
//        设置使用eventTime,默认使用processtime
        streamEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        streamEnv.setParallelism(1);

        DataStreamSource<String> text = streamEnv.socketTextStream("127.0.0.1", 9000, "\n");

        SingleOutputStreamOperator<Tuple2<String, Long>> inputTuple2 = text.map(new MapFunction<String, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(String s) throws Exception {
                String[] sArr = s.split(",");
                return Tuple2.of(sArr[0], Long.parseLong(sArr[1]));
            }
        });

        SingleOutputStreamOperator<Tuple2<String,Long>> tuple2Wm = inputTuple2.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple2<String,Long>>() {
            Long curMaxTimestamp = 0L;
            final Long maxOutOfOrderness = 3000L;  //最大允许乱序时间 10s
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            定义生成watermark的逻辑，默认每100ms被调用一次

            @Override
            public long extractTimestamp(Tuple2<String,Long> t2, long l) {
                long evnTime = t2.f1;
                curMaxTimestamp = Math.max(evnTime, curMaxTimestamp);
                System.out.println("key:" + t2.f0 + ",evnTime:" + sdf.format(evnTime) + ",curMaxTime:" + sdf.format(curMaxTimestamp)
                + ",watermark:"+ sdf.format(getCurrentWatermark().getTimestamp()));
                return evnTime;
            }

            @Nullable
            @Override
            public Watermark getCurrentWatermark() {
                return new Watermark(curMaxTimestamp - maxOutOfOrderness);
            }


        });

        OutputTag<Tuple2<String, Long>> outputTag = new OutputTag<Tuple2<String, Long>>("late-data") {
        };

        SingleOutputStreamOperator<String> goodsWin = tuple2Wm.keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.seconds(3)))
                .allowedLateness(Time.seconds(2))
//                .sideOutputLateData(outputTag)
                .apply(new WindowFunction<Tuple2<String,Long>, String, Tuple, TimeWindow>() {
                    @Override
                    public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<Tuple2<String,Long>> input, Collector<String> col) throws Exception {
                        String key = tuple.toString();
                        ArrayList<Long> arrList = new ArrayList<>();
                        Iterator<Tuple2<String,Long>> it = input.iterator();
                        while (it.hasNext()) {
                            Tuple2<String, Long> next = it.next();
                            arrList.add(next.f1);
                        }
                        Collections.sort(arrList);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String result = "key值为："+key + "," + "窗口数据个数为："+ arrList.size() + "," + "窗口内第一条数据时间戳为:"+ sdf.format(arrList.get(0)) + "," +
                                "窗口内最后一条数据时间戳为:"+sdf.format(arrList.get(arrList.size() - 1)) + "," + "窗口的开始时间为:" + sdf.format(timeWindow.getStart()) + "," +
                                "窗口的结束时间为："+sdf.format(timeWindow.getEnd());
                        col.collect(result);
                    }
                });
        DataStream<Tuple2<String, Long>> sideOutput = goodsWin.getSideOutput(outputTag);
        sideOutput.print("latedata:");

        goodsWin.print("window:");

        streamEnv.execute("period window");


    }

}

//public class BoundedOutofordernessGenerator implements AssignerWithPeriodicWatermarks<Goods>{
//    private final long maxOutOfOrderness = 3500; //3.5s
//
//    private long currentMaxTimestamp;
//
//    @Override
//    public long extractTimestamp(Goods g, long l) {
//        long timestamp = g.getEvnTime();
//        currentMaxTimestamp = Math.max(timestamp,currentMaxTimestamp);
//        return timestamp;
//    }
//
//    @Nullable
//    @Override
//    public Watermark getCurrentWatermark() {
//        return new Watermark(currentMaxTimestamp - maxOutOfOrderness);
//    }
//}
