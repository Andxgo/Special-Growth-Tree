package com.example.specialgrowthtree;

import com.example.specialgrowthtree.common.block.ModBlocks;
import com.example.specialgrowthtree.common.item.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SpecialGrowthTreeMod.MODID)
public class SpecialGrowthTreeMod {
    public static final String MODID = "special_growth_tree";

    public SpecialGrowthTreeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
    }
}
