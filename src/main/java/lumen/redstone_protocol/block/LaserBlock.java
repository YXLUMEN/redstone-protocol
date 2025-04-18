package lumen.redstone_protocol.block;

import lumen.redstone_protocol.RPProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LaserBlock extends PillarBlock {
    public static final IntProperty LASER_MODE = RPProperties.LASER_MODE;

    protected static final VoxelShape Y_SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    protected static final VoxelShape Z_SHAPE = Block.createCuboidShape(4.0, 4.0, 0.0, 12.0, 12.0, 16.0);
    protected static final VoxelShape X_SHAPE = Block.createCuboidShape(0.0, 4.0, 4.0, 16.0, 12.0, 12.0);

    public LaserBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AXIS, Direction.Axis.Y));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AXIS)) {
            case Z -> Z_SHAPE;
            case Y -> Y_SHAPE;
            default -> X_SHAPE;
        };
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        int mode = state.get(LASER_MODE);
        applyLaserEffect(mode, world, entity);
        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        final double spread = 0.1d;

        world.addParticle(getLaserParticle(state.get(LASER_MODE)),
                pos.getX() + 0.5,
                pos.getY() + 0.6,
                pos.getZ() + 0.5,
                random.nextDouble() * spread, 0.06, random.nextDouble() * spread
        );
    }

    private static ParticleEffect getLaserParticle(int mode) {
        return switch (mode) {
            case 1 -> ParticleTypes.END_ROD;
            case 2 -> ParticleTypes.FLAME;
            case 3 -> ParticleTypes.PORTAL;
            case 4 -> ParticleTypes.WHITE_SMOKE;
            default -> ParticleTypes.CRIT;
        };
    }

    private static void applyLaserEffect(int mode, World world, Entity entity) {
        switch (mode) {
            case 0 -> {
                if ((entity instanceof LivingEntity livingEntity)) {
                    livingEntity.damage(world.getDamageSources().generic(), 3f);
                }
            }
            case 1 -> entity.damage(world.getDamageSources().generic(), 3f);
            case 2 -> {
                int fireTicks = Math.max(entity.getFireTicks(), 10);
                if (fireTicks <= 120) entity.setOnFireForTicks(fireTicks + 20);
            }
            case 3 -> entity.damage(world.getDamageSources().outOfWorld(), 3f);
            case 4 -> {
                if ((entity instanceof LivingEntity livingEntity)) {
                    livingEntity.setHealth(livingEntity.getHealth() - livingEntity.getMaxHealth() * 0.1f);
                    if (livingEntity.isDead()) {
                        livingEntity.onDeath(world.getDamageSources().outsideBorder());
                        livingEntity.playSoundIfNotSilent(SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT);
                    }
                }
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(AXIS, ctx.getSide().getAxis()).with(LASER_MODE, 0);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LASER_MODE).add(AXIS);
    }
}
