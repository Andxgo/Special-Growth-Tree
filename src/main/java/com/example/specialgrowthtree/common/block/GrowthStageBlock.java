package com.example.specialgrowthtree.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class GrowthStageBlock extends Block {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 5);
    private static final int MAX_STAGE = 5;

    public GrowthStageBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
            BlockHitResult hit) {
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            int currentStage = state.getValue(STAGE);
            if (currentStage >= MAX_STAGE) {
                if (player != null) {
                    player.getInventory().add(new ItemStack(Items.OAK_SAPLING, 2));
                    player.getInventory().add(new ItemStack(Items.STICK, 3));
                }
                level.destroyBlock(pos, false);
                return InteractionResult.CONSUME;
            }
            advanceGrowth(level, pos, state);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        advanceGrowth(level, pos, state);
    }

    private void advanceGrowth(Level level, BlockPos pos, BlockState state) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        int currentStage = state.getValue(STAGE);
        if (currentStage >= MAX_STAGE) {
            return;
        }

        BlockPos belowPos = pos.below();
        BlockState belowState = serverLevel.getBlockState(belowPos);
        boolean validGround = belowState.is(Blocks.DIRT) || belowState.is(Blocks.GRASS_BLOCK)
                || belowState.is(Blocks.FARMLAND);
        if (!validGround) {
            serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            return;
        }

        boolean shouldGrow = serverLevel.random.nextInt(100) < (currentStage < 3 ? 70 : 50);
        if (!shouldGrow) {
            int delayTicks = 120 + serverLevel.random.nextInt(180);
            serverLevel.scheduleTick(pos, this, delayTicks);
            return;
        }

        int nextStage = currentStage + 1;
        serverLevel.setBlock(pos, state.setValue(STAGE, nextStage), 3);
        spawnGrowthEffects(serverLevel, pos);

        if (nextStage < MAX_STAGE) {
            int delayTicks = 120 + serverLevel.random.nextInt(180);
            serverLevel.scheduleTick(pos, this, delayTicks);
        }
    }

    private void spawnGrowthEffects(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 8; i++) {
            double offsetX = (level.random.nextDouble() - 0.5D) * 0.4D;
            double offsetY = level.random.nextDouble() * 0.6D;
            double offsetZ = (level.random.nextDouble() - 0.5D) * 0.4D;
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5D + offsetX,
                    pos.getY() + 0.6D + offsetY, pos.getZ() + 0.5D + offsetZ, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
        level.playSound(null, pos, SoundEvents.PLANT_CROP_GROW, net.minecraft.sounds.SoundSource.BLOCKS, 0.5F, 1.0F);
    }
}
