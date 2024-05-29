package name.chitir.item;

import name.chitir.ChitirChicken;
import name.chitir.item.custom.ChitirChickenSpecial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item CTRBUCKET = registerItem("ctrbucket", new Item(new FabricItemSettings().food(ModFoodComponents.CTRBUCKET)));
    public static final Item FRIED_CHICKEN = registerItem("friedchicken", new Item(new FabricItemSettings().food(ModFoodComponents.FRIED_CHICKEN)));
    public static final Item CTRBURGER = registerItem("ctrburger", new Item(new FabricItemSettings().food(ModFoodComponents.CTRBURGER)));
    public static final Item CTRSPECIAL = registerItem("ctrpotion", new ChitirChickenSpecial(new FabricItemSettings().food(ModFoodComponents.CTRSPECIAL)));

    private static void addItemsToIngredientTabItemGroup(FabricItemGroupEntries entries) {
        entries.add(CTRBUCKET);
        entries.add(FRIED_CHICKEN);
        entries.add(CTRSPECIAL);
        entries.add(CTRBURGER);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(ChitirChicken.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ChitirChicken.LOGGER.info("Registering Mod Items for " + ChitirChicken.MOD_ID);
    }
}