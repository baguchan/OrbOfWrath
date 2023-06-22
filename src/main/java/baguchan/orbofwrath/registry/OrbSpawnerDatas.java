package baguchan.orbofwrath.registry;

import baguchan.orbofwrath.OrbOfWrath;
import baguchan.orbofwrath.util.OrbSpawnerData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class OrbSpawnerDatas {
    public static final DeferredRegister<OrbSpawnerData> ORB_SPAWNER_DATA = DeferredRegister.create(OrbSpawnerData.REGISTRY_KEY,
            OrbOfWrath.MODID);
    public static final Supplier<IForgeRegistry<OrbSpawnerData>> ORB_SPAWNER_DATA_REGISTRY = ORB_SPAWNER_DATA.makeRegistry(
            () -> new RegistryBuilder<OrbSpawnerData>().disableSaving());
    public static ResourceKey<OrbSpawnerData> key(ResourceLocation name) {
        return ResourceKey.create(OrbSpawnerData.REGISTRY_KEY, name);
    }
}