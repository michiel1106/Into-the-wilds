package net.bikerboys.itw.mixins;


import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.momosoftworks.coldsweat.config.spec.ItemSettingsConfig;
import com.momosoftworks.coldsweat.util.serialization.ListBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ItemSettingsConfig.class)
public abstract class ListBuilderMixin {



    @ModifyReceiver(method = "lambda$static$13", at = @At(value = "INVOKE", target = "Lcom/momosoftworks/coldsweat/util/serialization/ListBuilder;build()Ljava/util/ArrayList;"))
    private static ListBuilder modifybuilder(ListBuilder instance) {




        return instance.add(
                List.of("tailory:black_mask", 4, 0),
                List.of("tailory:jeans", 12, 0)
        );
    }


    @ModifyReceiver(method = "lambda$static$16", at = @At(value = "INVOKE", target = "Lcom/momosoftworks/coldsweat/util/serialization/ListBuilder;build()Ljava/util/ArrayList;"))
    private static ListBuilder modifybuilders(ListBuilder instance) {



        return instance.add(
                List.of("tailory:black_mask", 4, 0),
                List.of("tailory:jeans", 12, 0));
    }

    @Redirect(method = "lambda$static$18()Ljava/util/List;", at = @At(value = "INVOKE", target = "Ljava/util/List;of()Ljava/util/List;"), remap = false )
    private static List<?> addCustomCuriosInsulation() {
        // Add your custom insulation entries here
        return List.of(

                List.of("tailory:black_mask", 4, 0),
                List.of("tailory:jeans", 12, 0)
        );
    }


}



