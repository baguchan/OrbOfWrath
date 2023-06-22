package baguchan.orbofwrath.entity;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.registry.ModSoundEvents;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import baguchan.orbofwrath.registry.OrbTypes;
import baguchan.orbofwrath.util.OrbType;
import baguchan.orbofwrath.util.OrbUtils;
import baguchan.orbofwrath.util.OrbSpawnerData;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class OrbOfDominance extends Monster {
    private static final EntityDataAccessor<String> ORB_TYPE = SynchedEntityData.defineId(OrbOfDominance.class, EntityDataSerializers.STRING);

    public static final Predicate<LivingEntity> TARGET_ENTITY_SELECTOR = (p_213616_0_) -> {
        return !(p_213616_0_ instanceof Enemy);
    };

    public AnimationState animationStateIdle = new AnimationState();

    private LivingEntity enchantTarget;
    protected int spellCastingTickCount;
    private OrbType orbTypeCache;
    public OrbOfDominance(EntityType<? extends OrbOfDominance> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 32.0D).add(Attributes.ATTACK_KNOCKBACK).add(Attributes.ARMOR, 10).add(Attributes.KNOCKBACK_RESISTANCE, 1.0F).add(Attributes.MAX_HEALTH, 200F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new SpellGoal());
        this.goalSelector.addGoal(2, new SummonSpellGoal());
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Enemy.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, TARGET_ENTITY_SELECTOR));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ORB_TYPE, OrbTypes.OVERWORLD.location().toString());
    }

    @Override
    public void travel(Vec3 p_21280_) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, p_21280_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double)0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, p_21280_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            } else {
                this.moveRelative(this.getSpeed(), p_21280_);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double)0.91F));
            }
        }

        this.calculateEntityAnimation(false);
    }

    @Override
    protected int decreaseAirSupply(int p_28882_) {
        return p_28882_;
    }


    protected float getSoundVolume() {
        return 3.0F;
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public void randomOrbType(LevelAccessor levelAccessor){
        this.setOrbType(OrbUtils.getRandomOrbType(levelAccessor.registryAccess(), levelAccessor.getRandom()).toString());
    }


    public void pickupOrbTypeFromBiome(LevelAccessor levelAccessor, Holder<Biome> biomeHolder){
        this.setOrbType(OrbUtils.getOrbTypeFromBiome(levelAccessor.registryAccess(), biomeHolder).toString());
    }

    public void setOrbType(String type) {
        this.entityData.set(ORB_TYPE, type);
        orbTypeCache = this.level().registryAccess().registryOrThrow(OrbType.REGISTRY_KEY).get(ResourceLocation.tryParse(type));
    }

    public OrbType getOrbTypeCache() {
        if(orbTypeCache == null){
            orbTypeCache = this.level().registryAccess().registryOrThrow(OrbType.REGISTRY_KEY).get(ResourceLocation.tryParse(this.getOrbType()));
        }

        return orbTypeCache;
    }

    public String getOrbType(){
        return this.entityData.get(ORB_TYPE);
    }

    public void addAdditionalSaveData(CompoundTag p_33619_) {
        super.addAdditionalSaveData(p_33619_);

        p_33619_.putInt("SpellTicks", this.spellCastingTickCount);
        p_33619_.putString("OrbType", this.getOrbType());
    }

    public void readAdditionalSaveData(CompoundTag p_33607_) {
        super.readAdditionalSaveData(p_33607_);
        this.spellCastingTickCount = p_33607_.getInt("SpellTicks");
        this.setOrbType(p_33607_.getString("OrbType"));
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()){
            this.animationStateIdle.startIfStopped(this.tickCount);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spellCastingTickCount > 0) {
            --this.spellCastingTickCount;
        }
    }

    @Override
    public boolean fireImmune() {
        return super.fireImmune();
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if(this.getOrbTypeCache().isIgnoreNonPlayer()) {
            if (p_21016_.getEntity() instanceof Player || p_21016_.is(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)) {
                return super.hurt(p_21016_, p_21017_);
            }else {
                return false;
            }
        }
        return super.hurt(p_21016_, p_21017_);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33601_, DifficultyInstance p_33602_, MobSpawnType p_33603_, @Nullable SpawnGroupData p_33604_, @Nullable CompoundTag p_33605_) {
        RandomSource randomsource = p_33601_.getRandom();
        if(p_33603_ == MobSpawnType.SPAWN_EGG) {
            this.randomOrbType(p_33601_);
        }else {
            this.pickupOrbTypeFromBiome(p_33601_, p_33601_.getBiome(this.blockPosition()));
        }
        return super.finalizeSpawn(p_33601_, p_33602_, p_33603_, p_33604_, p_33605_);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    private void setEnchantTarget(@Nullable LivingEntity enchantTargetIn) {
        this.enchantTarget = enchantTargetIn;
    }

    @Nullable
    public LivingEntity getEnchantTarget() {
        return enchantTarget;
    }


    @Override
    public boolean isAlliedTo(Entity p_184191_1_) {
        if (super.isAlliedTo(p_184191_1_)) {
            return true;
        } else if (p_184191_1_ instanceof LivingEntity && p_184191_1_ instanceof Enemy) {
            return this.getTeam() == null && p_184191_1_.getTeam() == null;
        } else {
            return false;
        }
    }

    public void summonMobAt(LivingEntity targetedEntity) {
        Optional<OrbSpawnerData> optional = OrbUtils.weightedRandomSpawnData(this.level().registryAccess(), ResourceLocation.tryParse(this.getOrbType())).getRandom(this.random);

        if(optional.isPresent()) {
            int randomCount = this.random.nextInt(optional.get().minCount, optional.get().maxCount);
            for(int spawnTry = 0; spawnTry < randomCount; spawnTry++) {
                Entity entity = optional.get().type.create(this.level());

                entity.setPos(this.getX(), this.getY(), this.getZ());


                for (int i = 0; i < 16; i++) {
                    double attemptX;
                    double attemptY;
                    double attemptZ;

                    attemptX = targetedEntity.getX() + this.getRandom().nextGaussian() * 6.0D;
                    attemptY = targetedEntity.getY() + this.getRandom().nextGaussian() * 6.0D;
                    attemptZ = targetedEntity.getZ() + this.getRandom().nextGaussian() * 6.0D;

                    BlockPos areaPos = new BlockPos((int) attemptX, (int) attemptY, (int) attemptZ);

                    if (this.level().getBlockState(areaPos).isAir()) {
                        this.gameEvent(GameEvent.ENTITY_PLACE, entity);
                        if (entity instanceof Mob mob) {
                            if (this.level() instanceof ServerLevelAccessor levelAccessor) {
                                mob.finalizeSpawn(levelAccessor, levelAccessor.getCurrentDifficultyAt(areaPos), MobSpawnType.MOB_SUMMONED, null, null);
                                if (mob.getTeam() instanceof PlayerTeam playerTeam) {
                                    levelAccessor.getLevel().getScoreboard().addPlayerToTeam(this.getStringUUID(), playerTeam);
                                }
                            }
                            mob.setTarget(this.getTarget());

                        }
                        entity.setPos(areaPos.getX(), areaPos.getY(), areaPos.getZ());

                        this.level().addFreshEntity(entity);
                        break;
                    }
                }
            }
        }
    }


    public class SummonSpellGoal extends Goal {
        private final Predicate<LivingEntity> fillter = (entity) -> {
            return !(entity instanceof OrbOfDominance) && entity instanceof Enemy;
        };

        protected int attackWarmupDelay;
        protected int nextAttackTickCount;

        public SummonSpellGoal(){
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (OrbOfDominance.this.getTarget() == null) {
                return false;
            } else if (OrbOfDominance.this.getEnchantTarget() != null) {
                return false;
            } else if (OrbOfDominance.this.tickCount < this.nextAttackTickCount) {
                return false;
            } else {
                if(this.canUseBase()){
                    List<LivingEntity> list = OrbOfDominance.this.level().getEntitiesOfClass(LivingEntity.class, OrbOfDominance.this.getBoundingBox().expandTowards(32.0D, 32.0D, 32.0D), fillter);

                    //set enchant limit
                    if (list.size() < 16) {
                         OrbOfDominance.this.level().broadcastEntityEvent(OrbOfDominance.this, (byte) 61);
                         return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }


        public boolean canUseBase() {
            LivingEntity livingentity = OrbOfDominance.this.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                return OrbOfDominance.this.tickCount >= this.nextAttackTickCount;
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return this.attackWarmupDelay > 0;
        }

        @Override
        public void start() {
            super.start();
            this.attackWarmupDelay = this.adjustedTickDelay(this.getCastWarmupTime());
            OrbOfDominance.this.spellCastingTickCount = this.getCastingTime();
            this.nextAttackTickCount = OrbOfDominance.this.tickCount + this.getCastingInterval();
            SoundEvent soundevent = SoundEvents.RESPAWN_ANCHOR_CHARGE;
            if (soundevent != null) {
                OrbOfDominance.this.playSound(soundevent, OrbOfDominance.this.getSoundVolume(), 0.5F);
            }
        }

        @Override
        public void tick() {
            super.tick();
            --this.attackWarmupDelay;
            if (this.attackWarmupDelay == 0) {
                this.performSpellCasting();
                OrbOfDominance.this.playSound(SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, OrbOfDominance.this.getSoundVolume(), 1.0F);
            }

        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            super.stop();
        }

        protected void performSpellCasting() {
            OrbOfDominance.this.summonMobAt(OrbOfDominance.this);
        }

        protected int getCastWarmupTime() {
            return 40;
        }

        protected int getCastingTime() {
            return 60;
        }

        protected int getCastingInterval() {
            return 300;
        }
    }

    public class SpellGoal extends Goal {
        private final Predicate<LivingEntity> fillter = (entity) -> {
            return !(entity instanceof OrbOfDominance) && entity instanceof IEnchantCap enchantCap && !enchantCap.getEnchantCap().hasEnchant();
        };

        private final Predicate<LivingEntity> enchanted_fillter = (entity) -> {
            return !(entity instanceof OrbOfDominance) && entity instanceof IEnchantCap enchantCap && enchantCap.getEnchantCap().hasEnchant();
        };

        protected int attackWarmupDelay;
        protected int nextAttackTickCount;

        public SpellGoal(){
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (OrbOfDominance.this.getTarget() == null) {
                return false;
            } else if (OrbOfDominance.this.tickCount < this.nextAttackTickCount) {
                return false;
            } else {
                List<LivingEntity> list = OrbOfDominance.this.level().getEntitiesOfClass(LivingEntity.class, OrbOfDominance.this.getBoundingBox().expandTowards(16.0D, 16.0D, 16.0D), this.fillter);
                if (list.isEmpty()) {
                    return false;
                } else if(this.canUseBase()){
                    List<LivingEntity> enchanted_list = OrbOfDominance.this.level().getEntitiesOfClass(LivingEntity.class, OrbOfDominance.this.getBoundingBox().expandTowards(16.0D, 16.0D, 16.0D), this.enchanted_fillter);

                    //set enchant limit
                    if (enchanted_list.size() < 12) {
                        LivingEntity target = list.get(OrbOfDominance.this.random.nextInt(list.size()));
                        if (target != OrbOfDominance.this.getTarget() && target != OrbOfDominance.this && (target.getTeam() == OrbOfDominance.this.getTeam() || target instanceof Enemy && target.getTeam() == null)) {
                            OrbOfDominance.this.setEnchantTarget(target);
                            OrbOfDominance.this.level().broadcastEntityEvent(OrbOfDominance.this, (byte) 61);
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }


        public boolean canUseBase() {
            LivingEntity livingentity = OrbOfDominance.this.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                return OrbOfDominance.this.tickCount >= this.nextAttackTickCount;
            } else {
                return false;
            }
        }

        public boolean canContinueToUseBase() {
            return this.attackWarmupDelay > 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return canContinueToUseBase() && OrbOfDominance.this.getEnchantTarget() != null && OrbOfDominance.this.getEnchantTarget() != OrbOfDominance.this.getTarget() && this.attackWarmupDelay > 0;
        }

        @Override
        public void start() {
            super.start();
            this.attackWarmupDelay = this.adjustedTickDelay(this.getCastWarmupTime());
            OrbOfDominance.this.spellCastingTickCount = this.getCastingTime();
            this.nextAttackTickCount = OrbOfDominance.this.tickCount + this.getCastingInterval();
            SoundEvent soundevent = SoundEvents.ENDERMAN_AMBIENT;
            if (soundevent != null) {
                OrbOfDominance.this.playSound(soundevent, OrbOfDominance.this.getSoundVolume(), 0.5F);
            }
        }

        @Override
        public void tick() {
            super.tick();
            --this.attackWarmupDelay;
            if (this.attackWarmupDelay == 0) {
                this.performSpellCasting();
                OrbOfDominance.this.playSound(ModSoundEvents.ENCHANTER_SPELL.get(), OrbOfDominance.this.getSoundVolume(), 1.0F);
            }

        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            super.stop();
            OrbOfDominance.this.setEnchantTarget(null);
        }

        protected void performSpellCasting() {
            LivingEntity entity = OrbOfDominance.this.getEnchantTarget();
            if (entity != null && entity.isAlive()) {
                if (entity instanceof IEnchantCap cap) {
                    MobEnchantUtils.addUnstableRandomEnchantmentToEntity(entity, OrbOfDominance.this, cap, entity.getRandom(), 12, false, false);
                }
            }
        }

        protected int getCastWarmupTime() {
            return 40;
        }

        protected int getCastingTime() {
            return 60;
        }

        protected int getCastingInterval() {
            return 100;
        }
    }
}
