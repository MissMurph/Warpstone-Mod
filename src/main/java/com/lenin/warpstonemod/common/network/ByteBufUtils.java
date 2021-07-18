package com.lenin.warpstonemod.common.network;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

public class ByteBufUtils {
	public static void writeUUID(PacketBuffer buf, UUID uuid) {
		buf.writeLong(uuid.getMostSignificantBits());
		buf.writeLong(uuid.getLeastSignificantBits());
	}

	public static UUID readUUID(PacketBuffer buf) {
		return new UUID(buf.readLong(), buf.readLong());
	}

	public static void writeNBT (PacketBuffer buf, @Nonnull CompoundNBT nbt) {
		try (DataOutputStream dos = new DataOutputStream(new ByteBufOutputStream(buf))) {
			CompressedStreamTools.write(nbt, dos);
		} catch (Exception ignored) {}
	}

	public static CompoundNBT readNBT (PacketBuffer buf) {
		try (DataInputStream dis = new DataInputStream(new ByteBufInputStream(buf))) {
			return CompressedStreamTools.read(dis);
		} catch (Exception ignored) {}
		throw new IllegalStateException("Could not load incoming NBT from byte buffer");
	}
}