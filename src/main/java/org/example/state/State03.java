package org.example.state;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.example.udfunc.ListStateFunc;
import org.example.udfunc.UdfMapSsFunc;
import org.example.utils.FlinkUtils;

/**
 * liststate
 */

public class State03 {
    public static void main(String[] args) throws Exception{
        DataStreamSource streamSource = FlinkUtils.getStreamSource("action");

        streamSource.map(new UdfMapSsFunc())
                .keyBy(0)
                .process(new ListStateFunc())
                .print();
        FlinkUtils.getStreamEnv().execute("liststate");

    }
}
