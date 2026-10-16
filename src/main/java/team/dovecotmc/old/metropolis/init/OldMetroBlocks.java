package team.dovecotmc.old.metropolis.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.old.metropolis.block.*;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.RegisterEvent;

public class OldMetroBlocks {
    // Security instruments
    public static Block BLOCK_SECURITY_DOOR;
    public static Block BLOCK_SECURITY_INSPECTION_MACHINE;

    // Platform Fence door
    public static Block BLOCK_PSD_JR_DOOR_1;
    public static Block BLOCK_PSD_JR_DOOR_2;
    public static Block BLOCK_PSD_JR_FENCE_1;
    public static Block BLOCK_PSD_JR_FENCE_2;

    // Ticket Vendors
    public static Block BLOCK_TICKET_VENDOR_UP_1;
    public static Block BLOCK_TICKET_VENDOR_UP_EM10;
    public static Block BLOCK_TICKET_VENDOR_UP_EV23;
    public static Block BLOCK_TICKET_VENDOR_UP_EV23_GREEN;
    public static Block BLOCK_TICKET_VENDOR_UP_EV23_YELLOW;

    public static Block BLOCK_TICKET_VENDOR_TOP;

    // Ticket Vendor
    public static Block BLOCK_TICKET_VENDOR_PANEL;
    public static Block BLOCK_TICKET_VENDOR_EM10;
    public static Block BLOCK_TICKET_VENDOR_EV23;
    public static Block BLOCK_FARE_ADJ_EV23_YELLOW;

    // Turnstile
    public static Block BLOCK_TURNSTILE;
    public static Block BLOCK_TURNSTILE_IC_ONLY;

    private static Block register(RegisterEvent event, String id, Block block) {
        event.register(Registries.BLOCK, new ResourceLocation(Metropolis.MOD_ID, id), () -> block);
        return block;
    }


    /**
     * Constructs and registers every entry of this class. Invoked from the matching Forge
     * RegisterEvent: a Block/Item takes an intrusive holder from its registry, so it can
     * only be created while that registry is unfrozen (i.e. during the register phase).
     */
    public static void registerAll(RegisterEvent event) {
        BLOCK_SECURITY_DOOR = register(event, "security_door", new BlockSecurityDoor());
        BLOCK_SECURITY_INSPECTION_MACHINE = register(event, "security_inspection_machine", new BlockSecurityInspectionMachine());
        BLOCK_PSD_JR_DOOR_1 = register(event, "psd_jr_door_1", new BlockLegacyPSDSmallDoorSemiAuto(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0f)));
        BLOCK_PSD_JR_DOOR_2 = register(event, "psd_jr_door_2", new BlockLegacyPSDSmallDoorSemiAuto(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0f)));
        BLOCK_PSD_JR_FENCE_1 = register(event, "psd_jr_fence_1", new BlockLegacyPSDSmallFenceSemiAuto(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0f)));
        BLOCK_PSD_JR_FENCE_2 = register(event, "psd_jr_fence_2", new BlockLegacyPSDSmallFenceSemiAuto(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0f)));
        BLOCK_TICKET_VENDOR_UP_1 = register(event, "ticket_vendor_up_1", new BlockTicketVendorUp());
        BLOCK_TICKET_VENDOR_UP_EM10 = register(event, "ticket_vendor_up_em10", new BlockTicketVendorUp());
        BLOCK_TICKET_VENDOR_UP_EV23 = register(event, "ticket_vendor_up_ev23", new BlockTicketVendorUp());
        BLOCK_TICKET_VENDOR_UP_EV23_GREEN = register(event, "ticket_vendor_up_ev23_green", new BlockTicketVendorUp());
        BLOCK_TICKET_VENDOR_UP_EV23_YELLOW = register(event, "ticket_vendor_up_ev23_yellow", new BlockTicketVendorUp());
        BLOCK_TICKET_VENDOR_TOP = register(event, "ticket_vendor_top", new BlockTicketVendorTop());
        BLOCK_TICKET_VENDOR_PANEL = register(event, "ticket_vendor_panel", new BlockTicketVendor(false));
        BLOCK_TICKET_VENDOR_EM10 = register(event, "ticket_vendor_em10", new BlockTicketVendor(true, BLOCK_TICKET_VENDOR_UP_EM10));
        BLOCK_TICKET_VENDOR_EV23 = register(event, "ticket_vendor_ev23", new BlockTicketVendor(true, BLOCK_TICKET_VENDOR_UP_EV23_GREEN));
        BLOCK_FARE_ADJ_EV23_YELLOW = register(event, "fare_adj_ev23", new BlockFareAdjMachine(BLOCK_TICKET_VENDOR_UP_EV23_YELLOW));
        BLOCK_TURNSTILE = register(event, "turnstile", new BlockTurnstile(false));
        BLOCK_TURNSTILE_IC_ONLY = register(event, "turnstile_ic_only", new BlockTurnstile(true));
    }

    public static void initialize() {
        OldMetroBlockEntities.initialize();
    }
}
