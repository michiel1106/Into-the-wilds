package net.bikerboys.itw.screen;

import net.bikerboys.itw.TutorialMod;
import net.bikerboys.itw.screen.custom.SewingStationMenu;
import net.bikerboys.itw.screen.secondone.SecondSewingStationMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TutorialMod.MOD_ID);

    public static final RegistryObject<MenuType<AbstractContainerMenu>> SEWING_STATION_MENU = register("sewing_station", SewingStationMenu::new);
    public static final RegistryObject<MenuType<AbstractContainerMenu>> SECOND_SEWING_STATION_MENU = register("second_sewing_station", SecondSewingStationMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }


//    public static final RegistryObject<MenuType<SewingStationMenu>> SEWING_STATION_MENU =
//            MENUS.register("sewing_station_menu", () -> IForgeMenuType.create(
//                    (containerId, inventory, data) -> {
//                        Level level = inventory.player.level();
//                        ContainerLevelAccess access = ContainerLevelAccess.create(level, data.readBlockPos());
//                        SewingStationBlockEntity blockEntity = (SewingStationBlockEntity) level.getBlockEntity(data.readBlockPos());
//                        ContainerData containerData = new ContainerData() {
//                            @Override
//                            public int get(int index) {
//                                return data.readInt();
//                            }
//
//                            @Override
//                            public void set(int index, int value) {
//                                data.writeInt(value);
//                            }
//
//                            @Override
//                            public int getCount() {
//                                return 2; // Adjust based on how many values you're storing
//                            }
//                        };
//                        return new SewingStationMenu(containerId, inventory, access);
//                    }
//            ));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }






}
