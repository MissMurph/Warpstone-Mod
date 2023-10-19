package com.lenin.warpstonemod.client.models;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.lenin.warpstonemod.common.entities.RatEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class RatModel<T extends RatEntity> extends EntityModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer legFR;
	private final ModelRenderer legFL;
	private final ModelRenderer legBR;
	private final ModelRenderer legBL;
	private final ModelRenderer head;
	private final ModelRenderer tail;

	public RatModel() {
		textureWidth = 32;
		textureHeight = 32;

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.setTextureOffset(1, 1).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(3, 3).addBox(-2.0F, -2.0F, 1.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(7, 7).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(1, 14).addBox(-1.0F, -3.0F, 1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(1, 14).addBox(-1.0F, -3.0F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		legFR = new ModelRenderer(this);
		legFR.setRotationPoint(-1.5F, 23.5F, -3.5F);
		legFR.setTextureOffset(0, 9).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		legFL = new ModelRenderer(this);
		legFL.setRotationPoint(1.5F, 23.5F, -3.5F);
		legFL.setTextureOffset(0, 7).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		legBR = new ModelRenderer(this);
		legBR.setRotationPoint(-2.5F, 23.5F, 0.5F);
		legBR.setTextureOffset(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		legBL = new ModelRenderer(this);
		legBL.setRotationPoint(2.5F, 23.5F, 0.5F);
		legBL.setTextureOffset(0, 2).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 23.5F, -6.0F);
		head.setTextureOffset(10, 12).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

		tail = new ModelRenderer(this);
		tail.setRotationPoint(0.5F, 23.5F, 2.5F);
		tail.setTextureOffset(0, 7).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 5.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(RatEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		this.head.rotateAngleX = headPitch * 0.017453292F;
		this.head.rotateAngleY = netHeadYaw * 0.017453292F;
		this.body.rotateAngleX = 1.5707964F;
		this.legBR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.legBL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwing;
		this.legFR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.legFL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		legFR.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		legFL.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		legBR.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		legBL.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		tail.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}