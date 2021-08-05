package org.example.udfunc;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class ListStateFunc extends KeyedProcessFunction<String, Tuple2<String,String>,Tuple2<String, List<String>>> {

//   之前的操作记录
    private transient ListState<String> listState;

    @Override
    public void open(Configuration parameters) throws Exception {
        ListStateDescriptor<String> descriptor = new ListStateDescriptor<>(
                "recent-operator",
                String.class
        );
        listState = getRuntimeContext().getListState(descriptor);

    }

    @Override
    public void processElement(Tuple2<String, String> input, Context ctx, Collector<Tuple2<String, List<String>>> out) throws Exception {
        String action = input.f1;

        listState.add(action);

        Iterable<String> recItera = listState.get();

        ArrayList<String> events = new ArrayList<>();

        for (String actionName:recItera
             ) {
            events.add(actionName);
        }

        out.collect(Tuple2.of(input.f0,events));

        listState.update(events);
    }
}
