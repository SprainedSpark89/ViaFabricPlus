package com.viaversion.viafabricplus.visuals.injection.mixin.classic.walking_animation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.viaversion.viafabricplus.visuals.settings.VisualSettings;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;

@Mixin(LivingEntityRenderer.class)
// Nostalgic tweaks was a reference while making this code, i don't really don't do modern modding, so it was quite a good reference to help do this!
// check it out!: https://github.com/Nostalgica-Reverie/Nostalgic-Tweaks/blob/1.21/common/src/main/java/mod/adrenix/nostalgic/mixin/tweak/animation/player/LivingEntityRendererMixin.java
// https://github.com/Nostalgica-Reverie/Nostalgic-Tweaks/blob/1.21/common/src/main/java/mod/adrenix/nostalgic/helper/animation/ClassicWalkHelper.java
// this code also takes a bit from c0.30 and birevan's port they made for me, thx if i haven't said that before again
public class MixinLivingEntityRenderer {
	
	float prevLimbYaw = 0.0F;
	@Inject(
	        method = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", // targets the render method
	        at = @At(
	            ordinal = 1,
	            value = "INVOKE",
	            target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"
	        )
	    )
	    public <S extends LivingEntityRenderState> void oldBobbing(S livingEntityRenderState, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo callback) {
			if (VisualSettings.INSTANCE.oldWalkingAnimation.isEnabled()) {
				float var15;
				float var16;
				float var14 = 0.0625F;
				//matrixStack.translate(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
				// from the port to a1.1.2_01 by birevan, modified to work
				var15 = prevLimbYaw + (livingEntityRenderState.walkAnimationSpeed - prevLimbYaw) * livingEntityRenderState.xRot;
				var16 = livingEntityRenderState.walkAnimationPos - livingEntityRenderState.walkAnimationSpeed/* * (1.0F - livingEntityRenderState.pitch)*/;
				if(var15 > 1.0F) {
					var15 = 1.0F;
				}
				
				float var1 = var16;
				float var2 = var15;
				prevLimbYaw = livingEntityRenderState.walkAnimationSpeed;
				matrixStack.translate(0F, -Math.abs(Math.cos(var1 * 0.6662F)) * 0.5F * var2, 0F); // some math was stolen from nostalgic tweaks, sue me ;-), please actually don't, i don't know how copyright works
	    	}
	    }
}
