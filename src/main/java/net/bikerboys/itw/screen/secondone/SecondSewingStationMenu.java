package net.bikerboys.itw.screen.secondone;

import com.google.common.collect.Lists;
import net.bikerboys.itw.block.ModBlocks;
import net.bikerboys.itw.recipes.ModRecipes;
import net.bikerboys.itw.recipes.SecondSewingRecipe;
import net.bikerboys.itw.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
public class SecondSewingStationMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;

    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    private final Level level;
    private List<SecondSewingRecipe> recipes = Lists.newArrayList();

    private ItemStack input = ItemStack.EMPTY;

    long lastSoundTime;
    final Slot inputSlot;
    /** The inventory slot that stores the output of the crafting recipe. */
    final Slot resultSlot;
    Runnable slotUpdateListener = () -> {
    };
    public final Container container = new SimpleContainer(1) {

        public void setChanged() {
            super.setChanged();
            SecondSewingStationMenu.this.slotsChanged(this);
            SecondSewingStationMenu.this.slotUpdateListener.run();
        }
    };
    /** The inventory that stores the output of the crafting recipe. */
    final ResultContainer resultContainer = new ResultContainer();

    public SecondSewingStationMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf data) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    public SecondSewingStationMenu(int pContainerId, Inventory pPlayerInventory, final ContainerLevelAccess pAccess) {
        super(ModMenuTypes.SECOND_SEWING_STATION_MENU.get(), pContainerId);
        this.access = pAccess;
        this.level = pPlayerInventory.player.level();
        this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean mayPlace(ItemStack p_40362_) {
                return false;
            }

            public void onTake(Player p_150672_, ItemStack p_150673_) {
                p_150673_.onCraftedBy(p_150672_.level(), p_150672_, p_150673_.getCount());
                SecondSewingStationMenu.this.resultContainer.awardUsedRecipes(p_150672_, this.getRelevantItems());
                ItemStack itemstack = SecondSewingStationMenu.this.inputSlot.remove(1);
                if (!itemstack.isEmpty()) {
                    SecondSewingStationMenu.this.setupResultSlot();
                }

                pAccess.execute((p_40364_, p_40365_) -> {
                    long l = p_40364_.getGameTime();
                    if (SecondSewingStationMenu.this.lastSoundTime != l) {
                        p_40364_.playSound((Player)null, p_40365_, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        SecondSewingStationMenu.this.lastSoundTime = l;
                    }

                });
                super.onTake(p_150672_, p_150673_);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(SecondSewingStationMenu.this.inputSlot.getItem());
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pPlayerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlot(this.selectedRecipeIndex);
    }



    /**
     * Returns the index of the selected recipe.
     */
    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<SecondSewingRecipe> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.recipes.isEmpty();
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, ModBlocks.SECOND_SEWING_STATION.get());
    }

    /**
     * Handles the given Button-click on the server, currently only used by enchanting. Name is for legacy.
     */
    public boolean clickMenuButton(Player pPlayer, int pId) {
        if (this.isValidRecipeIndex(pId)) {
            this.selectedRecipeIndex.set(pId);
            this.setupResultSlot();
        }

        return true;
    }

    private boolean isValidRecipeIndex(int pRecipeIndex) {
        return pRecipeIndex >= 0 && pRecipeIndex < this.recipes.size();
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void slotsChanged(Container pInventory) {
        ItemStack itemstack = this.inputSlot.getItem();
        if (!itemstack.is(this.input.getItem())) {
            this.input = itemstack.copy();
            this.setupRecipeList(pInventory, itemstack);
        }

    }

    private void setupRecipeList(Container pContainer, ItemStack pStack) {
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!pStack.isEmpty()) {
            this.recipes = this.level.getRecipeManager().getRecipesFor(ModRecipes.SECOND_SEWING.get(), pContainer, this.level);
        }

    }

    void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            SecondSewingRecipe secondSewingRecipe = this.recipes.get(this.selectedRecipeIndex.get());
            ItemStack itemstack = secondSewingRecipe.assemble((Container) this.container, this.level.registryAccess());
            if (itemstack.isItemEnabled(this.level.enabledFeatures())) {
                this.resultContainer.setRecipeUsed(secondSewingRecipe);
                this.resultSlot.set(itemstack);
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }

        this.broadcastChanges();
    }

    public MenuType<?> getType() {
        return ModMenuTypes.SECOND_SEWING_STATION_MENU.get();
    }

    public void registerUpdateListener(Runnable pListener) {
        this.slotUpdateListener = pListener;
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
     * null for the initial slot that was double-clicked.
     */
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultContainer && super.canTakeItemForPickAll(pStack, pSlot);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();

            if (index == 2) { // Result slot
                item.onCraftedBy(itemstack1, player.level(), player);
                if (!this.moveItemStackTo(itemstack1, 3, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index == 0 || index == 1) { // Input slots
                if (!this.moveItemStackTo(itemstack1, 3, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 3 && index < 30) { // Inventory
                if (!this.moveItemStackTo(itemstack1, 30, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 30 && index < 38 && !this.moveItemStackTo(itemstack1, 3, 30, false)) { // Hotbar
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            this.broadcastChanges();
        }

        return itemstack;
    }
    /**
     * Called when the container is closed.
     */
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((p_40313_, p_40314_) -> {
            this.clearContainer(pPlayer, this.container);
        });
    }
}