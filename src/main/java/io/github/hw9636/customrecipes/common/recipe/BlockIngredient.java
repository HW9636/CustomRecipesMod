package io.github.hw9636.customrecipes.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BlockIngredient implements Predicate<BlockState> {

    public static List<Block> getBlocks(TagKey<Block> pTag) {
        List<Block> blocks = new ArrayList<>();

        //noinspection deprecation
        for (Holder<Block> holder : Registry.BLOCK.getTagOrEmpty(pTag)) {
            blocks.add(holder.value());
        }

        return blocks;
    }

    private final Block[] validBlocks;

    public BlockIngredient(Block pBlock) {
        this(List.of(pBlock));
    }

    public BlockIngredient(TagKey<Block> pTag) {
        this(getBlocks(pTag));
    }

    public BlockIngredient(List<Block> pValidBlocks) {
        this.validBlocks = pValidBlocks.toArray(new Block[0]);
    }

    @Override
    public boolean test(BlockState pBlockState) {
        if (this.validBlocks.length == 0) return true;

        for (Block block : this.validBlocks) {
            if (pBlockState.is(block)) return true;
        }

        return false;
    }

    public Block[] getValidBlocks() {
        return validBlocks;
    }

    public static BlockIngredient fromJson(JsonObject pJson) {
        List<Block> blocks = new ArrayList<>();

        if (pJson.has("block")) {
            //noinspection deprecation
            blocks.add(Registry.BLOCK.get(new ResourceLocation(GsonHelper.getAsString(pJson, "block"))));
        }

        if (pJson.has("tag")) {
            TagKey<Block> blockTag = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(GsonHelper.getAsString(pJson,"tag")));

            //noinspection deprecation
            for (Holder<Block> holder : Registry.BLOCK.getTagOrEmpty(blockTag)) {
                blocks.add(holder.value());
            }
        }

        if (pJson.has("blocks")) {
            JsonElement blocksJson = pJson.get("blocks");

            if (blocksJson.isJsonArray()) {
                blocksJson.getAsJsonArray().forEach(e -> blocks.add(Registry.BLOCK.get(new ResourceLocation(e.getAsString()))));
            }
            else {
                throw new JsonParseException("JsonElement 'Blocks' is not an array");
            }
        }

        return new BlockIngredient(blocks);
    }

    public void toNetwork(FriendlyByteBuf pBuffer) {
        BlockIngredient.toNetwork(this, pBuffer);
    }

   public static BlockIngredient fromNetwork(FriendlyByteBuf pBuffer) {
        int size = pBuffer.readInt();
        List<Block> blocks = new ArrayList<>(size);
        for (int i = 0;i < size;i++) {
            blocks.set(i, Registry.BLOCK.get(new ResourceLocation((String) pBuffer.readCharSequence(pBuffer.readInt(), Charset.defaultCharset()))));
        }

        return new BlockIngredient(blocks);
   }

   public static void toNetwork(BlockIngredient pBlockIngredient, FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(pBlockIngredient.validBlocks.length);
        for (Block b : pBlockIngredient.validBlocks) {
            String rl = b.getRegistryName().toString();
            pBuffer.writeInt(rl.length());
            pBuffer.writeCharSequence(rl, Charset.defaultCharset());
        }
   }
}
