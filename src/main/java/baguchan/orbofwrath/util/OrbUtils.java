package baguchan.orbofwrath.util;

import baguchan.orbofwrath.registry.OrbTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public class OrbUtils {

    public static WeightedRandomList<OrbSpawnerData> weightedRandomSpawnData(RegistryAccess registryAccess, ResourceLocation resourceLocation){
        WeightedRandomList<OrbSpawnerData> weightedRandomList = WeightedRandomList.create(registryAccess.registryOrThrow(OrbSpawnerData.REGISTRY_KEY).stream().filter(orbSpawnerData -> {
            return orbSpawnerData.orbTypeId.equals(resourceLocation);
        }).toList());
        return weightedRandomList;
    }

    public static ResourceLocation getRandomOrbType(RegistryAccess registryAccess, RandomSource random){
        return registryAccess.registryOrThrow(OrbType.REGISTRY_KEY).getRandom(random).get().key().location();
    }

    public static ResourceLocation getOrbTypeFromBiome(RegistryAccess registryAccess, Holder<Biome> biome){
        Optional<OrbType> optional = registryAccess.registryOrThrow(OrbType.REGISTRY_KEY).stream().filter(orbType -> {
            return orbType.getBiomes().contains(biome);
        }).findFirst();
        if(optional.isPresent()){
            return registryAccess.registryOrThrow(OrbType.REGISTRY_KEY).getKey(optional.get());
        }
        return OrbTypes.OVERWORLD.location();
    }
}