package team.dovecotmc.old.metropolis.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.old.metropolis.block.entity.BlockEntityTurnstile;
import team.dovecotmc.metropolis.item.*;
import team.dovecotmc.old.metropolis.OldMetropolis;
import team.dovecotmc.old.metropolis.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.RegisterEvent;

public class OldMetroItems {
    // Security instruments
    public static Item ITEM_SECURITY_DOOR;
    public static Item ITEM_SECURITY_INSPECTION_MACHINE;

    // PSD
    public static Item ITEM_PSD_JR_DOOR_1;
    public static Item ITEM_PSD_JR_DOOR_2;
    public static Item ITEM_PSD_JR_FENCE_1;
    public static Item ITEM_PSD_JR_FENCE_2;

    // Ticket System
    public static Item ITEM_SINGLE_TRIP_TICKET;
    public static Item ITEM_SINGLE_TRIP_TICKET_USED;
    public static Item ITEM_CARD;
    public static Item ITEM_CREATIVE_CARD;
    public static Item ITEM_EXIT_TICKET;

    // Vendors
    public static Item ITEM_TICKET_VENDOR_TOP;
    public static Item ITEM_TICKET_VENDOR_PANEL;
    public static Item ITEM_TICKET_VENDOR_EM10;
    public static Item ITEM_TICKET_VENDOR_EV23;
    public static Item ITEM_FARE_ADJ_EV23;

    // Turnstile
    public static Item ITEM_TURNSTILE_ENTER;
    public static Item ITEM_TURNSTILE_EXIT;
    public static Item ITEM_TURNSTILE_IC_ONLY_ENTER;
    public static Item ITEM_TURNSTILE_IC_ONLY_EXIT;

    public static Item register(RegisterEvent event, String id, Item item) {
        return register(event, id, item, true);
    }

    public static Item register(RegisterEvent event, String id, Item item, boolean addToTab) {
        event.register(Registries.ITEM, new ResourceLocation(Metropolis.MOD_ID, id), () -> item);
        if (addToTab && OldMetropolis.shouldAddItemsToTab()) {
            MetroItems.TAB_ITEMS.add(item);
        }
        return item;
    }


    /**
     * Constructs and registers every entry of this class. Invoked from the matching Forge
     * RegisterEvent: a Block/Item takes an intrusive holder from its registry, so it can
     * only be created while that registry is unfrozen (i.e. during the register phase).
     */
    public static void registerAll(RegisterEvent event) {
        ITEM_SECURITY_DOOR = register(event, "security_door", new BlockItem(OldMetroBlocks.BLOCK_SECURITY_DOOR, new Item.Properties()));
        ITEM_SECURITY_INSPECTION_MACHINE = register(event, "security_inspection_machine", new BlockItem(OldMetroBlocks.BLOCK_SECURITY_INSPECTION_MACHINE, new Item.Properties()));
        ITEM_PSD_JR_DOOR_1 = register(event, "psd_jr_door_1", new BlockItemWithTooltips(OldMetroBlocks.BLOCK_PSD_JR_DOOR_1, new Item.Properties()), false);
        ITEM_PSD_JR_DOOR_2 = register(event, "psd_jr_door_2", new BlockItemWithTooltips(OldMetroBlocks.BLOCK_PSD_JR_DOOR_2, new Item.Properties()), false);
        ITEM_PSD_JR_FENCE_1 = register(event, "psd_jr_fence_1", new BlockItemWithTooltips(OldMetroBlocks.BLOCK_PSD_JR_FENCE_1, new Item.Properties()), false);
        ITEM_PSD_JR_FENCE_2 = register(event, "psd_jr_fence_2", new BlockItemWithTooltips(OldMetroBlocks.BLOCK_PSD_JR_FENCE_2, new Item.Properties()), false);
        ITEM_SINGLE_TRIP_TICKET = register(event, "single_trip_ticket", new ItemTicket(new Item.Properties(), true));
        ITEM_SINGLE_TRIP_TICKET_USED = register(event, "single_trip_ticket_used", new ItemTicket(new Item.Properties(), true));
        ITEM_CARD = register(event, "card", new ItemCard(new Item.Properties(), false));
        ITEM_CREATIVE_CARD = register(event, "creative_card", new ItemCard(new Item.Properties(), true));
        ITEM_EXIT_TICKET = register(event, "exit_ticket", new ItemExitTicket(new Item.Properties()));
        ITEM_TICKET_VENDOR_TOP = register(event, "ticket_vendor_top", new BlockItemWithTooltips(OldMetroBlocks.BLOCK_TICKET_VENDOR_TOP, new Item.Properties()));
        ITEM_TICKET_VENDOR_PANEL = register(event, "ticket_vendor_panel", new BlockItemWithTooltips(OldMetroBlocks.BLOCK_TICKET_VENDOR_PANEL, new Item.Properties()));
        ITEM_TICKET_VENDOR_EM10 = register(event, "ticket_vendor_em10", new ItemBlockWithTooltipShowStationHUD(OldMetroBlocks.BLOCK_TICKET_VENDOR_EM10, new Item.Properties()));
        ITEM_TICKET_VENDOR_EV23 = register(event, "ticket_vendor_ev23", new ItemBlockWithTooltipShowStationHUD(OldMetroBlocks.BLOCK_TICKET_VENDOR_EV23, new Item.Properties()));
        ITEM_FARE_ADJ_EV23 = register(event, "fare_adj_ev23", new ItemBlockWithTooltipShowStationHUD(OldMetroBlocks.BLOCK_FARE_ADJ_EV23_YELLOW, new Item.Properties()));
        ITEM_TURNSTILE_ENTER = register(event, "turnstile_enter", new ItemTurnstile(OldMetroBlocks.BLOCK_TURNSTILE, new Item.Properties(), BlockEntityTurnstile.EnumTurnstileType.ENTER));
        ITEM_TURNSTILE_EXIT = register(event, "turnstile_exit", new ItemTurnstile(OldMetroBlocks.BLOCK_TURNSTILE, new Item.Properties(), BlockEntityTurnstile.EnumTurnstileType.EXIT));
        ITEM_TURNSTILE_IC_ONLY_ENTER = register(event, "turnstile_ic_only_enter", new ItemTurnstile(OldMetroBlocks.BLOCK_TURNSTILE_IC_ONLY, new Item.Properties(), BlockEntityTurnstile.EnumTurnstileType.ENTER));
        ITEM_TURNSTILE_IC_ONLY_EXIT = register(event, "turnstile_ic_only_exit", new ItemTurnstile(OldMetroBlocks.BLOCK_TURNSTILE_IC_ONLY, new Item.Properties(), BlockEntityTurnstile.EnumTurnstileType.EXIT));
    }

    public static void initialize() {
    }
}
