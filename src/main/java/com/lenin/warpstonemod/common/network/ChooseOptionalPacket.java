package com.lenin.warpstonemod.common.network;

import com.lenin.warpstonemod.common.Registration;
import com.lenin.warpstonemod.common.mutations.MutateHelper;
import com.lenin.warpstonemod.common.mutations.evolving_mutations.EvolvingMutation;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ChooseOptionalPacket extends WarpPacket<ChooseOptionalPacket> {
    private CompoundNBT data;

    public ChooseOptionalPacket() {}

    public ChooseOptionalPacket (UUID _uuid, ResourceLocation _parent, ResourceLocation _target) {
        data = new CompoundNBT();
        data.putUniqueId("player", _uuid);
        data.putString("parent", _parent.toString());
        data.putString("target", _target.toString());
    }

    @Nonnull
    @Override
    public Encoder<ChooseOptionalPacket> encoder() {
        return (packet, buffer) -> ByteBufUtils.writeNBT(buffer, packet.data);
    }

    @Nonnull
    @Override
    public Decoder<ChooseOptionalPacket> decoder() {
        return buffer -> {
            ChooseOptionalPacket pkt = new ChooseOptionalPacket();
            pkt.data = ByteBufUtils.readNBT(buffer);

            return pkt;
        };
    }

    @Nonnull
    @Override
    public Handler<ChooseOptionalPacket> handler() {
        return new Handler<ChooseOptionalPacket>() {
            @Override
            public void handle(ChooseOptionalPacket packet, NetworkEvent.Context context, LogicalSide side) {
                if (side.isServer()) {
                    context.enqueueWork(() -> {
                        UUID player = data.getUniqueId("player");
                        ResourceLocation parent = new ResourceLocation(data.getString("parent"));
                        ResourceLocation target = new ResourceLocation(data.getString("target"));

                        if (Registration.MUTATIONS.containsKey(target)
                                && Registration.MUTATIONS.containsKey(parent)
                                && MutateHelper.getManager(player).containsMutation(parent)) {

                            ((EvolvingMutation)Registration.MUTATIONS.getValue(parent)).chooseOptional(
                                    MutateHelper.getManager(player),
                                    Registration.MUTATIONS.getValue(target)
                            );
                        }
                    });
                }
            }
        };
    }
}
