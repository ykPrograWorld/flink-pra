package org.example.state;

/**
 * keyedstate
 * 利用value状态计算平均值
 */

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.udfunc.CountWindowAv;
import org.example.udfunc.UdfMapFunc;
import org.example.utils.FlinkUtils;

public class State01 {
    public static void main(String[] args) throws Exception{
        DataStreamSource wStream = FlinkUtils.getStreamSource("avg");
        wStream.map(new UdfMapFunc()).keyBy(0).flatMap(new CountWindowAv()).print();
        FlinkUtils.getStreamEnv().execute("state pra");
    }
}
