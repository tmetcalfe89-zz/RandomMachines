package us.timinc.randommachines.recipes.inputs;

import net.minecraftforge.fluids.FluidStack;

public class FluidStackInput extends Input<FluidStack> {
  public String name = "";
  public int amount = 0;

  @Override
  public boolean matches(FluidStack fluidStack) {
    if (fluidStack == null) {
      return amount == 0;
    }

    return (
        fluidStack.amount == 0
            && amount == 0
    ) || (
        fluidStack.getFluid().getName().equals(name)
            && fluidStack.amount >= amount
    );
  }
}
