package team.dovecotmc.metropolis.network;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import team.dovecotmc.metropolis.Metropolis;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Forge port of the Fabric networking calls used by Metropolis.
 * <p>
 * The Fabric API allows an arbitrary {@link ResourceLocation} "channel" per packet, while Forge
 * uses a single {@link SimpleChannel} with registered message types. To keep the ported code as
 * close to the original as possible, one multiplexed message carries the original channel id plus
 * the raw payload, and receivers are dispatched by that id. Handler bodies therefore stay
 * unchanged and keep running on the network thread, exactly like they did on Fabric.
 */
public final class MetroNetwork {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Metropolis.MOD_ID, "main"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public interface ServerHandler {
        void handle(MinecraftServer server, ServerPlayer player, FriendlyByteBuf buf);
    }

    public interface ClientHandler {
        void handle(Minecraft client, FriendlyByteBuf buf);
    }

    private static final Map<String, ServerHandler> SERVER_HANDLERS = new HashMap<>();
    private static final Map<String, ClientHandler> CLIENT_HANDLERS = new HashMap<>();

    private MetroNetwork() {
    }

    public static FriendlyByteBuf createBuf() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }

    /** Fabric: {@code ServerPlayNetworking.registerGlobalReceiver}. */
    public static void registerServerReceiver(ResourceLocation id, ServerHandler handler) {
        SERVER_HANDLERS.put(id.toString(), handler);
    }

    /** Fabric: {@code ClientPlayNetworking.registerGlobalReceiver}. */
    public static void registerClientReceiver(ResourceLocation id, ClientHandler handler) {
        CLIENT_HANDLERS.put(id.toString(), handler);
    }

    /** Fabric: {@code ClientPlayNetworking.send} (client -> server). */
    public static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        CHANNEL.sendToServer(new MetroMessage(id.toString(), copyPayload(buf)));
    }

    /** Fabric: {@code ServerPlayNetworking.send} (server -> client). */
    public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf buf) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MetroMessage(id.toString(), copyPayload(buf)));
    }

    private static byte[] copyPayload(FriendlyByteBuf buf) {
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        return bytes;
    }

    public static void initialize() {
        CHANNEL.registerMessage(0, MetroMessage.class, MetroMessage::encode, MetroMessage::decode, MetroNetwork::handle);
    }

    private static void handle(MetroMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.setPacketHandled(true);

        FriendlyByteBuf buf = message.asBuf();
        if (context.getDirection().getReceptionSide().isServer()) {
            MinecraftServer server = context.getSender() == null ? null : context.getSender().getServer();
            ServerHandler handler = SERVER_HANDLERS.get(message.id);
            if (handler != null) {
                handler.handle(server, context.getSender(), buf);
            }
        } else {
            ClientHandler handler = CLIENT_HANDLERS.get(message.id);
            if (handler != null) {
                handler.handle(Minecraft.getInstance(), buf);
            }
        }
    }

    public static final class MetroMessage {
        private final String id;
        private final byte[] payload;

        public MetroMessage(String id, byte[] payload) {
            this.id = id;
            this.payload = payload;
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeUtf(id);
            buf.writeByteArray(payload);
        }

        public static MetroMessage decode(FriendlyByteBuf buf) {
            return new MetroMessage(buf.readUtf(), buf.readByteArray());
        }

        public FriendlyByteBuf asBuf() {
            return new FriendlyByteBuf(Unpooled.wrappedBuffer(payload));
        }
    }
}
