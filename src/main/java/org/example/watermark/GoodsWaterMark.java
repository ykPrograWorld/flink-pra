package org.example.watermark;

import org.apache.flink.api.common.eventtime.*;
import org.example.bean.Goods;

//构建水印分配器
public class GoodsWaterMark implements WatermarkStrategy<Goods> {
    @Override
    public TimestampAssigner<Goods> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
        return (element, recordTimestamp) -> System.currentTimeMillis();
    }

    @Override
    public WatermarkGenerator<Goods> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
        return new WatermarkGenerator<Goods>() {
            @Override
            public void onEvent(Goods goods, long l, WatermarkOutput output) {
                output.emitWatermark(new Watermark(System.currentTimeMillis()));
            }

            @Override
            public void onPeriodicEmit(WatermarkOutput output) {
                output.emitWatermark(new Watermark(System.currentTimeMillis()));
            }
        };
    }
}
