package org.example.udfunc;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class UdfMapSsFunc implements MapFunction<String, Tuple2<String,String>> {
    @Override
    public Tuple2<String,String> map(String s) throws Exception {
        String[] split = s.split(",");
        return Tuple2.of(split[0],split[1]);
    }
}