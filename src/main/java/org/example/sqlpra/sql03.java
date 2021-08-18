package org.example.sqlpra;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.example.bean.Goods;
import org.example.source.GoodsSource;

import java.util.ArrayList;

/**
 * flink blink 流表
 */
public class sql03 {
    public static void main(String[] args) throws Exception {
//        创建流执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        创建sql解析引擎设置
        EnvironmentSettings blSet = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
//        获取流表执行环境
        StreamTableEnvironment blTableEnv = StreamTableEnvironment.create(env, blSet);

        ArrayList<String> strList = new ArrayList<>();
        strList.add("1");
        strList.add("2");
        strList.add("3");
        strList.add("4");
        strList.add("5");
        strList.add("6");

//      获取流
        DataStreamSource<String> streamSour = env.fromCollection(strList);
//      流转表
        Table table = blTableEnv.fromDataStream(streamSour);
//      打印表结构
        table.printSchema();
//      创建临时表视图
        blTableEnv.createTemporaryView("num",table);
//      执行sql语句
        Table tableQ = blTableEnv.sqlQuery("select * from num");
//      表转流输出
       blTableEnv.toRetractStream(tableQ, TypeInformation.of(new TypeHint<Tuple1<String>>() {
       })).print();
//       blTableEnv.toRetractStream(tableQ, Row.class).print();

       env.execute("flink sql");
    }
}
