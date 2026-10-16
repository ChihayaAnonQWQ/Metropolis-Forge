package team.dovecotmc.old.metropolis.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;
import team.dovecotmc.metropolis.block.platform_doors.AbstractBlockPlatformDoor;
import team.dovecotmc.metropolis.block.platform_doors.AbstractBlockPlatformFence;
import team.dovecotmc.old.metropolis.block.interfaces.IBlockMTRPlatformDoor;

/**
 * Bridge between MTR 3's train door logic and Metropolis' platform screen doors.
 * <p>
 * MTR 3 only recognises its own platform/PSD blocks when it scans a train door position
 * ({@code Train#scanDoors}), and it only drives its own {@code BlockPSDAPGDoorBase} blocks. Metropolis
 * platforms and doors are neither, which is what the MTR 4 port solved with
 * {@code MixinRenderVehicleHelper} (it forced {@code canOpenDoors} to true for {@code IBlockPlatform}
 * blocks and pushed the train's door value into {@code IBlockMTRPlatformDoor}).
 * <p>
 * MTR's {@code doorValue} runs from 0 to 1, and its own PSD blocks use
 * {@code (int) Mth.clamp(doorValue * 64, 0, 32)} where 32 is fully open, i.e. their doors are fully
 * open at {@code doorValue == 0.5}. Metropolis' legacy PSD takes a 0..1 fraction, so the same visual
 * timing is {@code doorValue * 2}; the half height doors are binary and simply follow
 * {@code doorValue > 0}.
 */
public final class MtrPlatformUtil {
    /** Throttles the diagnostic logging below to one line every few seconds. */
    private static long lastLogMillis = 0L;

    private MtrPlatformUtil() {
    }

    private static void logDriven(BlockPos pos, float trainDoorValue) {
        final long now = System.currentTimeMillis();
        if (now - lastLogMillis > 5000L) {
            lastLogMillis = now;
            Metropolis.LOGGER.info("[platform-door] train doorValue={} driven door at {}", trainDoorValue, pos);
        }
    }

    /**
     * Everything Metropolis builds on a platform edge: the platform blocks themselves plus both the
     * half height fences (railings, static) and the half height doors (which slide).
     */
    public static boolean isMetropolisPlatformPart(Block block) {
        return block instanceof IBlockPlatform || block instanceof AbstractBlockPlatformFence;
    }

    /** Drives the Metropolis door at {@code pos} from MTR's train door value. Server side only. */
    public static void driveDoor(Level level, BlockPos pos, float trainDoorValue) {
        final BlockState state = level.getBlockState(pos);
        final Block block = state.getBlock();

        if (!(block instanceof IBlockMTRPlatformDoor) && !(block instanceof AbstractBlockPlatformDoor)) {
            return;
        }

        // Server authoritative: block state and block entity changes are replicated to clients.
        if (level.isClientSide()) {
            return;
        }

        if (block instanceof IBlockMTRPlatformDoor door) {
            // Legacy JR style PSD: proportional, same 0..1 timing as MTR's own doors.
            final float openFraction = Mth.clamp(trainDoorValue * 2.0F, 0.0F, 1.0F);
            door.setOpenState(openFraction > 0.0F, openFraction, level, pos, state);
            logDriven(pos, trainDoorValue);
        } else if (block instanceof AbstractBlockPlatformDoor door) {
            // Half height door: binary open/closed, animated by the block entity over 20 ticks.
            // setOpenState needs the lower half, because that is where the block entity lives.
            final BlockPos lower = state.getValue(AbstractBlockPlatformFence.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
            door.setOpenState(level, lower, trainDoorValue > 0.0F);
            logDriven(lower, trainDoorValue);
        }
    }
}
