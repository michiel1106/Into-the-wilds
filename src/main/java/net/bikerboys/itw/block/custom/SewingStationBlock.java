package net.bikerboys.itw.block.custom;

import net.bikerboys.itw.block.entity.SewingStationBlockEntity;
import net.bikerboys.itw.screen.SewingStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SewingStationBlock extends Block {
    public static final VoxelShape SHAPE = Block.box(0,0,0, 16, 16, 16);

    public SewingStationBlock(Properties pProperties) {
        super(pProperties);
    }
    private static final Component CONTAINER_TITLE = Component.translatable("container.sewing_station");

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof SewingStationBlockEntity) {
                ((SewingStationBlockEntity) blockEntity).drops();
            }
        }


        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

//    @Override
//    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
//        if (!pLevel.isClientSide()) {
//            BlockEntity entity = pLevel.getBlockEntity(pPos);
//            if(entity instanceof SewingStationBlockEntity) {
//                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (SewingStationBlockEntity)entity, pPos);
//            } else {
//                throw new IllegalStateException("Our Container provider is missing!");
//            }
//        }
//
//
//        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
//    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));
            pPlayer.awardStat(Stats.INTERACT_WITH_STONECUTTER);
            return InteractionResult.CONSUME;
        }
    }


    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((id, inventory, player) ->
                new SewingStationMenu(id, inventory, ContainerLevelAccess.create(level, pos)),
                Component.translatable("container.sewing_station"));
    }



//   @Override
//   public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
//       return new SewingStationBlockEntity(pPos, pState);
//   }
//
//   @Override
//   public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
//       if (pLevel.isClientSide()) {
//           return null;
//       }
//
//       return createTickerHelper(pBlockEntityType, ModBlockEntities.SEWING_STATION_BE.get(), (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
//   }
}
