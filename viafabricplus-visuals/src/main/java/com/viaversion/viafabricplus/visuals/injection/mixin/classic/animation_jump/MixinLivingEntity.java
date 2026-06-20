package com.viaversion.viafabricplus.visuals.injection.mixin.classic.animation_jump;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.viaversion.viafabricplus.visuals.settings.VisualSettings;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.level.Level;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    protected MixinLivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "handleDamageEvent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/WalkAnimationState;setSpeed(F)V"
        )
    )
    private void noAnimationDamageSpeed(WalkAnimationState animation, float speed) {
    	if(!VisualSettings.INSTANCE.disableDamageAnimationJump.isEnabled()) {
    		animation.setSpeed(speed);
    	}
        // Prevent damage animation from affecting walk speed
    }
}
