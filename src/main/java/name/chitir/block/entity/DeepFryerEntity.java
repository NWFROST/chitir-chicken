package name.chitir.block.entity;

import name.chitir.ChitirChicken;
import name.chitir.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DeepFryerEntity extends BlockEntity {
    private final DefaultedList<ItemStack> itemsBeingCooked = DefaultedList.ofSize(4, ItemStack.EMPTY);
    private final int[] cookingTimes = new int[6];
    private final int[] cookingTotalTimes = new int[6];
    private final RecipeManager.MatchGetter<Inventory, CampfireCookingRecipe> matchGetter = RecipeManager.createCachedMatchGetter(RecipeType.CAMPFIRE_COOKING);

    public static void litServerTick(World world, BlockPos pos, BlockState state, DeepFryerEntity deepfryer) {
        boolean bl = false;
        for (int i = 0; i < deepfryer.itemsBeingCooked.size(); ++i) {
            SimpleInventory inventory;
            ItemStack itemStack2;
            ItemStack itemStack = deepfryer.itemsBeingCooked.get(i);
            if (itemStack.isEmpty()) continue;
            bl = true;
            int n = i;
            deepfryer.cookingTimes[n] = deepfryer.cookingTimes[n] + 1;
            if (deepfryer.cookingTimes[i] < deepfryer.cookingTotalTimes[i] || !(itemStack2 = deepfryer.matchGetter.getFirstMatch(inventory = new SimpleInventory(itemStack), world).map(recipe -> recipe.craft(inventory, world.getRegistryManager())).orElse(itemStack)).isItemEnabled(world.getEnabledFeatures())) continue;
            ItemScatterer.spawn(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemStack2);
            deepfryer.itemsBeingCooked.set(i, ItemStack.EMPTY);
            world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
        }
        if (bl) {
            CampfireBlockEntity.markDirty(world, pos, state);
        }
    }

    public static void unlitServerTick(World world, BlockPos pos, BlockState state, DeepFryerEntity deepfryer) {
        boolean bl = false;
        for (int i = 0; i < deepfryer.itemsBeingCooked.size(); ++i) {
            if (deepfryer.cookingTimes[i] <= 0) continue;
            bl = true;
            deepfryer.cookingTimes[i] = MathHelper.clamp(deepfryer.cookingTimes[i] - 2, 0, deepfryer.cookingTotalTimes[i]);
        }
        if (bl) {
            CampfireBlockEntity.markDirty(world, pos, state);
        }
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, DeepFryerEntity deepfryer) {
        int i;
        Random random = world.random;
        if (random.nextFloat() < 0.11f) {
            for (i = 0; i < random.nextInt(2) + 2; ++i) {
                CampfireBlock.spawnSmokeParticle(world, pos, state.get(CampfireBlock.SIGNAL_FIRE), false);
            }
        }
        i = state.get(CampfireBlock.FACING).getHorizontal();
        for (int j = 0; j < deepfryer.itemsBeingCooked.size(); ++j) {
            if (deepfryer.itemsBeingCooked.get(j).isEmpty() || !(random.nextFloat() < 0.2f)) continue;
            Direction direction = Direction.fromHorizontal(Math.floorMod(j + i, 4));
            float f = 0.3125f;
            double d = (double)pos.getX() + 0.5 - (double)((float)direction.getOffsetX() * 0.3125f) + (double)((float)direction.rotateYClockwise().getOffsetX() * 0.3125f);
            double e = (double)pos.getY() + 0.5;
            double g = (double)pos.getZ() + 0.5 - (double)((float)direction.getOffsetZ() * 0.3125f) + (double)((float)direction.rotateYClockwise().getOffsetZ() * 0.3125f);
            for (int k = 0; k < 4; ++k) {
                world.addParticle(ParticleTypes.SMOKE, d, e, g, 0.0, 5.0E-4, 0.0);
            }
        }
    }

    public DefaultedList<ItemStack> getItemsBeingCooked() {
        return this.itemsBeingCooked;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        int[] is;
        super.readNbt(nbt);
        this.itemsBeingCooked.clear();
        Inventories.readNbt(nbt, this.itemsBeingCooked);
        if (nbt.contains("CookingTimes", NbtElement.INT_ARRAY_TYPE)) {
            is = nbt.getIntArray("CookingTimes");
            System.arraycopy(is, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
        }
        if (nbt.contains("CookingTotalTimes", NbtElement.INT_ARRAY_TYPE)) {
            is = nbt.getIntArray("CookingTotalTimes");
            System.arraycopy(is, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, is.length));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.itemsBeingCooked, true);
        nbt.putIntArray("CookingTimes", this.cookingTimes);
        nbt.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, this.itemsBeingCooked, true);
        return nbtCompound;
    }

    public Optional<CampfireCookingRecipe> getRecipeFor(ItemStack stack) {
        if (this.itemsBeingCooked.stream().noneMatch(ItemStack::isEmpty)) {
            return Optional.empty();
        }
        return this.matchGetter.getFirstMatch(new SimpleInventory(stack), this.world);
    }

    public boolean addItem(@Nullable Entity user, ItemStack stack, int cookTime) {
        for (int i = 0; i < this.itemsBeingCooked.size(); ++i) {
            ItemStack itemStack = this.itemsBeingCooked.get(i);
            if (!itemStack.isEmpty()) continue;
            this.cookingTotalTimes[i] = cookTime;
            this.cookingTimes[i] = 0;
            this.itemsBeingCooked.set(i, stack.split(1));
            this.world.emitGameEvent(GameEvent.BLOCK_CHANGE, this.getPos(), GameEvent.Emitter.of(user, this.getCachedState()));
            this.updateListeners();
            return true;
        }
        return false;
    }

    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    public void clear() {
        this.itemsBeingCooked.clear();
    }

    public void spawnItemsBeingCooked() {
        if (this.world != null) {
            this.updateListeners();
        }
    }

    public /* synthetic */ Packet toUpdatePacket() {
        return this.toUpdatePacket();
    }

    public DeepFryerEntity(BlockPos pos, BlockState state) {
        super(DEEPFRYER, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, DeepFryerEntity dfe) {

    }

    public static final BlockEntityType<DeepFryerEntity> DEEPFRYER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier("chitir", "DeepFryerEntity"),
            FabricBlockEntityTypeBuilder.create(DeepFryerEntity::new, ModBlocks.DEEPFRYER).build()
    );
}