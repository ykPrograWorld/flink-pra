package org.example.state;

import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;

import javax.swing.plaf.nimbus.State;

/**
 * state有效期
 */
public class State02 {
    public static void main(String[] args) {
        StateTtlConfig ttlConfig = StateTtlConfig
                .newBuilder(Time.seconds(1))
                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                .build();
        ValueStateDescriptor<String> stateDescriptor = new ValueStateDescriptor<>("text state", String.class);
        stateDescriptor.enableTimeToLive(ttlConfig);
    }
}
