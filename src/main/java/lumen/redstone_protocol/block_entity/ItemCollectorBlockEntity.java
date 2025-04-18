package lumen.redstone_protocol.block_entity;

import lumen.redstone_protocol.screen_handler.ItemCollectorScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class ItemCollectorBlockEntity extends LootableContainerBlockEntity {
    private static final int DETECTION_RADIUS = 3;
    public static final int TRANSFER_COOLDOWN = 12;

    private final Box detectionBox = new Box(pos).expand(DETECTION_RADIUS);

    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
    private int transferCooldown = -1;

    public ItemCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(RPBlockEntities.ITEM_COLLECTOR_BLOCK_ENTITY_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory, registryLookup);
        this.transferCooldown = nbt.getInt("TransferCooldown");
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
        nbt.putInt("TransferCooldown", this.transferCooldown);
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        this.generateLoot(null);
        return Inventories.splitStack(this.getHeldStacks(), slot, amount);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.generateLoot(null);
        this.getHeldStacks().set(slot, stack);
        stack.capCount(this.getMaxCount(stack));
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.item_collector");
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, ItemCollectorBlockEntity blockEntity) {
        blockEntity.transferCooldown--;
        if (blockEntity.isFull() || blockEntity.needsCooldown()) return;
        blockEntity.setTransferCooldown();

        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, blockEntity.detectionBox, item -> true);
        collectItems(blockEntity, items);

        markDirty(world, pos, state);
    }

    private boolean isFull() {
        for (ItemStack itemStack : this.inventory) {
            if (itemStack.isEmpty() || itemStack.getCount() != itemStack.getMaxCount()) {
                return false;
            }
        }

        return true;
    }

    private static boolean canMergeItems(ItemStack first, ItemStack second) {
        return first.getCount() <= first.getMaxCount() && ItemStack.areItemsAndComponentsEqual(first, second);
    }

    public static ItemStack insertItemStack(Inventory to, ItemStack stack) {
        int j = to.size();

        for (int i = 0; i < j && !stack.isEmpty(); i++) {
            stack = insertItemStack(to, stack, i);
        }
        return stack;
    }

    private static ItemStack insertItemStack(Inventory to, ItemStack stack, int slot) {
        ItemStack itemStack = to.getStack(slot);
        if (itemStack.isEmpty()) {
            to.setStack(slot, stack);
            return ItemStack.EMPTY;
        } else if (canMergeItems(stack, itemStack)) {
            int available = itemStack.getMaxCount() - itemStack.getCount();
            int transfer = Math.min(stack.getCount(), available);
            itemStack.increment(transfer);
            stack.decrement(transfer);
        }
        return stack;
    }

    private static void collectItems(ItemCollectorBlockEntity blockEntity, List<ItemEntity> items) {
        for (ItemEntity itemEntity : items) {
            ItemStack itemStack = itemEntity.getStack();
            ItemStack remaining = insertItemStack(blockEntity, itemStack.copy());

            if (remaining.isEmpty()) {
                itemEntity.discard();
            } else {
                itemEntity.setStack(remaining);
            }
        }
    }

    private boolean needsCooldown() {
        return this.transferCooldown > 0;
    }

    private void setTransferCooldown() {
        this.transferCooldown = TRANSFER_COOLDOWN;
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new ItemCollectorScreenHandler(syncId, playerInventory, this);
    }
}