package net.bikerboys.itw;

import com.mojang.logging.LogUtils;
import net.bikerboys.itw.block.ModBlocks;
import net.bikerboys.itw.block.entity.ModBlockEntities;
import net.bikerboys.itw.item.ModItems;
import net.bikerboys.itw.recipes.ModRecipes;
import net.bikerboys.itw.recipes.SewingRecipe;
import net.bikerboys.itw.render.ArmorCurioRenderer;
import net.bikerboys.itw.screen.ModMenuTypes;
import net.bikerboys.itw.screen.custom.SewingStationMenu;
import net.bikerboys.itw.screen.custom.SewingStationScreen;
import net.bikerboys.itw.screen.secondone.SecondSewingStationMenu;
import net.bikerboys.itw.screen.secondone.SecondSewingStationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TutorialMod.MOD_ID)
public class TutorialMod
{

    public static final String MOD_ID = "itw";

    public static final Logger LOGGER = LogUtils.getLogger();

    public TutorialMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModRecipes.register(modEventBus);

        TutorialMod.LOGGER.info("Registered recipe type: {}", SewingRecipe.Type.ID);
        TutorialMod.LOGGER.info("Registered recipe serializer: {}", SewingRecipe.Serializer.ID);
        ModMenuTypes.register(modEventBus);
        modEventBus.addListener(this::commonSetup);

        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

    }

    private void clientSetup(final FMLClientSetupEvent evt) {

        CuriosRendererRegistry.register(
                ModItems.MASK.get(), () -> new ArmorCurioRenderer(new ResourceLocation("minecraft", "textures/models/armor/leathers_layer_1.png"))
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {


            for (RegistryObject<Item> item : ModItems.ITEMS.getEntries()) {
                // Add each item to the creative tab
                event.accept(item.get());
            }


        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(
                    (MenuType<SewingStationMenu>)(MenuType<?>) ModMenuTypes.SEWING_STATION_MENU.get(),
                    SewingStationScreen::new
            );

            MenuScreens.register(
                    (MenuType<SecondSewingStationMenu>)(MenuType<?>) ModMenuTypes.SECOND_SEWING_STATION_MENU.get(),
                    SecondSewingStationScreen::new
            );
           // MenuScreens.register(ModMenuTypes.SEWING_STATION_MENU.get(), SewingStationScreen::new);
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
