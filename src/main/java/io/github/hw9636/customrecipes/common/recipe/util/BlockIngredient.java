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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class BlockIngredient implements Predicate<Block> {

    public BlockIngredient(Block pBlock) {
        this(new Value.SingleValue(pBlock));
    }

    public BlockIngredient(TagKey<Block> pTag) {
        this(new Value.TagValue(pTag));
    }

    public BlockIngredient(List<Block> pBlocks) {
        this(new Value.ListValue(pBlocks));
    }

    private final Value[] values;
    private final Block[] blocks;

    public BlockIngredient(Value ... pValues) {
        if (pValues.length == 0) throw new IllegalArgumentException("At least one value must be provided");

        this.values = pValues;
        this.blocks = ListUtils.joinLists(ArrayUtils.mapAll(values, Value::getValues)).toArray(Block[]::new);
    }

    public List<Block> getAllBlocks() {
        return Arrays.asList(blocks);
    }

    @Override
    public boolean test(Block block) {
        if (this.blocks.length == 0) return true;
        for (Block b : this.blocks) if (block == b) return true;
        return false;
    }

    public int length() {
        return blocks.length;
    }

    public static BlockIngredient fromJson(JsonObject pJson) {
        List<Value> values = new ArrayList<>();

        if (pJson.has("block")) values.add(Value.SingleValue.fromString(GsonHelper.getAsString(pJson, "block")));
        if (pJson.has("tag")) values.add(Value.TagValue.fromString(GsonHelper.getAsString(pJson, "tag")));
        if (pJson.has("blocks")) values.add(Value.ListValue.fromJsonArray(GsonHelper.getAsJsonArray(pJson, "blocks")));

        return new BlockIngredient(values.toArray(Value[]::new));
    }

    public void toNetwork(FriendlyByteBuf pBuffer) {
        BlockIngredient.toNetwork(pBuffer, this);
    }

    public static BlockIngredient fromNetwork(FriendlyByteBuf pBuffer) {
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
                    List<Block> blocks = new ArrayList<>(listLen);
                    for (int j = 0; j < listLen; j++) {
                        int strLen = pBuffer.readInt();
                        blocks.add(ForgeRegistries.BLOCKS.getValue(
                                new ResourceLocation((String) pBuffer.readCharSequence(strLen, StandardCharsets.UTF_8))));
                    }
                    values[i] = new Value.ListValue(blocks);
                }
            }
        }

        return new BlockIngredient(values);
    }

    public static void toNetwork(FriendlyByteBuf pBuffer, BlockIngredient pBlockIngredient) {
        pBuffer.writeInt(pBlockIngredient.values.length);

        for (int i = 0;i<pBlockIngredient.values.length;i++) {
            pBlockIngredient.values[i].toNetwork(pBuffer);
        }
    }

    @SuppressWarnings("unused")
    public interface Value {

        byte TAG = 0x01;
        byte SINGLE = 0x02;
        byte LIST = 0x03;

        List<Block> getValues();
        byte getType();
        void toNetwork(FriendlyByteBuf pBuffer);

        class TagValue implements Value {

            private final TagKey<Block> tag;

            public TagValue(TagKey<Block> pTag) {
                this.tag = pTag;
            }

            @Override
            public byte getType() {
                return TAG;
            }

            @SuppressWarnings("deprecation")
            @Override
            public List<Block> getValues() {
                return StreamUtils.fromIterable(Registry.BLOCK.getTagOrEmpty(this.tag)).map(Holder::value).toList();
            }

            public static TagValue fromString(String tag) {
                return new TagValue(TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(tag)));
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
            private final Block block;

            public SingleValue(Block pBlock) {
                this.block = pBlock;
            }

            @Override
            public byte getType() {
                return SINGLE;
            }

            @Override
            public List<Block> getValues() {
                return List.of(block);
            }

            @SuppressWarnings("deprecation")
            public static SingleValue fromString(String pString) {
                return new SingleValue(Registry.BLOCK.get(new ResourceLocation(pString)));
            }

            @Override
            public void toNetwork(FriendlyByteBuf pBuffer) {
                pBuffer.writeByte(getType());
                ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(this.block);
                String tag = rl != null ? rl.getPath() : "minecraft:air";
                pBuffer.writeInt(tag.length());
                pBuffer.writeCharSequence(tag, StandardCharsets.UTF_8);
            }
        }

        class ListValue implements Value {
            private final List<Block> blocks;

            public ListValue(List<Block> pBlocks) {
                this.blocks = pBlocks;
            }

            @Override
            public byte getType() {
                return LIST;
            }

            @Override
            public List<Block> getValues() {
                return blocks;
            }

            public static ListValue fromJsonArray(JsonArray pJsonArray) {
                return new ListValue(StreamUtils.fromIterable(pJsonArray).map(JsonElement::getAsString)
                        .map(ResourceLocation::new).map(FunctionUtils.nonNullFunction(ForgeRegistries.BLOCKS::getValue, Blocks.AIR)).toList());
            }

            @Override
            public void toNetwork(FriendlyByteBuf pBuffer) {
                pBuffer.writeByte(getType());
                pBuffer.writeInt(blocks.size());
                for (Block b : blocks) {
                    ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(b);
                    String id = rl != null ? rl.toString() : "minecraft:air";
                    pBuffer.writeInt(id.length());
                    pBuffer.writeCharSequence(id, StandardCharsets.UTF_8);
                }
            }
        }
    }
}
