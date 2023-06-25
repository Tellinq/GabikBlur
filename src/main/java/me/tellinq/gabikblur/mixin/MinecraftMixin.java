package me.tellinq.gabikblur.mixin;

import me.tellinq.gabikblur.GabikBlur;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", ordinal = 5))
    private void setupBlur(CallbackInfo ci) {
        if (GabikBlur.config.enabled) {
            float value = GabikBlur.INSTANCE.getAccumulationValue();
            GL11.glAccum(GL11.GL_MULT, value);
            GL11.glAccum(GL11.GL_ACCUM, 1F - value);
            GL11.glAccum(GL11.GL_RETURN, 1F);
        }
    }

    @Inject(method = "getLimitFramerate", at = @At(value = "HEAD"), cancellable = true)
    public void unfocusedFPSLimiter(CallbackInfoReturnable<Integer> cir) {
        if (Minecraft.getMinecraft().theWorld != null && GabikBlur.config.enabled) {
            if (!Display.isActive()) {
                cir.setReturnValue(30);
            }
        }
    }
}
