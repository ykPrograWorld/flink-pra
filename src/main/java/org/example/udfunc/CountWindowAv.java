package org.example.udfunc;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

/**
 * 状态计算
 */

public class CountWindowAv extends RichFlatMapFunction<Tuple2<Long,Long>,Tuple2<Long,Long>> {

    private transient ValueState<Tuple2<Long,Long>> sum;


    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<Tuple2<Long, Long>> descriptor = new ValueStateDescriptor<>("av",
                TypeInformation.of(new TypeHint<Tuple2<Long, Long>>() {
                }),
                Tuple2.of(0L, 0L));

        sum = getRuntimeContext().getState(descriptor);
    }

    @Override
    public void flatMap(Tuple2<Long, Long> input, Collector<Tuple2<Long, Long>> output) throws Exception {
        Tuple2<Long, Long> curSum = sum.value();
        curSum.f0 += 1;
        curSum.f1 += input.f1;
        sum.update(curSum);

        if(curSum.f0 >= 2){
            output.collect(new Tuple2<>(input.f0, curSum.f1/curSum.f0));
            sum.clear();
        }
    }
}