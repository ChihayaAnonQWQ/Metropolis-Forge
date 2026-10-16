package team.dovecotmc.metropolis.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.RegisterEvent;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("unused")
public class MetroItems {
    public static final List<Item> TAB_ITEMS = new ArrayList<>();

    public static Item ITEM_CABLE;

    // Ceiling
    public static Item ITEM_CEILING_A;

    // Lamp
    public static Item ITEM_FLUORESCENT_LAMP;

    public static Item ITEM_BUMPER;
    public static Item ITEM_CAMERA;

    // Sign
    public static Item ITEM_SIGN_NO_PHOTO;
    public static Item ITEM_TRAIN_STOP_SIGN;
//    public static final Item ITEM_TRAIN_SPEED_LIMIT_SIGN = register("train_speed_limit_sign", new BlockItemWithTooltips(MetroBlocks.BLOCK_TRAIN_SPEED_LIMIT_SIGN, new Item.Properties()));
    public static Item ITEM_TRACKSIDE_SIGN_PILLAR;

    // Ticket Machine
//    public static final Item ITEM_TICKET_MACHINE = register("ticket_machine", new BlockItem(MetroBlocks.BLOCK_TICKET_MACHINE, new Item.Settings().group(Metropolis.ITEM_GROUP)));

    // Monitor
    public static Item ITEM_ITV_MONITOR;

    // Platform fence doors
    public static Item ITEM_PLATFORM_FENCE_HALF_HEIGHT;
    public static Item ITEM_PLATFORM_DOOR_HALF_HEIGHT;
    public static Item ITEM_PLATFORM_DOOR_HALF_HEIGHT_GLASS;

    // Platform door controllers
    public static Item ITEM_PLATFORM_DOOR_CONTROLLER_PILLAR;

    /* =========== *
     * Decorations *
     * =========== */
    // Concrete
    public static Item ITEM_CONCRETE;

    // Tiles
    // White
    public static Item ITEM_TILES_WHITE;
    public static Item ITEM_TILES_LARGE_WHITE;
    public static Item ITEM_TILES_HORIZONTAL_WHITE;
    public static Item ITEM_TILES_SMALL_WHITE;

    // Gray
    public static Item ITEM_TILES_GRAY;
    public static Item ITEM_TILES_HORIZONTAL_GRAY;
    public static Item ITEM_TILES_SMALL_GRAY;

    // Cordon blocks
    public static Item ITEM_CORDON_YELLOW_BLACK;
    public static Item ITEM_CORDON_YELLOW_WHITE;
    public static Item ITEM_CORDON_RED_BLACK;
    public static Item ITEM_CORDON_RED_WHITE;

    // Bench
    public static Item ITEM_BENCH;

    // Awning
    public static Item ITEM_AWNING_PILLAR;
    public static Item ITEM_AWNING_PILLAR_EMERGENCY;
    public static Item ITEM_AWNING_BEAM;
    public static Item ITEM_AWNING_ROOF;

    // Blind path
    public static Item ITEM_BLIND_PATH;

    // Platform
    public static Item ITEM_PLATFORM_A;
    public static Item ITEM_PLATFORM_A_SLIM;

    // Sketch board
    public static Item ITEM_SKETCH_BOARD;

    public static Item register(RegisterEvent event, String id, Item item) {
        return register(event, id, item, true);
    }

    public static Item register(RegisterEvent event, String id, Item item, boolean displayInTab) {
        event.register(Registries.ITEM, new ResourceLocation(Metropolis.MOD_ID, id), () -> item);
        if (displayInTab) {
            TAB_ITEMS.add(item);
        }
        return item;
    }

    public static List<Item> getTabItems() {
        return TAB_ITEMS;
    }


