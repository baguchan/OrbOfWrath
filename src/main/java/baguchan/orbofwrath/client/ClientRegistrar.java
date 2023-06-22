package baguchan.orbofwrath.client;

import baguchan.enchantwithmob.registry.ModEntities;
import baguchan.orbofwrath.OrbOfWrath;
import baguchan.orbofwrath.client.model.OrbOfDominanceModel;
import baguchan.orbofwrath.client.render.OrbOfDominanceRenderer;
import baguchan.orbofwrath.registry.OrbEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OrbOfWrath.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistrar {
    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(OrbEntities.ORB_OF_DOMINANCE.get(), OrbOfDominanceRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OrbOfDominanceModel.LAYER_LOCATION, OrbOfDominanceModel::createBodyLayer);
     }
}
