package lumen.util;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BiologicalFieldState extends PersistentState {
    private static final String ID = "biological_field_data";
    private final Set<BlockPos> fieldPositions = ConcurrentHashMap.newKeySet();
    private static final PersistentState.Type<BiologicalFieldState> type = getPersistentStateType();

    public static PersistentState.Type<BiologicalFieldState> getPersistentStateType() {
        return new PersistentState.Type<>(BiologicalFieldState::new, BiologicalFieldState::fromNbt, DataFixTypes.SAVED_DATA_STRUCTURE_FEATURE_INDICES);
    }

    public static BiologicalFieldState fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        BiologicalFieldState state = new BiologicalFieldState();

        NbtList list = nbt.getList("Fields", 10);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound posNbt = list.getCompound(i);
            int x = posNbt.getInt("x");
            int y = posNbt.getInt("y");
            int z = posNbt.getInt("z");
            state.fieldPositions.add(new BlockPos(x, y, z));
        }
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList list = new NbtList();
        for (BlockPos pos : fieldPositions) {
            NbtCompound posNbt = new NbtCompound();
            posNbt.putInt("x", pos.getX());
            posNbt.putInt("y", pos.getY());
            posNbt.putInt("z", pos.getZ());
            list.add(posNbt);
        }
        nbt.put("Fields", list);
        return nbt;
    }

    public static BiologicalFieldState getServerState(MinecraftServer server) {
        ServerWorld world = server.getWorld(World.OVERWORLD);
        if (world == null) return null;

        PersistentStateManager persistentStateManager = world.getPersistentStateManager();

        BiologicalFieldState state = persistentStateManager.getOrCreate(type, BiologicalFieldState.ID);

        state.markDirty();

        return state;
    }

    public Set<BlockPos> getFieldPositions() {
        return fieldPositions;
    }

    public void addFieldPos(BlockPos pos) {
        fieldPositions.add(pos);
        markDirty();
    }

    public void removeFieldPos(BlockPos pos) {
        fieldPositions.remove(pos);
        markDirty();
    }
}
