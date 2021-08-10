package org.example.join;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.bean.Goods;
import org.example.source.GoodsSource;
import org.example.utils.FlinkUtils;

/**
 * union pra
 */
public class Join06 {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment streamEnv = FlinkUtils.getStreamEnv();
        streamEnv.setParallelism(1);
        DataStreamSource<Goods> goodSour1 = streamEnv.addSource(new GoodsSource());
        DataStreamSource<Goods> goodSour2 = streamEnv.addSource(new GoodsSource());
        DataStreamSource<Goods> goodSour3 = streamEnv.addSource(new GoodsSource());
        DataStream<Goods> goodUnion = goodSour1.union(goodSour2, goodSour3);
        goodUnion.print("union:");
        streamEnv.execute("union pra");
    }
}
