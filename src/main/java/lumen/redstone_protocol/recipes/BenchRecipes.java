package lumen.redstone_protocol.recipes;

import lumen.redstone_protocol.block.RPBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class BenchRecipes extends FabricRecipeProvider {
    public BenchRecipes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, RPBlocks.LOW_FRICTION_GLASS, 8)
                .pattern("GGG")
                .pattern("GBG")
                .pattern("GGG")
                .input('G', Items.GLASS)
                .input('B', Items.BLUE_ICE)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.LOW_FRICTION_GLASS), FabricRecipeProvider.conditionsFromItem(RPBlocks.LOW_FRICTION_GLASS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, RPBlocks.IDEAL_ORBIT, 4)
                .pattern("BBB")
                .pattern("BIB")
                .pattern("BBB")
                .input('I', Items.IRON_BLOCK)
                .input('B', Items.BLUE_ICE)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.IDEAL_ORBIT), FabricRecipeProvider.conditionsFromItem(RPBlocks.IDEAL_ORBIT))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.PLAYER_TRANSPARENT_BLOCK, 4)
                .pattern("G#G")
                .pattern("GFG")
                .pattern("GGG")
                .input('G', Items.GLASS)
                .input('F', ItemTags.FENCE_GATES)
                .input('#', Items.ARMOR_STAND)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.PLAYER_TRANSPARENT_BLOCK), FabricRecipeProvider.conditionsFromItem(RPBlocks.PLAYER_TRANSPARENT_BLOCK))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.MOB_TRANSPARENT_BLOCK, 4)
                .pattern("G#G")
                .pattern("GFG")
                .pattern("GGG")
                .input('G', Items.GLASS)
                .input('F', ItemTags.FENCE_GATES)
                .input('#', Items.BONE)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.MOB_TRANSPARENT_BLOCK), FabricRecipeProvider.conditionsFromItem(RPBlocks.MOB_TRANSPARENT_BLOCK))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.ITEM_TRANSPARENT_BLOCK, 4)
                .pattern("G#G")
                .pattern("GFG")
                .pattern("GGG")
                .input('G', Items.GLASS)
                .input('F', ItemTags.FENCE_GATES)
                .input('#', Items.HOPPER)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.ITEM_TRANSPARENT_BLOCK), FabricRecipeProvider.conditionsFromItem(RPBlocks.ITEM_TRANSPARENT_BLOCK))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.CONTROLLABLE_TRANSPARENT_BLOCK, 4)
                .pattern("G#G")
                .pattern("GFG")
                .pattern("GGG")
                .input('G', Items.GLASS)
                .input('F', ItemTags.FENCE_GATES)
                .input('#', Items.REDSTONE)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.CONTROLLABLE_TRANSPARENT_BLOCK), FabricRecipeProvider.conditionsFromItem(RPBlocks.CONTROLLABLE_TRANSPARENT_BLOCK))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, RPBlocks.CONTROLLABLE_TRANSPARENT_STONE, 1)
                .input(Items.SMOOTH_STONE)
                .input(RPBlocks.CONTROLLABLE_TRANSPARENT_BLOCK)
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.CONTROLLABLE_TRANSPARENT_STONE), FabricRecipeProvider.conditionsFromItem(RPBlocks.CONTROLLABLE_TRANSPARENT_STONE))
                .offerTo(recipeExporter);

        // 反转通过
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.REVERSE_CONTROLLABLE_TRANSPARENT_BLOCK, 4)
                .pattern("G#G")
                .pattern("GFG")
                .pattern("GRG")
                .input('G', Items.GLASS)
                .input('F', ItemTags.FENCE_GATES)
                .input('#', Items.REDSTONE)
                .input('R', Items.REDSTONE_TORCH)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.REVERSE_CONTROLLABLE_TRANSPARENT_BLOCK), FabricRecipeProvider.conditionsFromItem(RPBlocks.REVERSE_CONTROLLABLE_TRANSPARENT_BLOCK))
                .offerTo(recipeExporter);

        // 初级加速器
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.PRIMARY_SPEED_MULTIPLIER, 6)
                .pattern("III")
                .pattern("GGG")
                .input('G', Items.GLASS)
                .input('I', Items.ICE)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.PRIMARY_SPEED_MULTIPLIER), FabricRecipeProvider.conditionsFromItem(RPBlocks.PRIMARY_SPEED_MULTIPLIER))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.PRIMARY_VERTICAL_BOOSTER, 4)
                .pattern("$#$")
                .pattern("$ $")
                .pattern("$#$")
                .input('#', RPBlocks.PRIMARY_SPEED_MULTIPLIER)
                .input('$', RPBlocks.PRIMARY_HORIZONTAL_BOOSTER)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.PRIMARY_VERTICAL_BOOSTER), FabricRecipeProvider.conditionsFromItem(RPBlocks.PRIMARY_VERTICAL_BOOSTER))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.PRIMARY_HORIZONTAL_BOOSTER, 1)
                .input(RPBlocks.PRIMARY_SPEED_MULTIPLIER)
                .input(Items.POWERED_RAIL)
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.PRIMARY_HORIZONTAL_BOOSTER), FabricRecipeProvider.conditionsFromItem(RPBlocks.PRIMARY_HORIZONTAL_BOOSTER))
                .offerTo(recipeExporter);


        // 中级加速器
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.INTERMEDIATE_SPEED_MULTIPLIER, 1)
                .pattern(" I ")
                .pattern("I#I")
                .pattern(" I ")
                .input('#', RPBlocks.PRIMARY_SPEED_MULTIPLIER)
                .input('I', Items.ICE)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.INTERMEDIATE_SPEED_MULTIPLIER), FabricRecipeProvider.conditionsFromItem(RPBlocks.INTERMEDIATE_SPEED_MULTIPLIER))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.INTERMEDIATE_VERTICAL_BOOSTER, 4)
                .pattern("$#$")
                .pattern("$ $")
                .pattern("$#$")
                .input('#', RPBlocks.INTERMEDIATE_SPEED_MULTIPLIER)
                .input('$', RPBlocks.INTERMEDIATE_HORIZONTAL_BOOSTER)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.INTERMEDIATE_VERTICAL_BOOSTER), FabricRecipeProvider.conditionsFromItem(RPBlocks.INTERMEDIATE_VERTICAL_BOOSTER))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.INTERMEDIATE_HORIZONTAL_BOOSTER, 1)
                .input(RPBlocks.INTERMEDIATE_SPEED_MULTIPLIER)
                .input(Items.POWERED_RAIL)
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.INTERMEDIATE_HORIZONTAL_BOOSTER), FabricRecipeProvider.conditionsFromItem(RPBlocks.INTERMEDIATE_HORIZONTAL_BOOSTER))
                .offerTo(recipeExporter);

        // 高级加速器
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.ADVANCED_SPEED_MULTIPLIER, 1)
                .pattern(" I ")
                .pattern("I#I")
                .pattern(" I ")
                .input('#', RPBlocks.INTERMEDIATE_SPEED_MULTIPLIER)
                .input('I', Items.ICE)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.ADVANCED_SPEED_MULTIPLIER), FabricRecipeProvider.conditionsFromItem(RPBlocks.ADVANCED_SPEED_MULTIPLIER))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.ADVANCED_VERTICAL_BOOSTER, 4)
                .pattern("$#$")
                .pattern("$ $")
                .pattern("$#$")
                .input('#', RPBlocks.ADVANCED_SPEED_MULTIPLIER)
                .input('$', RPBlocks.ADVANCED_HORIZONTAL_BOOSTER)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.ADVANCED_VERTICAL_BOOSTER), FabricRecipeProvider.conditionsFromItem(RPBlocks.ADVANCED_VERTICAL_BOOSTER))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.ADVANCED_HORIZONTAL_BOOSTER, 1)
                .input(RPBlocks.ADVANCED_SPEED_MULTIPLIER)
                .input(Items.POWERED_RAIL)
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.ADVANCED_HORIZONTAL_BOOSTER), FabricRecipeProvider.conditionsFromItem(RPBlocks.ADVANCED_HORIZONTAL_BOOSTER))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.UNLIMITED_BOOSTER, 4)
                .pattern(" R ")
                .pattern("RPR")
                .pattern(" R ")
                .input('P', RPBlocks.PRIMARY_HORIZONTAL_BOOSTER)
                .input('R', Items.REDSTONE)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.UNLIMITED_BOOSTER), FabricRecipeProvider.conditionsFromItem(RPBlocks.UNLIMITED_BOOSTER))
                .offerTo(recipeExporter);

        // 物品封包机
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.ITEM_PACKER, 12)
                .pattern("PPP")
                .pattern("D#D")
                .pattern("SSS")
                .input('S', Items.SMOOTH_STONE)
                .input('D', Items.DISPENSER)
                .input('#', Items.HOPPER)
                .input('P', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.ITEM_PACKER), FabricRecipeProvider.conditionsFromItem(RPBlocks.ITEM_PACKER))
                .offerTo(recipeExporter);

        // 物品解包机
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.ITEM_UNPACKER, 12)
                .pattern("PPP")
                .pattern("D#D")
                .pattern("SSS")
                .input('S', Items.SMOOTH_STONE)
                .input('D', Items.DISPENSER)
                .input('#', Items.PISTON)
                .input('P', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.ITEM_UNPACKER), FabricRecipeProvider.conditionsFromItem(RPBlocks.ITEM_UNPACKER))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.ITEM_CALIBRATOR, 12)
                .pattern("SSS")
                .pattern("P P")
                .pattern("SSS")
                .input('S', Items.SMOOTH_STONE)
                .input('P', Items.PISTON)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.ITEM_CALIBRATOR), FabricRecipeProvider.conditionsFromItem(RPBlocks.ITEM_CALIBRATOR))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.ITEM_STEERING_GEAR, 12)
                .pattern("SSS")
                .pattern("PRP")
                .pattern("SSS")
                .input('S', Items.SMOOTH_STONE)
                .input('P', Items.PISTON)
                .input('R', Items.POWERED_RAIL)
                .group("multi_bench")
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.ITEM_STEERING_GEAR), FabricRecipeProvider.conditionsFromItem(RPBlocks.ITEM_STEERING_GEAR))
                .offerTo(recipeExporter);

        // 跳跃增强器
        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, RPBlocks.JUMP_ENHANCER, 4)
                .input(Items.SLIME_BLOCK)
                .input(Items.PISTON)
                .criterion(FabricRecipeProvider.hasItem(RPBlocks.JUMP_ENHANCER), FabricRecipeProvider.conditionsFromItem(RPBlocks.JUMP_ENHANCER))
                .offerTo(recipeExporter);
    }
}
