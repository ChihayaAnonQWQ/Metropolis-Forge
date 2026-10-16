package team.dovecotmc.old.metropolis.client.network;

import team.dovecotmc.metropolis.network.MetroNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.old.metropolis.client.gui.fare_adj.FareAdjData;
import team.dovecotmc.old.metropolis.client.gui.fare_adj.FareAdjScreenMain;
import team.dovecotmc.old.metropolis.client.gui.ticket_vendor.TicketVendorData;
import team.dovecotmc.old.metropolis.client.gui.ticket_vendor.TicketVendorScreen1;
import team.dovecotmc.old.metropolis.client.gui.ticket_vendor.TicketVendorScreen4;
import team.dovecotmc.old.metropolis.mtr.WrappedMtrStation;
import team.dovecotmc.old.metropolis.network.OldMetroServerNetwork;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class OldMetroClientNetwork {
    public static void ticketVendorClose(BlockPos pos, ItemStack stack, int balance) {
        FriendlyByteBuf buf = MetroNetwork.createBuf();
        buf.writeBlockPos(pos);
        buf.writeItem(stack);
        buf.writeInt(balance);
        MetroNetwork.sendToServer(OldMetroServerNetwork.TICKET_VENDOR_CLOSE, buf);
    }

    public static void ticketVendorResult(BlockPos pos, ItemStack stack, int balance) {
        FriendlyByteBuf buf = MetroNetwork.createBuf();
        buf.writeBlockPos(pos);
        buf.writeItem(stack);
        buf.writeInt(balance);
        MetroNetwork.sendToServer(OldMetroServerNetwork.TICKET_VENDOR_RESULT, buf);
    }

    public static void fareAdjClose(BlockPos pos, ItemStack stack, int balance, boolean replace) {
        FriendlyByteBuf buf = MetroNetwork.createBuf();
        buf.writeBlockPos(pos);
        buf.writeItem(stack);
        buf.writeInt(balance);
        buf.writeBoolean(replace);
        MetroNetwork.sendToServer(OldMetroServerNetwork.FARE_ADJ_CLOSE, buf);
    }

    private static void registerTicketVendorGuiReceiver() {
        MetroNetwork.registerClientReceiver(OldMetroServerNetwork.TICKET_VENDOR_GUI, (client, buf) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack itemStack = buf.readItem();
            System.out.println(pos);

            int stationSize = buf.readInt();
            Set<WrappedMtrStation> stations = new HashSet<>();
            for (int i = 0; i < stationSize; i++) {
                stations.add(new WrappedMtrStation(
                        buf.readUtf(),
                        buf.readInt(),
                        buf.readInt()
                ));
            }

            client.execute(() -> client.setScreen(new TicketVendorScreen1(pos, new TicketVendorData(itemStack, stations))));
        });
    }

    private static void registerTicketVendorChargeGuiReceiver() {
        MetroNetwork.registerClientReceiver(OldMetroServerNetwork.TICKET_VENDOR_CHARGE_GUI, (client, buf) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack itemStack = buf.readItem();

            int stationSize = buf.readInt();
            Set<WrappedMtrStation> stations = new HashSet<>();
            for (int i = 0; i < stationSize; i++) {
                stations.add(new WrappedMtrStation(
                        buf.readUtf(),
                        buf.readInt(),
                        buf.readInt()
                ));
            }

            client.execute(() -> client.setScreen(new TicketVendorScreen4(pos, null, new TicketVendorData(itemStack, stations))));
        });
    }

    private static void registerRemoveInventoryItem() {
        MetroNetwork.registerClientReceiver(OldMetroServerNetwork.REMOVE_INVENTORY_ITEM, (client, buf) -> {
            BlockPos pos = buf.readBlockPos();
            int slot = buf.readInt();
            client.execute(() -> {
                if (client.level != null) {
                    if (client.level.getBlockEntity(pos) instanceof Container inventory) {
                        inventory.removeItemNoUpdate(slot);
                    }
                }
            });
        });
    }

    private static void registerFareAdjGuiReceiver() {
        MetroNetwork.registerClientReceiver(OldMetroServerNetwork.FARE_ADJ_GUI, (client, buf) -> {
            BlockPos pos = buf.readBlockPos();
            ItemStack stack = buf.readItem();
            client.execute(() -> client.setScreen(new FareAdjScreenMain(pos, new FareAdjData(stack))));
        });
    }

    public static Item currencyItem = Items.EMERALD;

    public static void updateCurrencyItem() {
        MetroNetwork.sendToServer(OldMetroServerNetwork.GET_CURRENCY_ITEM, MetroNetwork.createBuf());
    }

    public static void registerGetCurrencyItemReceiver() {
        MetroNetwork.registerClientReceiver(OldMetroServerNetwork.GET_CURRENCY_ITEM_RECEIVER, (client, buf) -> {
            currencyItem = buf.readItem().getItem();
        });
    }


    public static int maxFare = Metropolis.config.maxFare;

    public static void updateMaxFare() {
        MetroNetwork.sendToServer(OldMetroServerNetwork.GET_MAX_FARE, MetroNetwork.createBuf());
    }

    public static void registerGetMaxFareReceiver() {
        MetroNetwork.registerClientReceiver(OldMetroServerNetwork.GET_MAX_FARE_RECEIVER, (client, buf) -> {
            maxFare = buf.readInt();
        });
    }

    public static void registerAll() {
        registerTicketVendorGuiReceiver();
        registerTicketVendorChargeGuiReceiver();
        registerRemoveInventoryItem();
        registerGetCurrencyItemReceiver();
        registerFareAdjGuiReceiver();
        registerGetMaxFareReceiver();
    }
}
