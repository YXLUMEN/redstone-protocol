package lumen.redstone_protocol.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class BiologicalFieldBlock extends Block {
    public static final short PROTECTION_RADIUS = 6;

    public BiologicalFieldBlock(Settings settings) {
        super(settings);
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

            world.addParticle(ParticleTypes.HEART,
                    particleX, centerY, particleZ,
                    0, 0.2, 0);
        }
    }
}
