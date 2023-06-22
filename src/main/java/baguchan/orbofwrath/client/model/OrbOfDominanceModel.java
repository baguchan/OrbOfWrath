package baguchan.orbofwrath.client.model;

import baguchan.orbofwrath.OrbOfWrath;
import baguchan.orbofwrath.client.animation.OrbOfDominanceAnimations;
import baguchan.orbofwrath.entity.OrbOfDominance;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class OrbOfDominanceModel<T extends OrbOfDominance> extends ColorableHierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(OrbOfWrath.MODID, "orb_of_dominance"), "orb_of_dominance");

    private final ModelPart root;
    private final ModelPart everything;

    public OrbOfDominanceModel(ModelPart root) {
        this.root = root;
        this.everything = root.getChild("everything");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition everything = partdefinition.addOrReplaceChild("everything", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition orb = everything.addOrReplaceChild("orb", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -7.0F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 56, 56);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.animationStateIdle, OrbOfDominanceAnimations.ORB_IDLE, ageInTicks);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}