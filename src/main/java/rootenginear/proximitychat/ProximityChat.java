package rootenginear.proximitychat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.net.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ProximityChat implements ModInitializer {
    public static final String MOD_ID = "proximitychat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static int RADIUS;
    public static String GLOBAL_TAG;
    public static String GLOBAL_STR;

    @Override
    public void onInitialize() {
        PropertyManager propertyManagerObj = new PropertyManager(new File("server.properties"));

        RADIUS = propertyManagerObj.getIntProperty("chat-radius", 0);
        GLOBAL_TAG = propertyManagerObj.getStringProperty("global-tag", "#");
        GLOBAL_STR = propertyManagerObj.getStringProperty("global-format", "§1<✈ {PLAYER}§1> {MSG}");

        RADIUS = Math.max(RADIUS, 0);
        GLOBAL_TAG = GLOBAL_TAG.isEmpty() ? "#" : GLOBAL_TAG;
        GLOBAL_STR = GLOBAL_STR.isEmpty() ? "§1<✈ {PLAYER}§1> {MSG}" : GLOBAL_STR;

        LOGGER.info("Proximity Chat initialized.");
    }
}
