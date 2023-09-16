package rootenginear.proximitychat;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProximityChat implements ModInitializer {
    public static final String MOD_ID = "proximitychat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Proximity Chat initialized.");
    }
}
