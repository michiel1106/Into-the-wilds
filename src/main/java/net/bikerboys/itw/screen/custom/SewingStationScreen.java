package net.bikerboys.itw.screen.custom;

import net.bikerboys.itw.TutorialMod;
import net.bikerboys.itw.recipes.SewingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SewingStationScreen extends AbstractContainerScreen<SewingStationMenu> {
    private static final ResourceLocation BG_LOCATION = new ResourceLocation(TutorialMod.MOD_ID, "textures/gui/sewing_station.png");


    private float scrollOffs;

    private boolean scrolling;

    private int startIndex;
    private boolean displayRecipes;

    public SewingStationScreen(SewingStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        pMenu.registerUpdateListener(this::containerChanged);
        --this.titleLabelY;
    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }


    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pGuiGraphics);
       // TutorialMod.LOGGER.info("renderbg");
        int i = this.leftPos;
        int j = this.topPos;
        pGuiGraphics.blit(BG_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int k = (int) (41.0F * this.scrollOffs);
        pGuiGraphics.blit(BG_LOCATION, i + 119, j + 15 + k, 176 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
        int l = this.leftPos + 52;
        int i1 = this.topPos + 14;
        int j1 = this.startIndex + 12;
        this.renderButtons(pGuiGraphics, pMouseX, pMouseY, l, i1, j1);
        this.renderRecipes(pGuiGraphics, l, i1, j1);
    }

    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        if (this.displayRecipes) {
            TutorialMod.LOGGER.info("render tooltip");
            int i = this.leftPos + 52;
            int j = this.topPos + 14;
            int k = this.startIndex + 12;
            List<SewingRecipe> list = this.menu.getRecipes();

            for(int l = this.startIndex; l < k && l < this.menu.getNumRecipes(); ++l) {
                int i1 = l - this.startIndex;
                int j1 = i + i1 % 4 * 16;
                int k1 = j + i1 / 4 * 18 + 2;
                if (pX >= j1 && pX < j1 + 16 && pY >= k1 && pY < k1 + 18) {
                    pGuiGraphics.renderTooltip(this.font, list.get(l).getResultItem(this.minecraft.level.registryAccess()), pX, pY);
                }
            }
        }

    }

    private void renderButtons(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int pX, int pY, int pLastVisibleElementIndex) {
        for (int i = this.startIndex; i < pLastVisibleElementIndex && i < this.menu.getNumRecipes(); ++i) {
            TutorialMod.LOGGER.info("renderbuttons");
            int j = i - this.startIndex;
            int k = pX + j % 4 * 16;
            int l = j / 4;
            int i1 = pY + l * 18 + 2;
            int j1 = this.imageHeight;
            if (i == this.menu.getSelectedRecipeIndex()) {
                j1 += 18;
            } else if (pMouseX >= k && pMouseY >= i1 && pMouseX < k + 16 && pMouseY < i1 + 18) {
                j1 += 36;
            }

            pGuiGraphics.blit(BG_LOCATION, k, i1 - 1, 0, j1, 16, 18);
        }
    }

    private void renderRecipes(GuiGraphics pGuiGraphics, int pX, int pY, int pStartIndex) {
        List<SewingRecipe> list = this.menu.getRecipes().stream().toList();
      //  TutorialMod.LOGGER.info("Rendering {} recipes", list.size());


        for (int i = this.startIndex; i < pStartIndex && i < list.size(); ++i) {
            int j = i - this.startIndex;
            int k = pX + j % 4 * 16;
            int l = j / 4;
            int i1 = pY + l * 18 + 2;

            if (this.minecraft != null && this.minecraft.level != null) {
                ItemStack result = list.get(i).getResultItem(this.minecraft.level.registryAccess());
                pGuiGraphics.renderItem(result, k, i1);
                pGuiGraphics.renderItemDecorations(this.font, result, k, i1);
            }
        }
    }




    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        this.scrolling = false;
        if (this.displayRecipes) {
            int i = this.leftPos + 52;
            int j = this.topPos + 14;
            int k = this.startIndex + 12;

            for(int l = this.startIndex; l < k; ++l) {
                int i1 = l - this.startIndex;
                double d0 = pMouseX - (double)(i + i1 % 4 * 16);
                double d1 = pMouseY - (double)(j + i1 / 4 * 18);
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, l)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
                    return true;
                }
            }

            i = this.leftPos + 119;
            j = this.topPos + 9;
            if (pMouseX >= (double)i && pMouseX < (double)(i + 12) && pMouseY >= (double)j && pMouseY < (double)(j + 54)) {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }




    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + 14;
            int j = i + 54;
            this.scrollOffs = ((float)pMouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5D) * 4;
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }



    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (this.isScrollBarActive()) {
            int i = this.getOffscreenRows();
            float f = (float) pDelta / (float) i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) i) + 0.5D) * 4;
        }

        return true;
    }

    private boolean isScrollBarActive() {
        int numRecipes = this.menu.getNumRecipes();
      //  TutorialMod.LOGGER.info("Checking scrollbar: {} recipes", numRecipes);
        return this.displayRecipes && numRecipes > 4; // Show scrollbar if more than 4 recipes
    }

    protected int getOffscreenRows() {
        int numRecipes = this.menu.getNumRecipes();
        return Math.max(0, (numRecipes + 3) / 4 - 3);
    }



    private void containerChanged() {
        this.displayRecipes = this.menu.hasInputItem();
        TutorialMod.LOGGER.info("Container changed - displayRecipes: {}", this.displayRecipes);

        if (this.displayRecipes) {
            this.startIndex = Math.max(0, Math.min(this.startIndex, this.menu.getNumRecipes() - 12));
        } else {
            this.scrollOffs = 0.0F;
            this.startIndex = 0;
        }
    }


}

