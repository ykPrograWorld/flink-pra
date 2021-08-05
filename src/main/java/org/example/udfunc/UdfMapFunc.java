package org.example.udfunc;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class UdfMapFunc implements MapFunction<String, Tuple2<Long,Long>> {
    @Override
    public Tuple2<Long, Long> map(String s) throws Exception {
        String[] split = s.split(",");
        return Tuple2.of(Long.parseLong(split[0]),Long.parseLong(split[1]));
    }
}
