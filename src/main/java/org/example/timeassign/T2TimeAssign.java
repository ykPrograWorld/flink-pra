package org.example.timeassign;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.java.tuple.Tuple2;

public class T2TimeAssign implements SerializableTimestampAssigner<Tuple2<String,Long>> {
    @Override
    public long extractTimestamp(Tuple2<String, Long> t2, long l) {
        return t2.f1;
    }
}
