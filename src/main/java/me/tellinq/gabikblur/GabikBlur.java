package me.tellinq.gabikblur;

import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = GabikBlur.MODID, name = GabikBlur.NAME, version = GabikBlur.VERSION)
public class GabikBlur {
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    // Sets the variables from `gradle.properties`. See the `blossom` config in `build.gradle.kts`.
    @Mod.Instance(MODID)
    public static GabikBlur INSTANCE; // Adds the instance of the mod, so we can access other variables.
    public static GabikBlurConfig config;

    private long lastTimestampInGame;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        INSTANCE = this;
        config = new GabikBlurConfig();
    }

    public float getMultiplier() {
        float multiplier = GabikBlurConfig.multiplier;
        int fps = Minecraft.getDebugFPS();
        return fps > 200 ? multiplier : fps > 120 ? 0.5F * multiplier : 0;
    }

    public float getAccumulationValue() {
        if (UMinecraft.getMinecraft().theWorld == null) {
            return 0;
        }

        float percent = this.getMultiplier() * 10.0F;
        if (UMinecraft.getMinecraft().currentScreen == null) {
            this.lastTimestampInGame = System.currentTimeMillis();
            percent = Math.min(percent, 996.0F);
        } else {
            percent = Math.min(percent, 990.0F);
        }

        long fadeOut = System.currentTimeMillis() - this.lastTimestampInGame;
        return fadeOut > 10000L ? 0 : Math.max(percent / 1000.0F, 0);
    }
}
