package me.tellinq.gabikblur;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;

/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class GabikBlurConfig extends Config {

    @Slider(
            name = "Multiplier",
            min = 0,
            max = 90,
            step = 1
    )
    public static int multiplier = 55;


    public GabikBlurConfig() {
        super(new Mod(GabikBlur.NAME, ModType.UTIL_QOL), GabikBlur.MODID + ".json");
        initialize();
    }
}

