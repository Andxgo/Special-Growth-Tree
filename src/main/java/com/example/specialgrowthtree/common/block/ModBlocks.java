package com.example.specialgrowthtree.common.block;

import com.example.specialgrowthtree.SpecialGrowthTreeMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            SpecialGrowthTreeMod.MODID);

    public static final RegistryObject<Block> GROWTH_STAGE_BLOCK = BLOCKS.register("growth_stage_block",
            () -> new GrowthStageBlock(BlockBehaviour.Properties.of().strength(1.0f)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
