package org.example.sqlpra;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * flink 旧流表
 */
public class sql01 {
    public static void main(String[] args) {
        EnvironmentSettings tSet = EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment sTableEnv = StreamTableEnvironment.create(env, tSet);



    }
}
