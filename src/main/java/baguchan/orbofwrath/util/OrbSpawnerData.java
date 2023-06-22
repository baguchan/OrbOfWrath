package baguchan.orbofwrath.util;

import baguchan.orbofwrath.OrbOfWrath;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class OrbSpawnerData extends WeightedEntry.IntrusiveBase {
    public static final Codec<OrbSpawnerData> CODEC = ExtraCodecs.validate(RecordCodecBuilder.create((p_275169_) -> {
        return p_275169_.group(BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter((p_151826_) -> {
            return p_151826_.type;
        }), Weight.CODEC.fieldOf("weight").forGetter(WeightedEntry.IntrusiveBase::getWeight), ExtraCodecs.POSITIVE_INT.fieldOf("minCount").forGetter((p_151824_) -> {
            return p_151824_.minCount;
        }), ExtraCodecs.POSITIVE_INT.fieldOf("maxCount").forGetter((p_151820_) -> {
            return p_151820_.maxCount;
        }), ResourceLocation.CODEC.fieldOf("orb_type_id").forGetter(orbType -> orbType.orbTypeId))
                .apply(p_275169_, OrbSpawnerData::new);
    }), (p_275168_) -> {
        return p_275168_.minCount > p_275168_.maxCount ? DataResult.error(() -> {
            return "minCount needs to be smaller or equal to maxCount";
        }) : DataResult.success(p_275168_);
    });
    public final EntityType<?> type;
    public final int minCount;
    public final int maxCount;
    public final ResourceLocation orbTypeId;

    public static final ResourceKey<Registry<OrbSpawnerData>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(new ResourceLocation(OrbOfWrath.MODID, "orb_spawn_type"));

    public OrbSpawnerData(EntityType<?> p_48409_, int p_48410_, int p_48411_, int p_48412_, ResourceLocation resourceLocation) {
        this(p_48409_, Weight.of(p_48410_), p_48411_, p_48412_, resourceLocation);
    }

    public OrbSpawnerData(EntityType<?> p_151815_, Weight p_151816_, int p_151817_, int p_151818_, ResourceLocation resourceLocation) {
        super(p_151816_);
        this.type = p_151815_.getCategory() == MobCategory.MISC ? EntityType.PIG : p_151815_;
        this.minCount = p_151817_;
        this.maxCount = p_151818_;
        this.orbTypeId = resourceLocation;
    }

    public String toString() {
        return EntityType.getKey(this.type) + "*(" + this.minCount + "-" + this.maxCount + "):" + this.getWeight();
    }
}