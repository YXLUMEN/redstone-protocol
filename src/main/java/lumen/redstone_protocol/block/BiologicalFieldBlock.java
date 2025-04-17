package lumen.redstone_protocol.block;

import lumen.util.BiologicalField;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static lumen.util.BiologicalField.PROTECTION_RADIUS;

public class BiologicalFieldBlock extends Block {
    public BiologicalFieldBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        BiologicalField.addFieldPos(pos);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BiologicalField.removeFieldPos(pos);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.2;
        double centerZ = pos.getZ() + 0.5;

        int numParticles = 48;
        for (int i = 0; i < numParticles; i++) {
            double angle = 2 * Math.PI * i / numParticles;
            double offsetX = Math.cos(angle) * PROTECTION_RADIUS;
            double offsetZ = Math.sin(angle) * PROTECTION_RADIUS;

            double particleX = centerX + offsetX;
            double particleZ = centerZ + offsetZ;

            world.addParticle(ParticleTypes.END_ROD,
                    particleX, centerY, particleZ,
                    0, 0.2, 0);
        }
    }
}
