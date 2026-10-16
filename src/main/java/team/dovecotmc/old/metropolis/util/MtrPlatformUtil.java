package team.dovecotmc.old.metropolis.util;

import mtr.data.RailwayData;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;
import team.dovecotmc.metropolis.block.platform_doors.AbstractBlockPlatformDoor;
import team.dovecotmc.metropolis.block.platform_doors.AbstractBlockPlatformFence;
import team.dovecotmc.old.metropolis.block.interfaces.IBlockMTRPlatformDoor;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Bridge between MTR 3's train simulation and Metropolis' platform screen doors.
 * <p>
 * On MTR 4 the original mod hooked {@code RenderVehicleHelper#canOpenDoors} to learn the train door
 * value at every door position. MTR 3 has no such class, and its own door scan
 * ({@code Train#scanDoors}, which is what drives {@code BlockPSDAPGDoorBase} blocks) turns out not to
 * run for a dwelling train in practice. So instead of hooking that scan, the doors are driven from
 * {@code TrainServer#handlePositions}, which runs on the server every tick and is where MTR computes
 * the car positions, using exactly the same scan geometry {@code Train#scanDoors} uses.
 * <p>
 * MTR's {@code doorValue} runs 0..1 and its own PSD blocks are fully open at {@code doorValue == 0.5},
 * so the legacy Metropolis PSD takes {@code doorValue * 2} and the half height doors simply follow
 * {@code doorValue > 0}.
 */
public final class MtrPlatformUtil {
    /** Throttles the diagnostic logging below to one line every few seconds. */
    private static long lastLogMillis = 0L;

    /**
     * Doors this train driven logic has opened, per level. When the train's doors close again only
     * these are closed, so doors the player opened independently (controller or redstone) are left
     * alone.
     */
    private static final Map<Level, Set<BlockPos>> OPENED_BY_TRAIN =
            Collections.synchronizedMap(new WeakHashMap<>());

    private MtrPlatformUtil() {
    }

    private static void logDriven(BlockPos pos, String kind, float trainDoorValue) {
        final long now = System.currentTimeMillis();
        if (now - lastLogMillis > 5000L) {
            lastLogMillis = now;
            Metropolis.LOGGER.info("[platform-door] train doorValue={} opened {} at {}", trainDoorValue, kind, pos);
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
    public static boolean driveDoor(Level level, BlockPos pos, float trainDoorValue) {
        final BlockState state = level.getBlockState(pos);
        final Block block = state.getBlock();

        if (!(block instanceof IBlockMTRPlatformDoor) && !(block instanceof AbstractBlockPlatformDoor)) {
            return false;
        }

        // Server authoritative: block state and block entity changes are replicated to clients.
        if (level.isClientSide()) {
            return false;
        }

        if (block instanceof IBlockMTRPlatformDoor door) {
            // Legacy JR style PSD: proportional, same 0..1 timing as MTR's own doors.
            final float openFraction = Mth.clamp(trainDoorValue * 2.0F, 0.0F, 1.0F);
            door.setOpenState(openFraction > 0.0F, openFraction, level, pos, state);
            logDriven(pos, "legacy PSD", trainDoorValue);
            return true;
        }

        if (block instanceof AbstractBlockPlatformDoor door) {
            // Half height door: binary open/closed, animated by the block entity over 20 ticks.
            // setOpenState needs the lower half, because that is where the block entity lives.
            final BlockPos lower = state.getValue(AbstractBlockPlatformFence.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
            door.setOpenState(level, lower, trainDoorValue > 0.0F);
            logDriven(lower, "half height door", trainDoorValue);
            return true;
        }

        return false;
    }

    /**
     * Drives every Metropolis door alongside a train, using MTR's own car positions and scan area.
     *
     * @param positions the {@code trainCars + 1} car positions MTR computed for this tick
     * @return how many door blocks were driven
     */
    public static int driveDoorsAlongTrain(Level level, int trainCars, float doorValue, Vec3[] positions) {
        if (level.isClientSide() || positions == null) {
            return 0;
        }

        // Doors closing again: close the ones this train opened and nothing else.
        // This is the common path (doors shut), so it avoids any map mutation.
        if (doorValue <= 0.0F) {
            final Set<BlockPos> previouslyOpened = OPENED_BY_TRAIN.get(level);
            if (previouslyOpened == null || previouslyOpened.isEmpty()) {
                return 0;
            }
            int closed = 0;
            for (BlockPos pos : previouslyOpened) {
                if (driveDoor(level, pos, 0.0F)) {
                    closed++;
                }
            }
            previouslyOpened.clear();
            return closed;
        }

        final Set<BlockPos> opened = OPENED_BY_TRAIN.computeIfAbsent(level, key -> new java.util.HashSet<>());
        int driven = 0;
        final int cars = Math.min(trainCars, positions.length - 1);
        for (int i = 0; i < cars; i++) {
            final Vec3 p1 = positions[i];
            final Vec3 p2 = positions[i + 1];
            if (p1 == null || p2 == null) {
                continue;
            }

            // Same layout Train#calculateCar hands to Train#scanDoors.
            final double x = (p1.x + p2.x) / 2.0;
            final double y = (p1.y + p2.y) / 2.0 + 1.0;
            final double z = (p1.z + p2.z) / 2.0;
            final double length = p1.distanceTo(p2);
            final float yaw = (float) Mth.atan2(p2.x - p1.x, p2.z - p1.z);
            final float pitch = length == 0.0 ? 0.0F : (float) Math.asin((p2.y - p1.y) / length);

            // Both sides of the car, exactly like MTR does for its own PSD blocks.
            driven += driveDoorsAtScan(level, x, y, z, (float) Math.PI + yaw, pitch, length / 2.0, doorValue, opened);
            driven += driveDoorsAtScan(level, x, y, z, yaw, pitch, length / 2.0, doorValue, opened);
        }
        return driven;
    }

    /** The block area {@code Train#scanDoors} walks around one door position. */
    private static int driveDoorsAtScan(Level level, double x, double y, double z, float rotY, float rotX,
                                         double width, float doorValue, Set<BlockPos> opened) {
        final Vec3 forward = new Vec3(1.0, 0.0, 0.0).yRot(rotY).xRot(rotX);
        final Vec3 side = new Vec3(0.0, 0.0, 1.0).yRot(rotY).xRot(rotX);

        int driven = 0;
        for (int a = 1; a <= 3; a++) {
            for (int b = -2; b <= 3; b++) {
                for (double c = -width; c <= width; c += 1.0) {
                    final BlockPos pos = RailwayData.newBlockPos(
                            x + forward.x * a + side.x * c,
                            y + b,
                            z + forward.z * a + side.z * c);
                    if (driveDoor(level, pos, doorValue)) {
                        opened.add(pos.immutable());
                        driven++;
                    }
                }
            }
        }
        return driven;
    }
}
