package team.dovecotmc.metropolis.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.old.metropolis.OldMetropolis;
import team.dovecotmc.old.metropolis.client.OldMetropolisClient;

/**
 * Forge-bus client events. Replaces Fabric's HudRenderCallback.
 */
@Mod.EventBusSubscriber(modid = Metropolis.MOD_ID, value = Dist.CLIENT)
public final class MetropolisForgeClientEvents {
    private MetropolisForgeClientEvents() {
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (OldMetropolis.MTR_CONTENT_ENABLED) {
            OldMetropolisClient.BLOCK_PLACE_HUD.render(event.getGuiGraphics(), event.getPartialTick());
        }
    }
}
