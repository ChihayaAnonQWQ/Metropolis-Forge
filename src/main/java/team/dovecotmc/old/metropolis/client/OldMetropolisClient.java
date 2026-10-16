package team.dovecotmc.old.metropolis.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.old.metropolis.OldMetropolis;
import team.dovecotmc.old.metropolis.client.block.entity.BlockEntityRendererPSDSmallDoorSemiAuto;
import team.dovecotmc.old.metropolis.client.block.entity.FareAdjBlockEntityRenderer;
import team.dovecotmc.old.metropolis.client.block.entity.SecurityInspectionMachineBlockEntityRenderer;
import team.dovecotmc.old.metropolis.client.block.entity.TicketVendorBlockEntityRenderer;
import team.dovecotmc.old.metropolis.client.block.entity.TurnstileBlockEntityRenderer;
import team.dovecotmc.old.metropolis.client.config.OldMetroClientConfig;
import team.dovecotmc.old.metropolis.client.gui.MetroBlockPlaceHud;
import team.dovecotmc.old.metropolis.client.network.OldMetroClientNetwork;
import team.dovecotmc.old.metropolis.init.OldMetroBlockEntities;
import team.dovecotmc.old.metropolis.init.OldMetroBlocks;

@Mod.EventBusSubscriber(modid = Metropolis.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OldMetropolisClient {
    /** Legacy content is only loaded when MTR is present (MTR 3, package "mtr", in this environment). */
    public static final boolean MTR_CONTENT_ENABLED = OldMetropolis.isMtrLoaded();

    public static OldMetroClientConfig config = OldMetroClientConfig.load();
    public static final MetroBlockPlaceHud BLOCK_PLACE_HUD = new MetroBlockPlaceHud();

    public static void initializeContent() {
        if (!MTR_CONTENT_ENABLED) {
            Metropolis.LOGGER.info("MTR not detected! Skipped legacy client content.");
            return;
        }

        Metropolis.LOGGER.info("MTR detected! Loading legacy client content.");
        OldMetroClientNetwork.registerAll();
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (!MTR_CONTENT_ENABLED) {
            return;
        }
        event.registerBlockEntityRenderer(OldMetroBlockEntities.SECURITY_INSPECTION_MACHINE_BLOCK_ENTITY, ctx -> new SecurityInspectionMachineBlockEntityRenderer());
        event.registerBlockEntityRenderer(OldMetroBlockEntities.TURNSTILE_BLOCK_ENTITY, ctx -> new TurnstileBlockEntityRenderer());
        event.registerBlockEntityRenderer(OldMetroBlockEntities.FARE_ADJ_BLOCK_ENTITY, ctx -> new FareAdjBlockEntityRenderer());
        event.registerBlockEntityRenderer(OldMetroBlockEntities.TICKET_VENDOR_BLOCK_ENTITY, ctx -> new TicketVendorBlockEntityRenderer());
        event.registerBlockEntityRenderer(OldMetroBlockEntities.PSD_SMALL_DOOR, ctx -> new BlockEntityRendererPSDSmallDoorSemiAuto());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (!MTR_CONTENT_ENABLED) {
            return;
        }
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(OldMetroBlocks.BLOCK_SECURITY_INSPECTION_MACHINE, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(OldMetroBlocks.BLOCK_TICKET_VENDOR_EM10, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(OldMetroBlocks.BLOCK_TICKET_VENDOR_EV23, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(OldMetroBlocks.BLOCK_FARE_ADJ_EV23_YELLOW, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(OldMetroBlocks.BLOCK_TICKET_VENDOR_PANEL, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(OldMetroBlocks.BLOCK_TICKET_VENDOR_TOP, RenderType.cutout());
        });
    }
}
