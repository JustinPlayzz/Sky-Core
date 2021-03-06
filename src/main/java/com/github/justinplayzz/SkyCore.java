package com.github.justinplayzz;

import com.github.justinplayzz.init.BayouBlues.util.TerrestriaConfigManager;
import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SkyCore implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "skycore";
    public static final String MOD_NAME = "Sky Core";
    private static final TerrestriaConfigManager CONFIG_MANAGER = new TerrestriaConfigManager();

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        //TODO: Initializer
    }

    public static TerrestriaConfigManager getConfigManager() {
        return CONFIG_MANAGER;
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}