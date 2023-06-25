package me.tellinq.gabikblur.mixin;

import me.tellinq.gabikblur.GabikBlur;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", ordinal = 5))
    private void onRunGameLoop(CallbackInfo ci) {
        float value = GabikBlur.INSTANCE.getAccumulationValue();
        GL11.glAccum(GL11.GL_MULT, value);
        GL11.glAccum(GL11.GL_ACCUM, 1F - value);
        GL11.glAccum(GL11.GL_RETURN, 1F);
    }
}
