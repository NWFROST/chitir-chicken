package name.chitir.item;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;

public class ModFoodComponents {
    public static final FoodComponent FRIED_CHICKEN = new FoodComponent.Builder().hunger(4).saturationModifier(0.25f)
            .build();
    public static final FoodComponent CTRBUCKET = new FoodComponent.Builder().hunger(14).saturationModifier(0.25f)
            .build();
    public static final FoodComponent CTRBURGER = new FoodComponent.Builder().hunger(7).saturationModifier(0.25f)
            .build();
    public static final FoodComponent CTRSPECIAL = new FoodComponent.Builder().hunger(0).saturationModifier(0)
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 1), 100)
            .statusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 120, 2), 100)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 300, 1), 100)
            .build();
}
