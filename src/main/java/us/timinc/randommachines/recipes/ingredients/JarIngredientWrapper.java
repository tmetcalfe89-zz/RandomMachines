package us.timinc.randommachines.recipes.ingredients;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class JarIngredientWrapper extends IngredientWrapper {
  public FluidStack fluidStack;
  public ItemStack itemStack;

  public JarIngredientWrapper(FluidStack fluidStack, ItemStack itemStack) {
    this.type = "jar";
    this.fluidStack = fluidStack;
    this.itemStack = itemStack;
  }
}
