package us.timinc.randommachines.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import us.timinc.randommachines.RandomMachines;
import us.timinc.randommachines.block.JarBlock;
import us.timinc.randommachines.recipes.JarRecipe;
import us.timinc.randommachines.recipes.ingredients.JarIngredientWrapper;
import us.timinc.randommachines.recipes.outputs.Output;

import javax.annotation.Nullable;
import java.util.Map;

public class JarEntity extends TileEntity implements ITickable {
  private final FluidTank fluidStorage = new FluidTank(1000) {
    @Override
    protected void onContentsChanged() {
      super.onContentsChanged();
      markDirty();
    }
  };
  private final ItemStackHandler itemStorage = new ItemStackHandler() {
    @Override
    protected void onContentsChanged(int slot) {
      super.onContentsChanged(slot);
      markDirty();
    }
  };
  private final ItemStackHandler lidStorage = new ItemStackHandler() {
    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }

    @Override
    protected void onContentsChanged(int slot) {
      super.onContentsChanged(slot);
      markDirty();
    }
  };
  private int progress = 0;

  @Override
  public void update() {
    if (hasLid()) {
      JarRecipe matching = (JarRecipe) RandomMachines.instance.recipes.findMatching(getIngredientWrapper());
      if (matching != null) {
        progress++;
        if (progress > matching.duration) {
          fluidStorage.drain(1000, true);
          itemStorage.setStackInSlot(0, ItemStack.EMPTY);

          Map<String, Output> outputs = matching.getOutputs();
          fluidStorage.fill((FluidStack) outputs.get("fluid").create(), true);
          itemStorage.setStackInSlot(0, (ItemStack) outputs.get("item").create());

          if (world != null) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(JarBlock.VARIANT, 2));
          }
        }
      }
    }
  }

  public ItemStack addLid(ItemStack lid) {
    progress = 0;
    return ItemHandlerHelper.insertItem(lidStorage, lid, false);
  }

  public ItemStack removeLid() {
    ItemStack itemStack = lidStorage.getStackInSlot(0);
    lidStorage.setStackInSlot(0, ItemStack.EMPTY);
    return itemStack;
  }

  public ItemStack getLid() {
    return lidStorage.getStackInSlot(0);
  }

  public boolean hasLid() {
    return !getLid().isEmpty();
  }

  public ItemStack addItemIngredient(ItemStack itemStack) {
    return ItemHandlerHelper.insertItem(itemStorage, itemStack, false);
  }

  public ItemStack removeItemIngredient() {
    ItemStack itemStack = itemStorage.getStackInSlot(0);
    itemStorage.setStackInSlot(0, ItemStack.EMPTY);
    return itemStack;
  }

  public boolean hasItemIngredient() {
    return !getItemIngredient().isEmpty();
  }

  public ItemStack getItemIngredient() {
    return itemStorage.getStackInSlot(0);
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
  }

  @Override
  @Nullable
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
  {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
    {
      return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidStorage);
    }
    return super.getCapability(capability, facing);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    if (compound.hasKey("itemStorage")) {
      itemStorage.deserializeNBT(compound.getCompoundTag("itemStorage"));
    }
    if (compound.hasKey("lidStorage")) {
      lidStorage.deserializeNBT(compound.getCompoundTag("lidStorage"));
    }
    if (compound.hasKey("progress")) {
      progress = compound.getInteger("progress");
    }
    fluidStorage.readFromNBT(compound);
    super.readFromNBT(compound);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    if (hasItemIngredient()) {
      compound.setTag("itemStorage", itemStorage.serializeNBT());
    }
    if (hasLid()) {
      compound.setTag("lidStorage", lidStorage.serializeNBT());
    }
    fluidStorage.writeToNBT(compound);
    compound.setInteger("progress", progress);
    return compound;
  }

  public JarIngredientWrapper getIngredientWrapper() {
    return new JarIngredientWrapper(
      fluidStorage.getFluid(),
      getItemIngredient()
    );
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    return false;
  }
}
