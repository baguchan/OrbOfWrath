package baguchan.orbofwrath.client.render;

import baguchan.orbofwrath.OrbOfWrath;
import baguchan.orbofwrath.client.model.OrbOfDominanceModel;
import baguchan.orbofwrath.entity.OrbOfDominance;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class OrbOfDominanceRenderer<T extends  OrbOfDominance> extends MobRenderer<T, OrbOfDominanceModel<T>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(OrbOfWrath.MODID, "textures/entity/orb_of_dominance.png");
    private final List<Vector4f> colors = List.of(new Vector4f(1, 0, 1, -1)
            , new Vector4f(1, 0, 0, 60)
            , new Vector4f(1, 1, 0, 100)
            , new Vector4f(1, 0, 1, 120));

    public final Vector3f DEFAULT_COLOR = new Vector3f(1, 1F, 1);

    public OrbOfDominanceRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_, new OrbOfDominanceModel<>(p_174008_.bakeLayer(OrbOfDominanceModel.LAYER_LOCATION)), 0.6F);
    }

    @Override
    public void render(T entity, float p_115456_, float particalTick, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) {
        //color setting like mojangs keyframe animation
        int coloroSize = this.colors.size();

        float f = ((float)entity.tickCount) % (this.colors.get(coloroSize - 1).w);
        int i = Math.max(0, Mth.binarySearch(0, this.colors.size(), (p_232315_) -> {
            return f <= this.colors.get(p_232315_).w;
        }) - 1);
        int j = Math.min(this.colors.size() - 1, i + 1);
        Vector4f colorkeyframe = this.colors.get(i);
        Vector4f colorokeyframe1 = this.colors.get(j);
        float f1 = f - colorkeyframe.w;
        float f2;
        if (j != i) {
            f2 = Mth.clamp(f1 / (colorokeyframe1.w - colorkeyframe.w), 0.0F, 1.0F);
        } else {
            f2 = 0.0F;
        }

        Vector4f vector3f = this.colors.get(Math.max(0, i - 1));
        Vector4f vector3f1 = this.colors.get(i);
        Vector4f vector3f2 = this.colors.get(j);
        Vector4f vector3f3 = this.colors.get(Math.min(this.colors.size() - 1, j + 1));
        Vector3f modifiredVec = new Vector3f(1.0F, 1.0F, 1.0F);
        modifiredVec.mulAdd(-1.0F, new Vector3f(Mth.catmullrom(f2, vector3f.x(), vector3f1.x(), vector3f2.x(), vector3f3.x()), Mth.catmullrom(f2, vector3f.y(), vector3f1.y(), vector3f2.y(), vector3f3.y()), Mth.catmullrom(f2, vector3f.z(), vector3f1.z(), vector3f2.z(), vector3f3.z())));
        this.model.setColor(modifiredVec.x, modifiredVec.y, modifiredVec.z);
        super.render(entity, p_115456_, particalTick, p_115458_, p_115459_, p_115460_);
        this.model.setColor(1, 1, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(OrbOfDominance p_114482_) {
        return TEXTURE;
    }
}
