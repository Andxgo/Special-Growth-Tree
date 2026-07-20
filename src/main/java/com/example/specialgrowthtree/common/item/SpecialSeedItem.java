package com.example.specialgrowthtree.common.item;

import com.example.specialgrowthtree.common.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SpecialSeedItem extends Item {
    public SpecialSeedItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.sidedSuccess(true);
        }

        BlockPos placementPos = context.getClickedPos().relative(context.getClickedFace());
        BlockState existingState = level.getBlockState(placementPos);
        BlockState groundState = level.getBlockState(placementPos.below());

        if (!existingState.canBeReplaced()) {
            return InteractionResult.PASS;
        }

        if (!groundState.is(net.minecraft.world.level.block.Blocks.DIRT)
                && !groundState.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)
                && !groundState.is(net.minecraft.world.level.block.Blocks.FARMLAND)) {
            return InteractionResult.PASS;
        }

        BlockState placedState = ModBlocks.GROWTH_STAGE_BLOCK.get().defaultBlockState();
        level.setBlock(placementPos, placedState, 3);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.scheduleTick(placementPos, ModBlocks.GROWTH_STAGE_BLOCK.get(), 100);
        }

        if (context.getPlayer() != null && !context.getPlayer().isCreative()) {
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}
