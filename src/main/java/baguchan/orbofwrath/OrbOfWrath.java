package baguchan.orbofwrath;

import baguchan.orbofwrath.registry.OrbEntities;
import baguchan.orbofwrath.util.OrbSpawnerData;
import baguchan.orbofwrath.util.OrbType;
import baguchan.orbofwrath.registry.OrbSpawnerDatas;
import baguchan.orbofwrath.registry.OrbTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OrbOfWrath.MODID)
public class OrbOfWrath
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "orbofwrath";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public OrbOfWrath()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        OrbSpawnerDatas.ORB_SPAWNER_DATA.register(modEventBus);
        OrbTypes.ORB_TYPE.register(modEventBus);
        OrbEntities.ENTITIES_REGISTRY.register(modEventBus);
        modEventBus.addListener(this::dataSetup);
        modEventBus.addListener(this::addOrbOfDominanceDatapack);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, OrbConfig.SERVER_SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    private void dataSetup(final DataPackRegistryEvent.NewRegistry event)
    {
        event.dataPackRegistry(OrbType.REGISTRY_KEY, OrbType.CODEC, OrbType.CODEC);
        event.dataPackRegistry(OrbSpawnerData.REGISTRY_KEY, OrbSpawnerData.CODEC, OrbSpawnerData.CODEC);
    }

    public void addOrbOfDominanceDatapack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            var resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("orbofdominance");
            var pack = Pack.readMetaAndCreate("builtin/orbofdominance", Component.translatable("pack.orbofwrath.orbofdominance"), false,
                    name -> new PathPackResources(name, resourcePath, true), PackType.SERVER_DATA, Pack.Position.TOP, PackSource.FEATURE);
            event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
        }
    }
}
