package team.dovecotmc.old.metropolis.util;

import mtr.client.ClientData;
import mtr.data.RailwayData;
import mtr.data.Station;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import team.dovecotmc.old.metropolis.mtr.WrappedMtrStation;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 * <p>
 * Forge port, retargeted from MTR 4 ({@code org.mtr.core.*}) to MTR 3 (package {@code mtr}).
 * MTR 3 exposes stations as public fields ({@code name}, {@code color}, {@code zone}) and models a
 * station area as a 2D rectangle ({@code corner1} / {@code corner2} holding X and Z).
 */
public class MtrStationUtil {
    private static WrappedMtrStation wrap(Station station) {
        return new WrappedMtrStation(station.name, station.color, station.zone);
    }

    private static boolean inArea(Station station, int x, int z) {
        if (station.corner1 == null || station.corner2 == null) {
            return false;
        }
        int x1 = Math.min(station.corner1.getA(), station.corner2.getA());
        int x2 = Math.max(station.corner1.getA(), station.corner2.getA());
        int z1 = Math.min(station.corner1.getB(), station.corner2.getB());
        int z2 = Math.max(station.corner1.getB(), station.corner2.getB());
        return x1 <= x && x <= x2 && z1 <= z && z <= z2;
    }

    public static Set<WrappedMtrStation> getStations(Level level) {
        Set<WrappedMtrStation> stations = new HashSet<>();
        if (level.isClientSide()) {
            for (Station station : ClientData.STATIONS) {
                stations.add(wrap(station));
            }
        } else {
            RailwayData railwayData = RailwayData.getInstance(level);
            if (railwayData != null) {
                for (Station station : railwayData.stations) {
                    stations.add(wrap(station));
                }
            }
        }
        return stations;
    }

    public static WrappedMtrStation getStationByPos(BlockPos pos, Level level) {
        final int x = pos.getX();
        final int z = pos.getZ();
        if (level.isClientSide()) {
            for (Station station : ClientData.STATIONS) {
                if (inArea(station, x, z)) {
                    return wrap(station);
                }
            }
        } else {
            RailwayData railwayData = RailwayData.getInstance(level);
            if (railwayData != null) {
                for (Station station : railwayData.stations) {
                    if (inArea(station, x, z)) {
                        return wrap(station);
                    }
                }
            }
        }
        return null;
    }
}
