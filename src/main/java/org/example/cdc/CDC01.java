package org.example.cdc;

//import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import com.ververica.cdc.debezium.StringDebeziumDeserializationSchema;
import com.ververica.cdc.connectors.mysql.MySqlSource;

import java.util.Properties;

public class CDC01 {
    public static void main(String[] args) throws Exception{
        Properties debeziumProperties = new Properties();
        debeziumProperties.put("snapshot.locking.mode", "none");// do not use lock

        SourceFunction<String> sourceFunction = MySqlSource.<String>builder()
                .hostname("yourHostname")
                .port(3306)
                .databaseList("yourDatabaseName") // set captured database
                .tableList("yourDatabaseName.yourTableName") // set captured table
                .username("yourUsername")
                .password("yourPassword")
                .deserializer(new StringDebeziumDeserializationSchema()) // converts SourceRecord to String
                .debeziumProperties(debeziumProperties)
                .build();


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.enableCheckpointing(3000); // checkpoint every 3000 milliseconds

        env.addSource(sourceFunction)
                .print().setParallelism(1); // use parallelism 1 for sink to keep message ordering

        env.execute();

    }
}
