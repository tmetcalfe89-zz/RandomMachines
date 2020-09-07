package us.timinc.randommachines.recipes.parts;

import net.minecraftforge.fluids.FluidStack;

public class FluidStackPart extends Part<FluidStack> {
  public String name = "";
  public int amount = 0;

  @Override
  public boolean matches(FluidStack fluidStack) {
    return (
        fluidStack.amount == 0
            && amount == 0
    ) || (
        fluidStack.getFluid().getName().equals(name)
            && fluidStack.amount >= amount
    );
  }
}
