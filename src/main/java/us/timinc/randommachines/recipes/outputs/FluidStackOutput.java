package us.timinc.randommachines.recipes.outputs;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackOutput extends Output<FluidStack> {
  public String name = "";
  public int amount = 0;

  @Override
  public FluidStack create() {
    FluidStack createdFluidStack = new FluidStack(FluidRegistry.getFluid(name), amount);
    return createdFluidStack;
  }
}
