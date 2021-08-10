package org.example.source;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.example.bean.Goods;

import java.util.concurrent.TimeUnit;

//实时生成商品数据流
public class GoodsSource extends RichSourceFunction<Goods> {
    private Boolean isCancel;

    @Override
    public void open(Configuration parameters) throws Exception {
        isCancel = false;
    }

    @Override
    public void run(SourceContext<Goods> sourceContext) throws Exception {
        while (!isCancel) {
//        for (int i = 0; i < 10; i++) {
            Goods.goods_List.stream().forEach(goods -> sourceContext.collect(goods));
            TimeUnit.SECONDS.sleep(1);
//        }
        }
    }

    @Override
    public void cancel() {
        isCancel = true;
    }
}
