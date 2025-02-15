package net.bikerboys.itw.screen;

import com.google.common.collect.Lists;
import net.bikerboys.itw.TutorialMod;
import net.bikerboys.itw.recipes.ModRecipes;
import net.bikerboys.itw.recipes.SewingRecipe;
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

public class SewingStationMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int INPUT_SLOT2 = 1;
    public static final int RESULT_SLOT = 2;
    private static final int INV_SLOT_START = 2;
    private static final int INV_SLOT_END = 29;
    private static final int USE_ROW_SLOT_START = 29;
    private static final int USE_ROW_SLOT_END = 38;
    private final ContainerLevelAccess access;
    /** The index of the selected recipe in the GUI. */
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    private final Level level;
    private List<SewingRecipe> recipes = Lists.newArrayList();

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack input2 = ItemStack.EMPTY;

    long lastSoundTime;
    final Slot inputSlot;
    final Slot secondInputSlot;
    final Slot resultSlot;

    Runnable slotUpdateListener = () -> {
    };
   // final Container container = new SimpleContainer(2);

   public final Container container = new SimpleContainer(3) {

       public void setChanged() {
           super.setChanged();
           SewingStationMenu.this.slotsChanged(this);
           SewingStationMenu.this.slotUpdateListener.run();
       }
   };

    final ResultContainer resultContainer = new ResultContainer();
    public SewingStationMenu(int containerId, Inventory inventory, FriendlyByteBuf data) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }


    public SewingStationMenu(int pContainerId, Inventory pPlayerInventory, final ContainerLevelAccess pAccess) {
        super(ModMenuTypes.SEWING_STATION_MENU.get(), pContainerId);
        this.access = pAccess;
        this.level = pPlayerInventory.player.level();

        this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
        this.secondInputSlot = this.addSlot(new Slot(this.container, 1, 40, 33));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 2, 143, 33) {

            public boolean mayPlace(ItemStack p_40362_) {
                return false;
            }

            public void onTake(Player player, ItemStack stack) {
                stack.onCraftedBy(player.level(), player, stack.getCount());
                SewingStationMenu.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());

                // Decrease the count for BOTH input slots
                SewingStationMenu.this.inputSlot.remove(1);
                SewingStationMenu.this.secondInputSlot.remove(1);

                SewingStationMenu.this.setupResultSlot();
                pAccess.execute((level, pos) -> {
                    long l = level.getGameTime();
                    if (SewingStationMenu.this.lastSoundTime != l) {
                        level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        SewingStationMenu.this.lastSoundTime = l;
                    }
                });

                super.onTake(player, stack);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(SewingStationMenu.this.inputSlot.getItem(), SewingStationMenu.this.secondInputSlot.getItem());
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






    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<SewingRecipe> getRecipes() {
        //TutorialMod.LOGGER.info("getrecipes: {}", this.recipes);
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public boolean hasInputItem() {
        boolean hasItems = this.inputSlot.hasItem() && this.secondInputSlot.hasItem();
        boolean hasRecipes = !this.recipes.isEmpty();
        TutorialMod.LOGGER.info("hasInputItem: hasItems={}, hasRecipes={}", hasItems, hasRecipes); // Debug log
        return hasItems && hasRecipes;
    }

    public boolean stillValid(Player pPlayer) {
        return true;
    }


    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {
        if (this.isValidRecipeIndex(pId)) {
            this.selectedRecipeIndex.set(pId);
            this.setupResultSlot();
            return true;
        }
        return false;
    }

    private boolean isValidRecipeIndex(int pRecipeIndex) {
        return pRecipeIndex >= 0 && pRecipeIndex < this.recipes.size();
    }


    public void slotsChanged(Container pInventory) {
        ItemStack itemstack1 = this.inputSlot.getItem();
        ItemStack itemstack2 = this.secondInputSlot.getItem();

        if (!itemstack1.is(this.input.getItem()) || !itemstack2.is(this.input2.getItem())) {
            this.input = itemstack1.copy();
            this.input2 = itemstack2.copy();
            this.setupRecipeList(pInventory, itemstack1, itemstack2);
            TutorialMod.LOGGER.info("Slots changed: Slot1={}, Slot2={}", itemstack1.getItem(), itemstack2.getItem()); // Debug log
        }
    }

    private void setupRecipeList(Container pContainer, ItemStack stack1, ItemStack stack2) {
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);

        if (!stack1.isEmpty() && !stack2.isEmpty()) {
            this.recipes = this.level.getRecipeManager().getRecipesFor(ModRecipes.SEWING_TYPE.get(), pContainer, this.level);
            TutorialMod.LOGGER.info("Found recipes: {}", this.recipes);
            if (!this.recipes.isEmpty()) {

                this.selectedRecipeIndex.set(0);

            }
            // Automatically select the first recipe if there are results

        } else {
            TutorialMod.LOGGER.info("Input slots are empty");
        }

        this.broadcastChanges();
        this.setupResultSlot(); // <-- Add this line to trigger result update
    }



    void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            SewingRecipe sewingRecipe = this.recipes.get(this.selectedRecipeIndex.get());
            TutorialMod.LOGGER.info("Selected recipe: {}", sewingRecipe.getId()); // Debug log
            ItemStack itemstack = sewingRecipe.assemble(this.container, this.level.registryAccess());
            if (itemstack.isItemEnabled(this.level.enabledFeatures())) {
                this.resultContainer.setRecipeUsed(sewingRecipe);
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
        return ModMenuTypes.SEWING_STATION_MENU.get();
    }

    public void registerUpdateListener(Runnable pListener) {
        this.slotUpdateListener = pListener;
    }


    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultContainer && super.canTakeItemForPickAll(pStack, pSlot);
    }


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

    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((p_40313_, p_40314_) -> {
            this.clearContainer(pPlayer, this.container);
        });
    }
}





