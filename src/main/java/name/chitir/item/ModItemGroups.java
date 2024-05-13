package name.chitir.item;

import name.chitir.ChitirChicken;
import name.chitir.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup CTR_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(ChitirChicken.MOD_ID, "chicken"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.chicken"))
                    .icon(() -> new ItemStack(ModItems.FRIED_CHICKEN)).entries(((displayContext, entries) -> {
                        entries.add(ModItems.CTRBUCKET);
                        entries.add(ModItems.FRIED_CHICKEN);
                        entries.add(ModItems.CTRBURGER);
                        entries.add(ModItems.CTRSPECIAL);
                        entries.add(ModBlocks.DEEPFRYER);
                    })).build());
    public static void registerItemGroups() {
        ChitirChicken.LOGGER.info("Registering Item Groups for" + ChitirChicken.MOD_ID);
    }
}
