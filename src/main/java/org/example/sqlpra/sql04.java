package org.example.sqlpra;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;

/**
 * flink batch 旧表   只有旧计划器中支持
 */
public class sql04 {
    public static void main(String[] args) {
        EnvironmentSettings batchSet = EnvironmentSettings.newInstance().useBlinkPlanner().inBatchMode().build();
        TableEnvironment batchTableEnv = TableEnvironment.create(batchSet);
        Table table = batchTableEnv.fromValues(1, 2, 3);

        batchTableEnv.createTemporaryView("num",table);

        Table tableQ = batchTableEnv.sqlQuery("select * from num");
    }
}