    /**
     * Constructs and registers every entry of this class. Invoked from the matching Forge
     * RegisterEvent: a Block/Item takes an intrusive holder from its registry, so it can
     * only be created while that registry is unfrozen (i.e. during the register phase).
     */
    public static void registerAll(RegisterEvent event) {
        ITEM_CABLE = register(event, "cable", new BlockItem(MetroBlocks.BLOCK_CABLE, new Item.Properties()));
        ITEM_CEILING_A = register(event, "ceiling_a", new BlockItem(MetroBlocks.BLOCK_CEILING_A, new Item.Properties()));
        ITEM_FLUORESCENT_LAMP = register(event, "fluorescent_lamp", new BlockItem(MetroBlocks.BLOCK_FLUORESCENT_LAMP, new Item.Properties()));
        ITEM_BUMPER = register(event, "bumper", new BlockItem(MetroBlocks.BLOCK_BUMPER, new Item.Properties()));
        ITEM_CAMERA = register(event, "camera", new BlockItem(MetroBlocks.BLOCK_CAMERA_CEILING, new Item.Properties()));
        ITEM_SIGN_NO_PHOTO = register(event, "sign_no_photo", new BlockItem(MetroBlocks.BLOCK_SIGN_NO_PHOTO, new Item.Properties()), false);
        ITEM_TRAIN_STOP_SIGN = register(event, "train_stop_sign", new BlockItemWithTooltips(MetroBlocks.BLOCK_TRAIN_STOP_SIGN, new Item.Properties()));
        ITEM_TRACKSIDE_SIGN_PILLAR = register(event, "trackside_sign_pillar", new BlockItem(MetroBlocks.BLOCK_TRACKSIDE_SIGN_PILLAR, new Item.Properties()));
        ITEM_ITV_MONITOR = register(event, "itv_monitor", new BlockItem(MetroBlocks.BLOCK_ITV_MONITOR, new Item.Properties()));
        ITEM_PLATFORM_FENCE_HALF_HEIGHT = register(event, "platform_fence_half_height", new BlockItem(MetroBlocks.BLOCK_PLATFORM_FENCE_HALF_HEIGHT, new Item.Properties()));
        ITEM_PLATFORM_DOOR_HALF_HEIGHT = register(event, "platform_door_half_height", new BlockItem(MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT, new Item.Properties()));
        ITEM_PLATFORM_DOOR_HALF_HEIGHT_GLASS = register(event, "platform_door_half_height_glass", new BlockItem(MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT_GLASS, new Item.Properties()));
        ITEM_PLATFORM_DOOR_CONTROLLER_PILLAR = register(event, "platform_door_controller_pillar", new BlockItem(MetroBlocks.BLOCK_PLATFORM_DOOR_CONTROLLER_PILLAR, new Item.Properties()));
        ITEM_CONCRETE = register(event, "concrete", new BlockItem(MetroBlocks.BLOCK_CONCRETE, new Item.Properties()));
        ITEM_TILES_WHITE = register(event, "tiles_white", new BlockItem(MetroBlocks.BLOCK_TILES_WHITE, new Item.Properties()));
        ITEM_TILES_LARGE_WHITE = register(event, "tiles_large_white", new BlockItem(MetroBlocks.BLOCK_TILES_LARGE_WHITE, new Item.Properties()));
        ITEM_TILES_HORIZONTAL_WHITE = register(event, "tiles_horizontal_white", new BlockItem(MetroBlocks.BLOCK_TILES_HORIZONTAL_WHITE, new Item.Properties()));
        ITEM_TILES_SMALL_WHITE = register(event, "tiles_small_white", new BlockItem(MetroBlocks.BLOCK_TILES_SMALL_WHITE, new Item.Properties()));
        ITEM_TILES_GRAY = register(event, "tiles_gray", new BlockItem(MetroBlocks.BLOCK_TILES_GRAY, new Item.Properties()));
        ITEM_TILES_HORIZONTAL_GRAY = register(event, "tiles_horizontal_gray", new BlockItem(MetroBlocks.BLOCK_TILES_HORIZONTAL_GRAY, new Item.Properties()));
        ITEM_TILES_SMALL_GRAY = register(event, "tiles_small_gray", new BlockItem(MetroBlocks.BLOCK_TILES_SMALL_GRAY, new Item.Properties()));
        ITEM_CORDON_YELLOW_BLACK = register(event, "cordon_yellow_black", new BlockItemWithTooltips(MetroBlocks.BLOCK_CORDON_YELLOW_BLACK, new Item.Properties()));
        ITEM_CORDON_YELLOW_WHITE = register(event, "cordon_yellow_white", new BlockItemWithTooltips(MetroBlocks.BLOCK_CORDON_YELLOW_WHITE, new Item.Properties()));
        ITEM_CORDON_RED_BLACK = register(event, "cordon_red_black", new BlockItemWithTooltips(MetroBlocks.BLOCK_CORDON_RED_BLACK, new Item.Properties()));
        ITEM_CORDON_RED_WHITE = register(event, "cordon_red_white", new BlockItemWithTooltips(MetroBlocks.BLOCK_CORDON_RED_WHITE, new Item.Properties()));
        ITEM_BENCH = register(event, "bench", new BlockItem(MetroBlocks.BLOCK_BENCH, new Item.Properties()));
        ITEM_AWNING_PILLAR = register(event, "awning_pillar", new BlockItem(MetroBlocks.BLOCK_AWNING_PILLAR, new Item.Properties()));
        ITEM_AWNING_PILLAR_EMERGENCY = register(event, "awning_pillar_emergency", new BlockItem(MetroBlocks.BLOCK_AWNING_PILLAR_EMERGENCY, new Item.Properties()));
        ITEM_AWNING_BEAM = register(event, "awning_beam", new BlockItem(MetroBlocks.BLOCK_AWNING_BEAM, new Item.Properties()));
        ITEM_AWNING_ROOF = register(event, "awning_roof", new BlockItem(MetroBlocks.BLOCK_AWNING_ROOF, new Item.Properties()));
        ITEM_BLIND_PATH = register(event, "blind_path", new BlockItem(MetroBlocks.BLOCK_BLIND_PATH, new Item.Properties()));
        ITEM_PLATFORM_A = register(event, "platform_a", new BlockItem(MetroBlocks.BLOCK_PLATFORM_A, new Item.Properties()));
        ITEM_PLATFORM_A_SLIM = register(event, "platform_a_slim", new BlockItem(MetroBlocks.BLOCK_PLATFORM_A_SLIM, new Item.Properties()));
        ITEM_SKETCH_BOARD = register(event, "sketch_board", new ItemSketchBoard(new Item.Properties().stacksTo(1)), false);
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Items");
    }
}
