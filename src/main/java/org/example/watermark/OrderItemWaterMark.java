package org.example.watermark;

import org.apache.flink.api.common.eventtime.*;
import org.example.bean.OrderItem;

public class OrderItemWaterMark implements WatermarkStrategy<OrderItem> {
    @Override
    public TimestampAssigner<OrderItem> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
        return (element, recordTimestamp) -> System.currentTimeMillis();
    }

    @Override
    public WatermarkGenerator<OrderItem> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
        return new WatermarkGenerator<OrderItem>() {
            @Override
            public void onEvent(OrderItem orderItem, long l, WatermarkOutput output) {
                output.emitWatermark(new Watermark(System.currentTimeMillis()));
            }

            @Override
            public void onPeriodicEmit(WatermarkOutput output) {
                output.emitWatermark(new Watermark(System.currentTimeMillis()));
            }
        };
    }
}
