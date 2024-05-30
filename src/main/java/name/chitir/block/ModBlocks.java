package name.chitir.block;

import name.chitir.ChitirChicken;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.minecraft.block.Blocks.BLUE_GLAZED_TERRACOTTA;

public class ModBlocks {
    public static final Block DEEPFRYER = registerBlock("deepfryer",
            new DeepFryer(FabricBlockSettings.copyOf(BLUE_GLAZED_TERRACOTTA)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(ChitirChicken.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(ChitirChicken.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        ChitirChicken.LOGGER.info("Registering ModBlocks for " + ChitirChicken.MOD_ID);
    }
}