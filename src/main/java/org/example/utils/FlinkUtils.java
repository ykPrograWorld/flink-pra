package org.example.utils;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class FlinkUtils {

    static StreamExecutionEnvironment env;

    public static DataStreamSource getStreamSource(String fileName){
        StreamExecutionEnvironment env = getStreamEnv();
        DataStreamSource<String> wStream = env.readTextFile("src/main/resources/"+fileName+".txt");
        return wStream;
    }

    public static DataStreamSource getKafkaStreamSource() throws Exception{
        StreamExecutionEnvironment env = getStreamEnv();
        String topic = "";
        Properties pro = new Properties();
        FlinkKafkaConsumer flinkKafkaConsumer = new FlinkKafkaConsumer<>(topic, SimpleStringSchema.class.newInstance(),pro);
        DataStreamSource streamSource = env.addSource(flinkKafkaConsumer);
        return streamSource;
    }

    public static StreamExecutionEnvironment getStreamEnv(){
        if(env == null){
            env = StreamExecutionEnvironment.getExecutionEnvironment();
        }
        return env;
    }



}
