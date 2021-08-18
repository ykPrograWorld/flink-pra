package org.example.sqlpra;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;

/**
 * flink 旧批表
 */
public class sql02 {
    public static void main(String[] args) {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        BatchTableEnvironment batEnv = BatchTableEnvironment.create(env);


    }
}
