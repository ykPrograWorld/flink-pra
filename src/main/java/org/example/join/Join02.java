package org.example.join;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.example.utils.FlinkUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * window join 滚动窗口
 */

public class Join02 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = FlinkUtils.getStreamEnv();

//        商品数据流
        DataStreamSource<Goods> goodsDs = env.addSource(new GoodsSource());
//        订单数据流
        DataStreamSource<OrderItem> orderSource = env.addSource(new OrderSource());

        SingleOutputStreamOperator<Goods> goodsDsWithWatermark = goodsDs.assignTimestampsAndWatermarks(new GoodsWaterMark());
        SingleOutputStreamOperator<OrderItem> orderItemDsWithWatermark = orderSource.assignTimestampsAndWatermarks(new OrderItemWaterMark());

        DataStream<FactOrderItem> resultDS = goodsDsWithWatermark.join(orderItemDsWithWatermark)
                .where(Goods::getGoodsId)
                .equalTo(OrderItem::getGoodsID)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .apply(new JoinFunction<Goods, OrderItem, FactOrderItem>() {
                    @Override
                    public FactOrderItem join(Goods goods, OrderItem orderItem) throws Exception {
                        FactOrderItem result = new FactOrderItem();
                        result.setGoodsId(goods.getGoodsId());
                        result.setGoodsName(goods.getGoodsName());
                        result.setCount(new BigDecimal(orderItem.count));
                        result.setTotalMoney(new BigDecimal(orderItem.getCount()).multiply(goods.getGoodsPrice()));
                        return result;
                    }
                });
        resultDS.print();

        env.execute("window join");


    }

    //    商品类  (商品id,商品名称,商品价格)
    public static class Goods {
        private String goodsId;
        private String goodsName;
        private BigDecimal goodsPrice;
        public static List<Goods> goods_List;
        public static Random r;

        public Goods() {

        }

        public Goods(String goodsId, String goodsName, BigDecimal goodsPrice) {
            this.goodsId = goodsId;
            this.goodsName = goodsName;
            this.goodsPrice = goodsPrice;
        }


        static {
            r = new Random();
            goods_List = new ArrayList<>();
            goods_List.add(new Goods("1", "小米12", new BigDecimal(4890)));
            goods_List.add(new Goods("2", "iphone12", new BigDecimal(3890)));
            goods_List.add(new Goods("3", "macbookpro", new BigDecimal(5890)));
            goods_List.add(new Goods("4", "thinkpadx1", new BigDecimal(6890)));
            goods_List.add(new Goods("5", "meizu", new BigDecimal(8890)));
            goods_List.add(new Goods("6", "mate40", new BigDecimal(9890)));
        }

        public static Goods randomGoods() {
            int rIndex = r.nextInt(goods_List.size());
            return goods_List.get(rIndex);
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }


        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public void setGoodsPrice(BigDecimal goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public static void setGoods_List(List<Goods> goods_List) {
            Goods.goods_List = goods_List;
        }

        public static void setR(Random r) {
            Goods.r = r;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public BigDecimal getGoodsPrice() {
            return goodsPrice;
        }

        public static List<Goods> getGoods_List() {
            return goods_List;
        }

        public static Random getR() {
            return r;
        }
    }


    //订单明细类
    public static class OrderItem {
        private String itemId;
        private String goodsID;
        private Integer count;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public void setGoodsID(String goodsID) {
            this.goodsID = goodsID;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getItemId() {
            return itemId;
        }

        public String getGoodsID() {
            return goodsID;
        }

        public Integer getCount() {
            return count;
        }
    }


    //关联结果
    public static class FactOrderItem {
        private String goodsId;
        private String goodsName;
        private BigDecimal count;
        private BigDecimal totalMoney;

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public void setCount(BigDecimal count) {
            this.count = count;
        }

        public void setTotalMoney(BigDecimal totalMoney) {
            this.totalMoney = totalMoney;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public BigDecimal getCount() {
            return count;
        }

        public BigDecimal getTotalMoney() {
            return totalMoney;
        }
    }

    //实时生成商品数据流
    public static class GoodsSource extends RichSourceFunction<Goods> {
        private Boolean isCancel;

        @Override
        public void open(Configuration parameters) throws Exception {
            isCancel = false;
        }

        @Override
        public void run(SourceContext<Goods> sourceContext) throws Exception {
            while (!isCancel) {
                Goods.goods_List.stream().forEach(goods -> sourceContext.collect(goods));
                TimeUnit.SECONDS.sleep(1);
            }
        }

        @Override
        public void cancel() {
            isCancel = true;
        }
    }

    //实时生成订单数据流
    public static class OrderSource extends RichSourceFunction<OrderItem> {
        private boolean iscancel;
        private Random r;

        @Override
        public void open(Configuration parameters) throws Exception {
            iscancel = false;
            r = new Random();
        }

        @Override
        public void run(SourceContext<OrderItem> sourceContext) throws Exception {
            while (!iscancel) {
                Goods goods = Goods.randomGoods();
                OrderItem orderItem = new OrderItem();
                orderItem.setGoodsID(goods.getGoodsId());
                orderItem.setCount(r.nextInt(10) + 1);
                orderItem.setItemId(UUID.randomUUID().toString());

                sourceContext.collect(orderItem);
                orderItem.setGoodsID("111");
                sourceContext.collect(orderItem);
                TimeUnit.SECONDS.sleep(1);
            }
        }

        @Override
        public void cancel() {
            iscancel = true;
        }
    }

    //构建水印分配器
    public static class GoodsWaterMark implements WatermarkStrategy<Goods> {
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


    public static class OrderItemWaterMark implements WatermarkStrategy<OrderItem> {
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
}
