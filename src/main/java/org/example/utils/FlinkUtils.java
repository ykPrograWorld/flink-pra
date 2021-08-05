package org.example.utils;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkUtils {

    static StreamExecutionEnvironment env;

    public static DataStreamSource getStreamSource(String fileName){
        StreamExecutionEnvironment env = getStreamEnv();
        DataStreamSource<String> wStream = env.readTextFile("src/main/resources/"+fileName+".txt");
        return wStream;
    }

    public static StreamExecutionEnvironment getStreamEnv(){
        if(env == null){
            env = StreamExecutionEnvironment.getExecutionEnvironment();
        }
        return env;
    }
}
