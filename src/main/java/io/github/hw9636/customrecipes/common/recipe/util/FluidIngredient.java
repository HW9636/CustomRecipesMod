package io.github.hw9636.customrecipes.common.recipe.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.hw9636.customrecipes.common.util.ArrayUtils;
import io.github.hw9636.customrecipes.common.util.FunctionUtils;
import io.github.hw9636.customrecipes.common.util.ListUtils;
import io.github.hw9636.customrecipes.common.util.StreamUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class FluidIngredient implements Predicate<FluidState> {

    public FluidIngredient(Fluid pFluid) {
        this(new Value.SingleValue(pFluid));
    }

    public FluidIngredient(TagKey<Fluid> pTag) {
        this(new Value.TagValue(pTag));
    }

    public FluidIngredient(List<Fluid> pFluids) {
        this(new Value.ListValue(pFluids));
    }

    private final Value[] values;
    private final Fluid[] fluids;

    public FluidIngredient(Value... pValues) {
        if (pValues.length == 0) throw new IllegalArgumentException("At least one value must be provided");

        this.values = pValues;
        this.fluids = ListUtils.joinLists(ArrayUtils.mapAll(values, Value::getValues)).toArray(Fluid[]::new);
    }

    public List<Fluid> getAllFluids() {
        return Arrays.asList(fluids);
    }

    @Override
    public boolean test(FluidState pFluid) {
        if (this.fluids.length == 0) return true;
        for (Fluid f : this.fluids) if (pFluid.is(f)) return true;
        return false;
    }

    public int length() {
        return fluids.length;
    }

    public static io.github.hw9636.customrecipes.common.recipe.util.FluidIngredient fromJson(JsonObject pJson) {
        List<Value> values = new ArrayList<>();

        if (pJson.has("fluid")) values.add(Value.SingleValue.fromString(GsonHelper.getAsString(pJson, "fluid")));
        if (pJson.has("tag")) values.add(Value.TagValue.fromString(GsonHelper.getAsString(pJson, "tag")));
        if (pJson.has("fluids")) values.add(Value.ListValue.fromJsonArray(GsonHelper.getAsJsonArray(pJson, "fluids")));

        return new FluidIngredient(values.toArray(Value[]::new));
    }

    public void toNetwork(FriendlyByteBuf pBuffer) {
        FluidIngredient.toNetwork(pBuffer, this);
    }

    public static FluidIngredient fromNetwork(FriendlyByteBuf pBuffer) {
        int amount = pBuffer.readInt();
        Value[] values = new Value[amount];

        for (int i = 0;i<amount;i++) {
            switch (pBuffer.readByte()) {
                case Value.TAG -> {
                    int tagLen = pBuffer.readInt();
                    String tag = (String) pBuffer.readCharSequence(tagLen, StandardCharsets.UTF_8);
                    values[i] = Value.TagValue.fromString(tag);
                }
                case Value.SINGLE -> {
                    int blockLen = pBuffer.readInt();
                    String block = (String) pBuffer.readCharSequence(blockLen, StandardCharsets.UTF_8);
                    values[i] = Value.SingleValue.fromString(block);
                }
                case Value.LIST -> {
                    int listLen = pBuffer.readInt();
                    List<Fluid> fluids = new ArrayList<>(listLen);
                    for (int j = 0; j < listLen; j++) {
                        int strLen = pBuffer.readInt();
                        fluids.add(ForgeRegistries.FLUIDS.getValue(
                                new ResourceLocation((String) pBuffer.readCharSequence(strLen, StandardCharsets.UTF_8))));
                    }
                    values[i] = new Value.ListValue(fluids);
                }
            }
        }

        return new FluidIngredient(values);
    }

    public static void toNetwork(FriendlyByteBuf pBuffer, FluidIngredient pFluidIngredient) {
        pBuffer.writeInt(pFluidIngredient.values.length);

        for (int i = 0;i<pFluidIngredient.values.length;i++) {
            pFluidIngredient.values[i].toNetwork(pBuffer);
        }
    }

    @SuppressWarnings("unused")
    public interface Value {

        byte TAG = 0x01;
        byte SINGLE = 0x02;
        byte LIST = 0x03;

        List<Fluid> getValues();
        byte getType();
        void toNetwork(FriendlyByteBuf pBuffer);

        class TagValue implements Value {

            private final TagKey<Fluid> tag;

            public TagValue(TagKey<Fluid> pTag) {
                this.tag = pTag;
            }

            @Override
            public byte getType() {
                return TAG;
            }

            @SuppressWarnings("deprecation")
            @Override
            public List<Fluid> getValues() {
                return StreamUtils.fromIterable(Registry.FLUID.getTagOrEmpty(this.tag)).map(Holder::value).toList();
            }

            public static TagValue fromString(String tag) {
                return new TagValue(TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(tag)));
            }

            @Override
            public void toNetwork(FriendlyByteBuf pBuffer) {
                pBuffer.writeByte(getType());
                String tag = this.tag.location().toString();
                pBuffer.writeInt(tag.length());
                pBuffer.writeCharSequence(tag, StandardCharsets.UTF_8);
            }
        }

        class SingleValue implements Value {
            private final Fluid fluid;

            public SingleValue(Fluid pFluid) {
                this.fluid = pFluid;
            }

            @Override
            public byte getType() {
                return SINGLE;
            }

            @Override
            public List<Fluid> getValues() {
                return List.of(fluid);
            }

            public static SingleValue fromString(String pString) {
                return new SingleValue(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(pString)));
            }

            @Override
            public void toNetwork(FriendlyByteBuf pBuffer) {
                pBuffer.writeByte(getType());
                ResourceLocation rl = ForgeRegistries.FLUIDS.getKey(this.fluid);
                String tag = rl != null ? rl.getPath() : "minecraft:air";
                pBuffer.writeInt(tag.length());
                pBuffer.writeCharSequence(tag, StandardCharsets.UTF_8);
            }
        }

        class ListValue implements Value {
            private final List<Fluid> fluids;

            public ListValue(List<Fluid> pBlocks) {
                this.fluids = pBlocks;
            }

            @Override
            public byte getType() {
                return LIST;
            }

            @Override
            public List<Fluid> getValues() {
                return fluids;
            }

            public static ListValue fromJsonArray(JsonArray pJsonArray) {
                return new ListValue(StreamUtils.fromIterable(pJsonArray).map(JsonElement::getAsString)
                        .map(ResourceLocation::new).map(FunctionUtils.nonNullFunction(ForgeRegistries.FLUIDS::getValue,
                                Fluids.EMPTY)).toList());
            }

            @Override
            public void toNetwork(FriendlyByteBuf pBuffer) {
                pBuffer.writeByte(getType());
                pBuffer.writeInt(fluids.size());
                for (Fluid f : fluids) {
                    ResourceLocation rl = ForgeRegistries.FLUIDS.getKey(f);
                    String id = rl != null ? rl.toString() : "minecraft:air";
                    pBuffer.writeInt(id.length());
                    pBuffer.writeCharSequence(id, StandardCharsets.UTF_8);
                }
            }
        }
    }
}