package com.lenin.warpstonemod.common.network;

import javax.annotation.Nonnull;

public class OpenMutScreenPacket extends WarpPacket<OpenMutScreenPacket> {
	public OpenMutScreenPacket() {}

	@Nonnull
	@Override
	public Encoder<OpenMutScreenPacket> encoder() {
		return null;
	}

	@Nonnull
	@Override
	public Decoder<OpenMutScreenPacket> decoder() {
		return null;
	}

	@Nonnull
	@Override
	public Handler<OpenMutScreenPacket> handler() {
		return null;
	}
}