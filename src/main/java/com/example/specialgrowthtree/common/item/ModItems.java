package com.example.specialgrowthtree.common.item;

import com.example.specialgrowthtree.SpecialGrowthTreeMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            SpecialGrowthTreeMod.MODID);

    public static final RegistryObject<Item> SPECIAL_SEED = ITEMS.register("special_seed",
            () -> new SpecialSeedItem(new Item.Properties().stacksTo(16)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
