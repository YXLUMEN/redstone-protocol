package lumen.redstone_protocol.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class FragmentEntity extends Entity {
    private Entity owner = null;
    private short life = 200;

    public FragmentEntity(EntityType<? extends FragmentEntity> entityType, World world) {
        super(entityType, world);
    }

    public FragmentEntity(Entity owner, World world, double x, double y, double z) {
        super(RPEntities.FRAGMENT_ENTITY, world);
        this.setPosition(x, y, z);
        this.owner = owner;
    }

    @Override
    public void tick() {
        super.tick();

        this.setVelocity(this.getVelocity().add(0, -0.05, 0));

        Vec3d velocity = this.getVelocity();
        this.setPosition(this.getPos().add(velocity));

        World world = this.getWorld();
        if (world.isClient) return;

        Box box = this.getBoundingBox();
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, box.expand(0.2), e -> true);

        for (LivingEntity entity : entities) {
            entity.timeUntilRegen = 0;
            entity.hurtTime = 0;
            entity.damage(this.getDamageSources().explosion(this, this.owner), 4.0f);
            this.discard();
            break;

        }

        if (--this.life <= 0) this.discard();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }
}
