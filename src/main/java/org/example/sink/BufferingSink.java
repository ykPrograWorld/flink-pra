package org.example.sink;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import scala.Int;

import java.util.ArrayList;
import java.util.List;

public class BufferingSink implements SinkFunction<Tuple2<String,Integer>>, CheckpointedFunction {
    private final int threshold;

    private transient ListState<Tuple2<String, Integer>> chekpointedSate;

    private List<Tuple2<String,Integer>> bufferedElements;

    public BufferingSink(int threshold){
        this.threshold = threshold;
        this.bufferedElements = new ArrayList<>();
    }

    @Override
    public void invoke(Tuple2<String, Integer> value, Context context) throws Exception {
        bufferedElements.add(value);
        if(bufferedElements.size() == threshold){
            for(Tuple2<String,Integer> element:bufferedElements){
                System.out.println(element);
            }
            bufferedElements.clear();
        }
    }

    @Override
    public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
        chekpointedSate.clear();
        for(Tuple2<String,Integer> element : bufferedElements){
            chekpointedSate.add(element);
        }
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<Tuple2<String,Integer>> descriptor = new ListStateDescriptor<Tuple2<String, Integer>>("buffered-elements", TypeInformation.of(
                new TypeHint<Tuple2<String, Integer>>() {
                }
        ));

        chekpointedSate =  context.getOperatorStateStore().getListState(descriptor);

        if(context.isRestored()){
            for (Tuple2<String,Integer> element : chekpointedSate.get()){
                bufferedElements.add(element);
            }
        }

    }
}
