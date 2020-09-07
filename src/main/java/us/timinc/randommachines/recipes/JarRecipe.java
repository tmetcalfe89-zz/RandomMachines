package us.timinc.randommachines.recipes;

import us.timinc.randommachines.recipes.outputs.FluidStackOutput;
import us.timinc.randommachines.recipes.outputs.ItemStackOutput;
import us.timinc.randommachines.recipes.outputs.Output;
import us.timinc.randommachines.recipes.parts.FluidStackPart;
import us.timinc.randommachines.recipes.parts.ItemStackPart;
import us.timinc.randommachines.recipes.ingredients.JarIngredientWrapper;

import java.util.HashMap;
import java.util.Map;

public class JarRecipe extends Recipe<JarIngredientWrapper> {
  public FluidStackPart fluidInput = new FluidStackPart();
  public ItemStackPart itemInput = new ItemStackPart();
  public FluidStackOutput fluidOutput = new FluidStackOutput();
  public ItemStackOutput itemOutput = new ItemStackOutput();
  public int duration = 200;

  @Override
  public boolean matches(JarIngredientWrapper ingredientWrapper) {
    return ingredientWrapper.type.equals("jar")
        && fluidInput.matches(ingredientWrapper.fluidStack)
        && itemInput.matches(ingredientWrapper.itemStack);
  }

  @Override
  public Map<String, Output> getOutputs() {
    HashMap<String, Output> outputs = new HashMap<>();
    outputs.put("fluid", fluidOutput);
    outputs.put("item", itemOutput);
    return outputs;
  }
}
