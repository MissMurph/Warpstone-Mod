package com.lenin.warpstonemod.common.mutations.effect_mutations.mutations;

import com.lenin.warpstonemod.common.PlayerManager;
import com.lenin.warpstonemod.common.mutations.effect_mutations.EffectMutation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AlcoholicMutation extends EffectMutation {
	public AlcoholicMutation(ResourceLocation _key) {
		super(_key);
	}

	/**This mutation grants the player {@link Effects#ABSORPTION} every time
	 * they drink any potion, but -25% {@link Attributes#MAX_HEALTH}
	 */

	private final Map<UUID, Integer> valueMap = new HashMap<>();

	@Override
	public void attachListeners(IEventBus bus) {
		bus.addListener(this::onItemUseFinish);
		bus.addListener(this::onPotionFinish);
	}

	/**The method for this becomes more complicated with the understanding that
	 * {@link Effects#ABSORPTION} does not stack, you can't add extra absorption
	 * by adding more potion effects. <br>
	 * <br>
	 * Instead we have to use {@link LivingEntity#setAbsorptionAmount(float)} in order
	 * to get the Effect to stack, requiring manual tracking of how much we're altering
	 * the value. <br>
	 * <br>
	 * If we simply reset absorption to 0 after the potion effect wears off, we end up
	 * risking interfering with other mods or items that may want to alter the
	 * absorption value, thus we must track how much we're applying to the value. <br>
	 * <br>
	 * The method is to first check if the potion has any effects, if so we apply
	 * {@link Effects#ABSORPTION} with an amplifier value of -1 to result in 0 change
	 * performed by the Effect. We then calculate the new amount this Mutation is
	 * going to apply in newValue and cap it to 20, compare it to the value in the
	 * {@link AlcoholicMutation#valueMap} and add the difference to the current
	 * {@link LivingEntity#getAbsorptionAmount()}
	 */

	private void onItemUseFinish (LivingEntityUseItemEvent.Finish event) {
		if (event.getEntityLiving().level.isClientSide()
				|| !(event.getEntityLiving() instanceof PlayerEntity)
				|| !(event.getItem().getItem() instanceof PotionItem)
				|| PotionUtils.getCustomEffects(event.getItem()).isEmpty()
				|| !containsInstance(event.getEntityLiving())
		) return;

		LivingEntity entity = event.getEntityLiving();

		float newValue = Math.min(20, valueMap.get(entity.getUUID()) + 4) - valueMap.get(entity.getUUID());

		int ticks = Math.max(100, Math.round((float)Math.random() * 300));
		entity.addEffect(new EffectInstance(Effects.ABSORPTION, ticks, -1));

		entity.setAbsorptionAmount(entity.getAbsorptionAmount() + newValue);
		valueMap.put(entity.getUUID(), valueMap.get(entity.getUUID()) + (int) newValue);
	}

	private void onPotionFinish (PotionEvent.PotionExpiryEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)
				|| event.getPotionEffect().getEffect() != Effects.ABSORPTION
				|| !containsInstance(event.getEntityLiving())
		) return;

		LivingEntity entity = event.getEntityLiving();

		entity.setAbsorptionAmount(entity.getAbsorptionAmount() - valueMap.get(entity.getUUID()));
		valueMap.put(entity.getUUID(), 0);
	}

	@Override
	public void applyMutation(PlayerManager manager) {
		super.applyMutation(manager);

		if (manager.getParentEntity().level.isClientSide()) return;

		if (!valueMap.containsKey(manager.getParentEntity().getUUID())) valueMap.put(manager.getParentEntity().getUUID(), 0);
	}

	@Override
	public void clearMutation(PlayerManager manager) {
		super.clearMutation(manager);

		if (manager.getParentEntity().level.isClientSide()) return;

		manager.getParentEntity().setAbsorptionAmount(manager.getParentEntity().getAbsorptionAmount() - valueMap.get(manager.getUniqueId()));
		valueMap.remove(manager.getUniqueId());
		manager.getParentEntity().removeEffect(Effects.ABSORPTION);
	}
}