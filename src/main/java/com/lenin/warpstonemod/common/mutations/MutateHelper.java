package com.lenin.warpstonemod.common.mutations;

import com.lenin.warpstonemod.common.network.PacketHandler;
import com.lenin.warpstonemod.common.network.SyncMutDataPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import com.lenin.warpstonemod.common.WarpstoneMain;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MutateHelper {

    private static MutateManager clientManager = new DummyMutateManager();

    protected static List<MutateManager> managers = new ArrayList<MutateManager>();

    public static MutateManager getManager (UUID playerUUID){
        for (MutateManager m : managers) {
            if (m.getParentEntity().getUniqueID() == playerUUID)
                return m; }
        return null;
    }

    public static MutateManager getManager (LivingEntity e) {
        for (MutateManager m : managers) { if (m.getParentEntity() == e) return m; }

        return null;
    }

    public static List<MutateManager> getManagers () {
        return managers;
    }

    public static MutateManager createManager (LivingEntity e) {
        MutateManager m = new MutateManager(e);
        managers.add(m);
        return m;
    }

    public static MutateManager getClientManager () {
        if (clientManager instanceof DummyMutateManager) clientManager = new MutateManager(Minecraft.getInstance().player);
        return clientManager;
    }

    @OnlyIn(Dist.CLIENT)
    public static void updateClientMutations(SyncMutDataPacket pkt) {
        MutateManager mut = getClientManager();
        mut.loadFromNBT(pkt.data);
    }

    public static void pushMutDataToClient (UUID playerUUID, CompoundNBT nbt){
        SyncMutDataPacket pkt = new SyncMutDataPacket();
        pkt.data = nbt;
        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        PacketHandler.CHANNEL.sendToPlayer(server.getPlayerList().getPlayerByUUID(playerUUID), pkt);
    }

    public static void loadMutData (UUID playerUUID){
        File f = getMutFile(playerUUID);
        CompoundNBT nbt = null;

        try {
            nbt = load_unsafe(f);
        } catch (Exception ignored) {}

        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        MutateManager manager = new MutateManager(server.getPlayerList().getPlayerByUUID(playerUUID));
        managers.add(manager);

        if (nbt != null) manager.loadFromNBT(nbt);
        else System.out.println("nbt loaded from file returns null");

        pushMutDataToClient(playerUUID, nbt);
    }

    private static CompoundNBT load_unsafe(File file) throws Exception {
        return CompressedStreamTools.read(file);
    }

    public static void savePlayerData (UUID playerUUID) {
        CompoundNBT data = Objects.requireNonNull(getManager(playerUUID)).getMutData();

        saveMutData(playerUUID, data);
    }

    private static void saveMutData (UUID playerUUID, CompoundNBT data) {
        File f = getMutFile(playerUUID);

        try {
            CompressedStreamTools.write(data, f);
        } catch (IOException ignored) {}
    }

    public static void clearManager (UUID uuid) {
        managers.remove(getManager(uuid));
        System.out.println(managers.size());
    }

    public static File getMutFile (UUID playerUUID) {
        File f = new File(WarpstoneMain.getProxy().getWarpServerDataDirectory(), playerUUID.toString() + ".warpstone");
        System.out.println("WARPLOGS: Loading NBT File");
        if (!f.exists()) {
            try {
                System.out.println("WARPLOGS: NBT File not found, writing new one");
                CompressedStreamTools.write(new CompoundNBT(), f);
            } catch (IOException ignored) {}
        }
        return f;
    }

    public static void init(){}
}