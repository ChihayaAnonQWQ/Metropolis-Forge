package team.dovecotmc.old.metropolis.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegisterEvent;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.old.metropolis.block.entity.*;

public class OldMetroBlockEntities {
    public static BlockEntityType<BlockEntitySecurityInspectionMachine> SECURITY_INSPECTION_MACHINE_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityPSDSmallDoorSemiAuto> PSD_SMALL_DOOR;
    public static BlockEntityType<BlockEntityTicketVendor> TICKET_VENDOR_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityFareAdj> FARE_ADJ_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityTurnstile> TURNSTILE_BLOCK_ENTITY;

    public static void registerAll(RegisterEvent event) {
        SECURITY_INSPECTION_MACHINE_BLOCK_ENTITY = register(event, "security_inspection_machine", BlockEntityType.Builder.of(BlockEntitySecurityInspectionMachine::new, OldMetroBlocks.BLOCK_SECURITY_INSPECTION_MACHINE).build(null));
        PSD_SMALL_DOOR = register(event, "psd_small_door", BlockEntityType.Builder.of(BlockEntityPSDSmallDoorSemiAuto::new, OldMetroBlocks.BLOCK_PSD_JR_DOOR_1, OldMetroBlocks.BLOCK_PSD_JR_DOOR_2).build(null));
        TICKET_VENDOR_BLOCK_ENTITY = register(event, "ticket_vendor", BlockEntityType.Builder.of(BlockEntityTicketVendor::new, OldMetroBlocks.BLOCK_TICKET_VENDOR_EM10, OldMetroBlocks.BLOCK_TICKET_VENDOR_EV23).build(null));
        FARE_ADJ_BLOCK_ENTITY = register(event, "fare_adj", BlockEntityType.Builder.of(BlockEntityFareAdj::new, OldMetroBlocks.BLOCK_FARE_ADJ_EV23_YELLOW).build(null));
        TURNSTILE_BLOCK_ENTITY = register(event, "turnstile", BlockEntityType.Builder.of(BlockEntityTurnstile::new, OldMetroBlocks.BLOCK_TURNSTILE, OldMetroBlocks.BLOCK_TURNSTILE_IC_ONLY).build(null));
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(RegisterEvent event, String id, BlockEntityType<T> type) {
        event.register(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation(Metropolis.MOD_ID, id), () -> type);
        return type;
    }

    public static void initialize() {
    }
}
