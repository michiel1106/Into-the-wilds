package net.bikerboys.itw.block.entity;

import net.bikerboys.itw.screen.custom.SewingStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SewingStationBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    private final ContainerData datas = new SimpleContainerData(2);

    public ContainerData getContainerData() {
        return datas;
    }


    public SewingStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityType.BARREL, pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SewingStationBlockEntity.this.progress;
                    case 1 -> SewingStationBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SewingStationBlockEntity.this.progress = pValue;
                    case 1 -> SewingStationBlockEntity.this.maxProgress = pValue;

                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {

        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition,  inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.itw.sewing_station");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        Level level = pPlayer.level();
        BlockPos pos = this.getBlockPos(); // Ensure you have a method to get the block's position
        SewingStationBlockEntity blockEntity = (SewingStationBlockEntity) level.getBlockEntity(pos);
        ContainerLevelAccess access = ContainerLevelAccess.create(level, pos);

        if (blockEntity == null) {
            throw new IllegalStateException("Block entity is null at " + pos);
        }

        return new SewingStationMenu(pContainerId, pPlayerInventory, access);
    }


    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("sewing_station.progress", progress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("sewing_station.progress");
    }


    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {

        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        //this.itemHandler.setStackInSlot(OUTPUT_SLOT,new ItemStack(result.getItem(), this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
        
    }

    private boolean hasRecipe() {


        return  true;
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
        
    }


}
