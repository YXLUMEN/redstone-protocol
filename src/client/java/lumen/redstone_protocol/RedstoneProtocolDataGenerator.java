package lumen.redstone_protocol;

import lumen.redstone_protocol.loot_tables.BlockLootTables;
import lumen.redstone_protocol.recipes.BenchRecipes;
import lumen.redstone_protocol.tags.VanillaTags;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class RedstoneProtocolDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(VanillaTags::new);
        pack.addProvider(BlockLootTables::new);
        pack.addProvider(BenchRecipes::new);
    }
}
