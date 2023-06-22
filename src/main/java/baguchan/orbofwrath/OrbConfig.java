package baguchan.orbofwrath;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class OrbConfig {
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static class Server {
        public final ForgeConfigSpec.BooleanValue orbOfWrathMode;

        public Server(ForgeConfigSpec.Builder builder) {
            orbOfWrathMode = builder
                    .comment("Allow the Orb Of Wrath mode.")
                    .define("Enable the Orb Of Wrath Mode Default", false);
        }
    }
}
