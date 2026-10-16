package team.dovecotmc.old.metropolis.mixins;

import mtr.data.Train;
import mtr.data.TrainServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.dovecotmc.old.metropolis.util.MtrPlatformUtil;

/**
 * Drives Metropolis platform screen doors from the train.
 * <p>
 * MTR 3 has no {@code RenderVehicleHelper#canOpenDoors} (what the MTR 4 original hooked) and its own
 * door scan does not run for a dwelling train here, so the doors are driven from
 * {@code TrainServer#handlePositions}: the server side place where MTR computes the car positions
 * every tick. {@link MtrPlatformUtil#driveDoorsAlongTrain} then walks the same block area MTR's own
 * door scan uses.
 */
@Mixin(value = TrainServer.class, remap = false)
public abstract class MixinTrainServerPositions {
    @Inject(method = "handlePositions(Lnet/minecraft/world/level/Level;[Lnet/minecraft/world/phys/Vec3;F)Z",
            at = @At("RETURN"), remap = false)
    private void metropolis$drivePlatformDoors(Level level, Vec3[] positions, float tickDelta,
                                               CallbackInfoReturnable<Boolean> cir) {
        final Train train = (Train) (Object) this;
        MtrPlatformUtil.driveDoorsAlongTrain(level, train.trainCars, train.getDoorValue(), positions);
    }
}
