package team.dovecotmc.metropolis;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.client.MetropolisClient;
import team.dovecotmc.metropolis.config.MetroConfig;
import team.dovecotmc.metropolis.entity.EntitySittable;
import team.dovecotmc.metropolis.entity.MetroEntities;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.network.MetroNetwork;
import team.dovecotmc.metropolis.network.MetroServerNetworking;
import team.dovecotmc.metropolis.sittable.SittableRegistries;
import team.dovecotmc.metropolis.sittable.SittableRegistry;
import team.dovecotmc.old.metropolis.OldMetropolis;

import java.util.Optional;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright (c) 2023 Arrokoth All Rights Reserved.
 * <p>
 * Forge 1.20.1 port of the original Fabric {@code ModInitializer}.
 * <p>
 * Forge keeps every registry frozen before mod construction and only unfreezes it for the
 * {@code RegisterEvent} phase (ForgeStatesProvider: UNFREEZE_DATA runs immediately before
 * LOAD_REGISTRIES, FREEZE_DATA immediately after). Because building a {@code Block}, {@code Item},
 * {@code EntityType}, {@code BlockEntityType} or {@code CreativeModeTab} grabs an intrusive holder
 * from its registry, all content is constructed and registered from inside the matching register
 * event, and registration goes through {@code RegisterEvent#register} as Forge requires.
 * <p>
 * Registry creation order (MappedRegistry#getKnownRegistries, i.e. BuiltInRegistries declaration
 * order) guarantees BLOCK -> ENTITY_TYPE -> ITEM -> BLOCK_ENTITY_TYPE -> CREATIVE_MODE_TAB, which is
 * what makes the item/CreativeModeTab passes able to reference already-built blocks.
 */
@Mod(Metropolis.MOD_ID)
public class Metropolis {
    public static final String MOD_ID = "metropolis";
    public static final Logger LOGGER = LogManager.getLogger("Metropolis");

    /** Assigned during the CREATIVE_MODE_TAB register event. */
    public static CreativeModeTab ITEM_GROUP;

    public static final MetroConfig config = MetroConfig.load();

    public Metropolis() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegister);

        MetroNetwork.initialize();
        MetroServerNetworking.initialize();
        OldMetropolis.initializeOldContent();

        // The client-only config screen extension point must only ever be touched on the client.
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> MetropolisClient::registerConfigScreen);
    }

    private void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            MetroBlocks.registerAll(event);
            SittableRegistries.registerSittable(new SittableRegistry(MetroBlocks.BLOCK_BENCH, (state, player, hit) -> Optional.of(new Vec3(0.5, 0.1, 0.5))));
            OldMetropolis.onRegister(event);
        } else if (event.getRegistryKey().equals(Registries.ENTITY_TYPE)) {
            MetroEntities.registerAll(event);
        } else if (event.getRegistryKey().equals(Registries.ITEM)) {
            MetroItems.registerAll(event);
            OldMetropolis.onRegister(event);
        } else if (event.getRegistryKey().equals(Registries.BLOCK_ENTITY_TYPE)) {
            MetroBlockEntities.registerAll(event);
            OldMetropolis.onRegister(event);
        } else if (event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB)) {
            final CreativeModeTab tab = CreativeModeTab.builder()
                    .icon(() -> new ItemStack(MetroItems.ITEM_ITV_MONITOR))
                    .title(Component.translatable("itemGroup.metropolis.all"))
                    .displayItems((parameters, output) -> {
                        for (Item tabItem : MetroItems.getTabItems()) {
                            output.accept(tabItem);
                        }
                        output.accept(MetroItems.ITEM_BENCH);
                    })
                    .build();
            event.register(Registries.CREATIVE_MODE_TAB, new ResourceLocation(MOD_ID, "all"), () -> tab);
            ITEM_GROUP = tab;
        }
    }

    /** Forge replacement for the Fabric {@code UseBlockCallback}. */
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static final class ForgeEvents {
        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            if (event.getEntity().isShiftKeyDown()) {
                return;
            }

            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            // The seat entity is created server side and replicated to clients, so only act there.
            if (!level.isClientSide() && EntitySittable.trySit(level, pos, level.getBlockState(pos), event.getHitVec(), event.getEntity())) {
                event.setCanceled(true);
            }
        }
    }
}
