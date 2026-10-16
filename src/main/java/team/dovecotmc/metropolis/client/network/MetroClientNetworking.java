package team.dovecotmc.metropolis.client.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import team.dovecotmc.metropolis.network.MetroNetwork;
import team.dovecotmc.metropolis.network.MetroServerNetworking;

public class MetroClientNetworking {
    public static void c2sTrainStopSignChanges(int index, BlockPos blockPos) {
        final FriendlyByteBuf buf = MetroNetwork.createBuf();
        buf.writeInt(index);
        buf.writeLong(blockPos.asLong());
        MetroNetwork.sendToServer(MetroServerNetworking.C2S_TRAIN_STOP_SIGN_CHANGES, buf);
    }

    public static void initialize() {
    }
}
