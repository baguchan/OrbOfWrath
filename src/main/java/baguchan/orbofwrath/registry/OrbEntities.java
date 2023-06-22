package baguchan.orbofwrath.registry;

import baguchan.orbofwrath.OrbOfWrath;
import baguchan.orbofwrath.entity.OrbOfDominance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = OrbOfWrath.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OrbEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OrbOfWrath.MODID);

    public static final RegistryObject<EntityType<OrbOfDominance>> ORB_OF_DOMINANCE = ENTITIES_REGISTRY.register("orb_of_dominance", () -> EntityType.Builder.of(OrbOfDominance::new, MobCategory.MONSTER).sized(1.0F, 1.0F).build(prefix("orb_of_dominance")));

    private static String prefix(String path) {
        return OrbOfWrath.MODID + "." + path;
    }

    @SubscribeEvent
    public static void registerEntity(EntityAttributeCreationEvent event) {
        event.put(ORB_OF_DOMINANCE.get(), OrbOfDominance.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(ORB_OF_DOMINANCE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
