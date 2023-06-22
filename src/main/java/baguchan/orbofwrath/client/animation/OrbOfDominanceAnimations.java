package baguchan.orbofwrath.client.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class OrbOfDominanceAnimations {

    public static final AnimationDefinition ORB_IDLE = AnimationDefinition.Builder.withLength(5.6f).looping()
            .addAnimation("orb",
                    new AnimationChannel(AnimationChannel.Targets.POSITION,
                            new Keyframe(0f, KeyframeAnimations.posVec(0f, 5f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(0.76f, KeyframeAnimations.posVec(0f, 8f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(1.4f, KeyframeAnimations.posVec(0f, 10f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(2.16f, KeyframeAnimations.posVec(0f, 7f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(2.8f, KeyframeAnimations.posVec(0f, 5f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(3.56f, KeyframeAnimations.posVec(0f, 8f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(4.2f, KeyframeAnimations.posVec(0f, 10f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(4.96f, KeyframeAnimations.posVec(0f, 7f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(5.6f, KeyframeAnimations.posVec(0f, 5f, 0f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("orb",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(90f, 45f, 45f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("everything",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(5.6f, KeyframeAnimations.degreeVec(0f, 360f, 0f),
                                    AnimationChannel.Interpolations.LINEAR))).build();
}
