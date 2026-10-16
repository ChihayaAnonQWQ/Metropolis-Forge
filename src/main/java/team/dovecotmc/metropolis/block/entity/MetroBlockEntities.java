package team.dovecotmc.metropolis.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegisterEvent;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.MetroBlocks;

public class MetroBlockEntities {
    public static BlockEntityType<BlockEntityITVMonitor> ITV_MONITOR_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityBumper> BUMPER_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityCamera> CAMERA_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityPlatformDoor> PLATFORM_DOOR_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityPlatformDoorController> PLATFORM_DOOR_CONTROLLER_BLOCK_ENTITY;

    public static void registerAll(RegisterEvent event) {
        ITV_MONITOR_BLOCK_ENTITY = register(event, "itv_monitor", BlockEntityType.Builder.of(BlockEntityITVMonitor::new, MetroBlocks.BLOCK_ITV_MONITOR).build(null));
        BUMPER_BLOCK_ENTITY = register(event, "bumper", BlockEntityType.Builder.of(BlockEntityBumper::new, MetroBlocks.BLOCK_BUMPER).build(null));
        CAMERA_BLOCK_ENTITY = register(event, "camera", BlockEntityType.Builder.of(BlockEntityCamera::new, MetroBlocks.BLOCK_CAMERA_CEILING).build(null));
        PLATFORM_DOOR_BLOCK_ENTITY = register(event, "platform_door", BlockEntityType.Builder.of(BlockEntityPlatformDoor::new, MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT, MetroBlocks.BLOCK_PLATFORM_DOOR_HALF_HEIGHT_GLASS).build(null));
        PLATFORM_DOOR_CONTROLLER_BLOCK_ENTITY = register(event, "platform_door_controller", BlockEntityType.Builder.of(BlockEntityPlatformDoorController::new, MetroBlocks.BLOCK_PLATFORM_DOOR_CONTROLLER_PILLAR).build(null));
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(RegisterEvent event, String id, BlockEntityType<T> type) {
        event.register(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation(Metropolis.MOD_ID, id), () -> type);
        return type;
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Registering Block entities");
    }
}
