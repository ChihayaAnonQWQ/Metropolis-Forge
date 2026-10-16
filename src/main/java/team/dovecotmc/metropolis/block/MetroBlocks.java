package team.dovecotmc.metropolis.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.platform_doors.BlockPlatformDoorControllerPillar;
import team.dovecotmc.metropolis.block.platform_doors.BlockPlatformDoorHalfHeight;
import team.dovecotmc.metropolis.block.platform_doors.BlockPlatformFenceHalfHeight;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.RegisterEvent;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("unused")
public class MetroBlocks {
    // Cables
    public static Block BLOCK_CABLE;
    public static Block BLOCK_CABLE_HORIZONTAL;
    public static Block BLOCK_CABLE_INNER_CORNER_LEFT;
    public static Block BLOCK_CABLE_INNER_CORNER_RIGHT;
    public static Block BLOCK_CABLE_OUTER_CORNER_LEFT;
    public static Block BLOCK_CABLE_OUTER_CORNER_RIGHT;
    public static Block BLOCK_CABLE_DOWN_LEFT;
    public static Block BLOCK_CABLE_DOWN_RIGHT;
    public static Block BLOCK_CABLE_UP_LEFT;
    public static Block BLOCK_CABLE_UP_RIGHT;
    // Tunnel light
//    public static final Block BLOCK_TUNNEL_LIGHT_A = register("tunnel_light_a", new BlockTunnelLight(BlockBehaviour.Properties.of().mapColor(MapColor.STONE_GRAY).nonOpaque()));

    // Ceiling
    public static Block BLOCK_CEILING_A;

    // Lamp
    public static Block BLOCK_FLUORESCENT_LAMP;
    // Train bumpers
    public static Block BLOCK_BUMPER;
    // Cameras
    public static Block BLOCK_CAMERA_CEILING;

    // Sign
    public static Block BLOCK_SIGN_NO_PHOTO;
    public static Block BLOCK_TRAIN_STOP_SIGN;
    public static Block BLOCK_TRAIN_SPEED_LIMIT_SIGN;
    public static Block BLOCK_TRACKSIDE_SIGN_PILLAR;

    // PIDS
//    public static final Block BLOCK_PIDS_1 = register("pids_1", new BlockMetroPIDSOne());

    // Monitor
    public static Block BLOCK_ITV_MONITOR;

    // Platform doors
    public static Block BLOCK_PLATFORM_FENCE_HALF_HEIGHT;
    public static Block BLOCK_PLATFORM_DOOR_HALF_HEIGHT;
    public static Block BLOCK_PLATFORM_DOOR_HALF_HEIGHT_GLASS;

    // Platform door controllers
//    public static final Block BLOCK_PLATFORM_DOOR_CONTROLLER_BOX = register("platform_door_controller_box", new AbstractPlatformDoorController(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(6.0f)));
    public static Block BLOCK_PLATFORM_DOOR_CONTROLLER_PILLAR;

    /* =========== *
     * Decorations *
     * =========== */
    // Concrete
    public static Block BLOCK_CONCRETE;

    // Tiles
    // White
    public static Block BLOCK_TILES_WHITE;
    public static Block BLOCK_TILES_LARGE_WHITE;
    public static Block BLOCK_TILES_HORIZONTAL_WHITE;
    public static Block BLOCK_TILES_SMALL_WHITE;
    // Gray
    public static Block BLOCK_TILES_GRAY;
    public static Block BLOCK_TILES_HORIZONTAL_GRAY;
    public static Block BLOCK_TILES_SMALL_GRAY;
    // Cordon line
    public static Block BLOCK_CORDON_YELLOW_BLACK;
    public static Block BLOCK_CORDON_YELLOW_WHITE;
    public static Block BLOCK_CORDON_RED_BLACK;
    public static Block BLOCK_CORDON_RED_WHITE;

    // Bench
    public static Block BLOCK_BENCH;

    // Awning
    public static Block BLOCK_AWNING_PILLAR;
    public static Block BLOCK_AWNING_PILLAR_EMERGENCY;
    public static Block BLOCK_AWNING_BEAM;
    public static Block BLOCK_AWNING_ROOF;

    // Blind path
    public static Block BLOCK_BLIND_PATH;

    // Platform
    public static Block BLOCK_PLATFORM_A;
    public static Block BLOCK_PLATFORM_A_SLIM;

