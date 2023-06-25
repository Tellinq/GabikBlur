package me.tellinq.gabikblur;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.opengl.Display;

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

    private int prevFpslimit;
    private boolean wasInactive;

    // Register the config and commands.
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        INSTANCE = this;
        config = new GabikBlurConfig();
        EventManager.INSTANCE.register(this);
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

    @Subscribe
    public void onTick(TickEvent e) {
        if (Minecraft.getMinecraft().theWorld == null) {
            if (wasInactive) {
                Minecraft.getMinecraft().gameSettings.limitFramerate = prevFpslimit;
                wasInactive = false;
            }
            return;
        }

        boolean active = Display.isActive();

        if (!active && Minecraft.getMinecraft().gameSettings.limitFramerate != 30) {
            prevFpslimit = Minecraft.getMinecraft().gameSettings.limitFramerate;
            Minecraft.getMinecraft().gameSettings.limitFramerate = 30;
            wasInactive = true;
        } else if (active && wasInactive) {
            Minecraft.getMinecraft().gameSettings.limitFramerate = prevFpslimit;
            wasInactive = false;
        }
    }
}
