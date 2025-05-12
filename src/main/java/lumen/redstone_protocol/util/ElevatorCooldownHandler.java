package lumen.redstone_protocol.util;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ElevatorCooldownHandler {
    private static final Map<UUID, Integer> cooldownMap = new ConcurrentHashMap<>();

    public static void setCooldown(PlayerEntity player, int cooldown) {
        cooldownMap.put(player.getUuid(), cooldown);
    }

    public static boolean isOnCooldown(PlayerEntity player) {
        Integer remaining = cooldownMap.get(player.getUuid());
        return remaining != null && remaining > 0;
    }

    public static void tickCooldowns() {
        Iterator<Map.Entry<UUID, Integer>> iterator = cooldownMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                iterator.remove();
            } else {
                entry.setValue(remaining);
            }
        }
    }
}
