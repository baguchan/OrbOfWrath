package baguchan.orbofwrath.util;

import baguchan.orbofwrath.OrbOfWrath;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class OrbType {
    public static final Codec<OrbType> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(orbType -> orbType.biomes),
                    Codec.BOOL.fieldOf("ignore_non_player").orElseGet(() -> false).forGetter(orbType -> orbType.ignore_non_player),
                    Codec.BOOL.fieldOf("ignore_fire").orElseGet(() -> false).forGetter(orbType -> orbType.ignore_fire))
            .apply(instance, OrbType::new)
    );
    public static final ResourceKey<Registry<OrbType>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(new ResourceLocation(OrbOfWrath.MODID, "orb_type"));
    private final HolderSet<Biome> biomes;
    private final boolean ignore_non_player;
    private final boolean ignore_fire;

    public OrbType(HolderSet<Biome> biomes, boolean ignoreNonPlayer, boolean ignoreFire) {
        this.biomes = biomes;
        this.ignore_non_player = ignoreNonPlayer;
        this.ignore_fire = ignoreFire;
    }

    public HolderSet<Biome> getBiomes() {
        return biomes;
    }

    public boolean isIgnoreNonPlayer() {
        return ignore_non_player;
    }

    public boolean isIgnoreFire() {
        return ignore_fire;
    }
}