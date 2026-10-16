package team.dovecotmc.metropolis.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.block.platform_doors.AbstractBlockPlatformFence;
import team.dovecotmc.metropolis.client.block.entity.BumperBlockEntityRenderer;
import team.dovecotmc.metropolis.client.block.entity.CameraBlockEntityRenderer;
import team.dovecotmc.metropolis.client.block.entity.ITVMonitorBlockEntityRenderer;
import team.dovecotmc.metropolis.client.block.entity.PlatformDoorBlockEntityRenderer;
import team.dovecotmc.metropolis.client.entity.EntitySittableRenderer;
import team.dovecotmc.metropolis.client.network.MetroClientNetworking;
import team.dovecotmc.metropolis.entity.MetroEntities;
import team.dovecotmc.old.metropolis.client.OldMetropolisClient;
import team.dovecotmc.old.metropolis.client.config.OldMetroClientConfig;
import team.dovecotmc.old.metropolis.client.modmenu.OldMetroModMenuConfigScreen;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright (c) 2023 Arrokoth All Rights Reserved.
 * <p>
 * Forge port of the Fabric ClientModInitializer. Every call that used to register into a Fabric
 * registry imperatively is now driven by the matching Forge mod-bus event.
 */
@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = Metropolis.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MetropolisClient {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(MetroBlockEntities.BUMPER_BLOCK_ENTITY, ctx -> new BumperBlockEntityRenderer());
        event.registerBlockEntityRenderer(MetroBlockEntities.CAMERA_BLOCK_ENTITY, ctx -> new CameraBlockEntityRenderer());
        event.registerBlockEntityRenderer(MetroBlockEntities.ITV_MONITOR_BLOCK_ENTITY, ctx -> new ITVMonitorBlockEntityRenderer());
        event.registerBlockEntityRenderer(MetroBlockEntities.PLATFORM_DOOR_BLOCK_ENTITY, ctx -> new PlatformDoorBlockEntityRenderer());
        event.registerEntityRenderer(MetroEntities.SITTABLE, EntitySittableRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((blockState, blockAndTintGetter, blockPos, i) -> DyeColor.byId(blockState.getValue(AbstractBlockPlatformFence.TINT_COLOR)).getTextColor(),
                MetroBlocks.BLOCK_PLATFORM_FENCE_HALF_HEIGHT,
                MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT,
                MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT_GLASS);
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new ResourceReloadListener());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(MetropolisClient::registerRenderLayers);
        MetroClientNetworking.initialize();
        OldMetropolisClient.initializeContent();
    }

    private static void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(MetroBlocks.BLOCK_TRAIN_STOP_SIGN, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MetroBlocks.BLOCK_TRAIN_SPEED_LIMIT_SIGN, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MetroBlocks.BLOCK_TRACKSIDE_SIGN_PILLAR, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MetroBlocks.BLOCK_BLIND_PATH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MetroBlocks.BLOCK_PLATFORM_DOOR_CONTROLLER_PILLAR, RenderType.cutout());
    }

    /** Replaces the Fabric ModMenu integration with Forge's built-in config screen hook. */
    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> new OldMetroModMenuConfigScreen(parent)));
    }

    private static class ResourceReloadListener implements PreparableReloadListener {
        @Override
        public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager,
                                              ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler,
                                              Executor backgroundExecutor, Executor gameExecutor) {
            return barrier.wait(null).thenRunAsync(() -> {
                Metropolis.LOGGER.info("Reloading!");
                OldMetropolisClient.config = OldMetroClientConfig.load();
            }, gameExecutor);
        }

        @Override
        public String getName() {
            return "metropolis_custom_resources";
        }
    }
}
