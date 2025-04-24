package lumen.redstone_protocol.util;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class ElevatorCooldownHandler {
    private static final WeakHashMap<PlayerEntity, Integer> cooldownMap = new WeakHashMap<>();

    /**
     * 设置玩家的冷却时间
     *
     * @param player   玩家实体
     * @param cooldown 冷却时间（tick）
     */
    public static void setCooldown(PlayerEntity player, int cooldown) {
        cooldownMap.put(player, cooldown);
    }

    /**
     * 检查玩家是否处于冷却中
     *
     * @param player 玩家实体
     * @return 若冷却时间大于 0 则返回 true，否则返回 false
     */
    public static boolean isOnCooldown(PlayerEntity player) {
        Integer remaining = cooldownMap.get(player);
        return remaining != null && remaining > 0;
    }

    /**
     * 每个 tick 调用一次以更新所有玩家的冷却时间。
     * 冷却时间归零或以下时，会自动移除对应的记录。
     */
    public static void tickCooldowns() {
        Iterator<Map.Entry<PlayerEntity, Integer>> iterator = cooldownMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<PlayerEntity, Integer> entry = iterator.next();
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                iterator.remove();
            } else {
                entry.setValue(remaining);
            }
        }
    }
}
