package team.dovecotmc.old.metropolis.mixins;

import mtr.data.RailwayData;
import team.dovecotmc.metropolis.Metropolis;
import mtr.data.Train;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.dovecotmc.old.metropolis.util.MtrPlatformUtil;

/**
 * Makes MTR 3 treat Metropolis platforms / platform screen doors like its own.
 * <p>
 * {@code Train#scanDoors} walks the blocks around every train door position and only accepts
 * {@code mtr.block.BlockPlatform} / {@code mtr.block.BlockPSDAPGBase}. Metropolis builds with
 * {@code metropolis:platform_a} etc., so MTR concludes there is no platform: the per-car door flags
 * stay false and MTR never calls {@code openDoors} at our PSD blocks.
 * <p>
 * The MTR 4 original fixed this from {@code RenderVehicleHelper#canOpenDoors} (returning true for any
 * {@code IBlockPlatform}) and additionally pushed the train door value into
 * {@code IBlockMTRPlatformDoor#setOpenState}. This mixin restores both behaviours on MTR 3:
 * it hooks the scan position helper so every scanned block can be inspected, drives our PSD there,
 * and finally forces {@code scanDoors} to report "platform found" when a Metropolis platform was seen.
 */
@Mixin(value = Train.class, remap = false)
public abstract class MixinTrainDoorScan {
    @Unique
    private Level metropolis$level;

    @Unique
    private float metropolis$doorValue;

    @Unique
    private boolean metropolis$platformFound;

    @Unique
    private int metropolis$scanned;

    @Unique
    private int metropolis$driven;

    @Unique
    private String metropolis$hit;

    /** Throttles the per-scan summary line to one entry every few seconds. */
    @Unique
    private static long metropolis$lastSummary;


    // scanDoors returns boolean, so every callback must use CallbackInfoReturnable (even at HEAD).
    @Inject(method = "scanDoors", at = @At("HEAD"), remap = false)
    private void metropolis$beginScan(Level level, double x, double y, double z, float f1, float f2, double width, int carIndex, CallbackInfoReturnable<Boolean> cir) {
        this.metropolis$level = level;
        this.metropolis$doorValue = ((Train) (Object) this).getDoorValue();
        this.metropolis$platformFound = false;
        this.metropolis$scanned = 0;
        this.metropolis$driven = 0;
        this.metropolis$hit = null;
    }

    /**
     * MTR computes every block position it inspects through this helper, which makes it a precise,
     * mapping independent hook: no vector maths and no Minecraft member names need to be reproduced.
     */
    @Redirect(method = "scanDoors", remap = false,
            at = @At(value = "INVOKE", remap = false,
                    target = "Lmtr/data/RailwayData;newBlockPos(DDD)Lnet/minecraft/core/BlockPos;"))
    private BlockPos metropolis$inspectScannedPosition(double x, double y, double z) {
        final BlockPos pos = RailwayData.newBlockPos(x, y, z);
        final Level level = this.metropolis$level;
        if (level != null) {
            this.metropolis$scanned++;
            final Block block = level.getBlockState(pos).getBlock();
            if (MtrPlatformUtil.isMetropolisPlatformPart(block)) {
                this.metropolis$platformFound = true;
                if (this.metropolis$hit == null) {
                    this.metropolis$hit = pos.toShortString() + " "
                            + net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block);
                }
                if (MtrPlatformUtil.driveDoor(level, pos, this.metropolis$doorValue)) {
                    this.metropolis$driven++;
                }
            }
        }
        return pos;
    }

    @Inject(method = "scanDoors", at = @At("TAIL"), cancellable = true, remap = false)
    private void metropolis$reportPlatform(Level level, double x, double y, double z, float f1, float f2, double width, int carIndex, CallbackInfoReturnable<Boolean> cir) {
        final long now = System.currentTimeMillis();
        if (now - metropolis$lastSummary > 5000L) {
            metropolis$lastSummary = now;
            Metropolis.LOGGER.info("[platform-door] train scan side={} car={} near {},{},{} doorValue={} scanned={} metropolisHit={} driven={}",
                    level.isClientSide() ? "client" : "server", carIndex, (int) x, (int) y, (int) z,
                    this.metropolis$doorValue, this.metropolis$scanned, this.metropolis$hit, this.metropolis$driven);
        }

        if (!cir.getReturnValueZ() && this.metropolis$platformFound) {
            cir.setReturnValue(true);
        }
    }
}
