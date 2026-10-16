package team.dovecotmc.old.metropolis;

import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.RegisterEvent;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.old.metropolis.init.OldMetroBlockEntities;
import team.dovecotmc.old.metropolis.init.OldMetroBlocks;
import team.dovecotmc.old.metropolis.init.OldMetroItems;
import team.dovecotmc.old.metropolis.network.OldMetroServerNetwork;

public class OldMetropolis {
    public static final boolean ADD_ITEMS_TO_TAB = true;

    /** The legacy content needs MTR, which in this environment is MTR 3 (package "mtr"). */
    public static final boolean MTR_CONTENT_ENABLED = detectMtr();
    private static final boolean enableMtrContent = MTR_CONTENT_ENABLED;

    private static boolean detectMtr() {
        try {
            Class.forName("mtr.MTR");
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean isMtrLoaded() {
        return MTR_CONTENT_ENABLED;
    }

    /** Network channels are independent of registries, so they are wired from the mod constructor. */
    public static void initializeOldContent() {
        if (MTR_CONTENT_ENABLED) {
            Metropolis.LOGGER.info("MTR detected! Loading legacy content.");
            OldMetroServerNetwork.registerAll();
        } else {
            Metropolis.LOGGER.info("MTR not detected! Skipped legacy content.");
        }
    }

    /** Legacy blocks/items/block entities, registered from the matching Forge register events. */
    public static void onRegister(RegisterEvent event) {
        if (!MTR_CONTENT_ENABLED) {
            return;
        }
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            OldMetroBlocks.registerAll(event);
        } else if (event.getRegistryKey().equals(Registries.ITEM)) {
            OldMetroItems.registerAll(event);
        } else if (event.getRegistryKey().equals(Registries.BLOCK_ENTITY_TYPE)) {
            OldMetroBlockEntities.registerAll(event);
        }
    }

    public static boolean shouldEnableMtrContent() {
        return enableMtrContent;
    }

    public static boolean shouldAddItemsToTab() {
        return ADD_ITEMS_TO_TAB && enableMtrContent;
    }
}