    // TODO: Blind path
//    public static final Block BLOCK_BLIND_PATH_STRIP = register("blind_path_strip", new BlockHorizontalAxis(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW.getMapColor())));
//    public static final Block BLOCK_BLIND_PATH_POINT = register("blind_path_point", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW.getMapColor())));

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
        BLOCK_CABLE = register(event, "cable", new BlockCable(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
        BLOCK_CABLE_HORIZONTAL = register(event, "cable_horizontal", new BlockCable(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
        BLOCK_CABLE_INNER_CORNER_LEFT = register(event, "cable_inner_corner_left", new BlockCable(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), true, false, false));
        BLOCK_CABLE_INNER_CORNER_RIGHT = register(event, "cable_inner_corner_right", new BlockCable(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), true, false, true));
        BLOCK_CABLE_OUTER_CORNER_LEFT = register(event, "cable_outer_corner_left", new BlockCable(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, true, false));
        BLOCK_CABLE_OUTER_CORNER_RIGHT = register(event, "cable_outer_corner_right", new BlockCable(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, true, true));
        BLOCK_CABLE_DOWN_LEFT = register(event, "cable_down_left", new BlockCable(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
        BLOCK_CABLE_DOWN_RIGHT = register(event, "cable_down_right", new BlockCable(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
        BLOCK_CABLE_UP_LEFT = register(event, "cable_up_left", new BlockCable(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
        BLOCK_CABLE_UP_RIGHT = register(event, "cable_up_right", new BlockCable(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
        BLOCK_CEILING_A = register(event, "ceiling_a", new BlockCeilingA(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(6.0f).noOcclusion().lightLevel((blockState) -> 1)));
        BLOCK_FLUORESCENT_LAMP = register(event, "fluorescent_lamp", new BlockFluorescentLamp());
        BLOCK_BUMPER = register(event, "bumper", new BlockBumper());
        BLOCK_CAMERA_CEILING = register(event, "camera_ceiling", new BlockCameraCeiling());
        BLOCK_SIGN_NO_PHOTO = register(event, "sign_no_photo", new BlockLegacySign(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0f)));
        BLOCK_TRAIN_STOP_SIGN = register(event, "train_stop_sign", new BlockTrainStopSign(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0f)));
        BLOCK_TRAIN_SPEED_LIMIT_SIGN = register(event, "train_speed_limit_sign", new BlockTrainSpeedLimitSign(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0f)));
        BLOCK_TRACKSIDE_SIGN_PILLAR = register(event, "trackside_sign_pillar", new BlockSignalSignPillar(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0f)));
        BLOCK_ITV_MONITOR = register(event, "itv_monitor", new BlockITVMonitor());
        BLOCK_PLATFORM_FENCE_HALF_HEIGHT = register(event, "platform_fence_half_height", new BlockPlatformFenceHalfHeight(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.TERRACOTTA_WHITE).strength(8.0f)));
        BLOCK_PLATFORM_DOOR_HALF_HEIGHT = register(event, "platform_door_half_height", new BlockPlatformDoorHalfHeight(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.TERRACOTTA_WHITE).strength(8.0f)));
        BLOCK_PLATFORM_DOOR_HALF_HEIGHT_GLASS = register(event, "platform_door_half_height_glass", new BlockPlatformDoorHalfHeight(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.TERRACOTTA_WHITE).strength(8.0f)));
        BLOCK_PLATFORM_DOOR_CONTROLLER_PILLAR = register(event, "platform_door_controller_pillar", new BlockPlatformDoorControllerPillar(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(6.0f)));
        BLOCK_CONCRETE = register(event, "concrete", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f)));
        BLOCK_TILES_WHITE = register(event, "tiles_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE.getMapColor()).strength(6.0f)));
        BLOCK_TILES_LARGE_WHITE = register(event, "tiles_large_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE.getMapColor()).strength(6.0f)));
        BLOCK_TILES_HORIZONTAL_WHITE = register(event, "tiles_horizontal_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE.getMapColor()).strength(6.0f)));
        BLOCK_TILES_SMALL_WHITE = register(event, "tiles_small_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE.getMapColor()).strength(6.0f)));
        BLOCK_TILES_GRAY = register(event, "tiles_gray", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY.getMapColor()).strength(6.0f)));
        BLOCK_TILES_HORIZONTAL_GRAY = register(event, "tiles_horizontal_gray", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY.getMapColor()).strength(6.0f)));
        BLOCK_TILES_SMALL_GRAY = register(event, "tiles_small_gray", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY.getMapColor()).strength(6.0f)));
        BLOCK_CORDON_YELLOW_BLACK = register(event, "cordon_yellow_black", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW.getMapColor()).strength(6.0f)));
        BLOCK_CORDON_YELLOW_WHITE = register(event, "cordon_yellow_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW.getMapColor()).strength(6.0f)));
        BLOCK_CORDON_RED_BLACK = register(event, "cordon_red_black", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.RED.getMapColor()).strength(6.0f)));
        BLOCK_CORDON_RED_WHITE = register(event, "cordon_red_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.RED.getMapColor()).strength(6.0f)));
        BLOCK_BENCH = register(event, "bench", new BlockBench(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f)));
        BLOCK_AWNING_PILLAR = register(event, "awning_pillar", new BlockAwningPillar(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f)));
        BLOCK_AWNING_PILLAR_EMERGENCY = register(event, "awning_pillar_emergency", new BlockAwningPillarEmergency(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f)));
        BLOCK_AWNING_BEAM = register(event, "awning_beam", new BlockAwningBeam(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f)));
        BLOCK_AWNING_ROOF = register(event, "awning_roof", new BlockAwningRoof(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY.getMapColor()).strength(6.0f)));
        BLOCK_BLIND_PATH = register(event, "blind_path", new BlockBlindPath(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW.getMapColor()).strength(4.0f)));
        BLOCK_PLATFORM_A = register(event, "platform_a", new BlockMetroPlatform(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f), BlockMetroPlatform.PlatformShape.NORMAL));
        BLOCK_PLATFORM_A_SLIM = register(event, "platform_a_slim", new BlockMetroPlatform(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f), BlockMetroPlatform.PlatformShape.SLIM));
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Blocks");
    }
}