package baguchan.orbofwrath.registry;

import baguchan.orbofwrath.OrbOfWrath;
import baguchan.orbofwrath.util.OrbType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class OrbTypes {
    public static final DeferredRegister<OrbType> ORB_TYPE = DeferredRegister.create(OrbType.REGISTRY_KEY,
            OrbOfWrath.MODID);
    public static final Supplier<IForgeRegistry<OrbType>> ORB_TYPE_REGISTRY = ORB_TYPE.makeRegistry(
            () -> new RegistryBuilder<OrbType>().disableSaving());
    public static final ResourceKey<OrbType> OVERWORLD = key(new ResourceLocation(OrbOfWrath.MODID, "overworld"));

    public static ResourceKey<OrbType> key(ResourceLocation name) {
        return ResourceKey.create(OrbType.REGISTRY_KEY, name);
    }
}