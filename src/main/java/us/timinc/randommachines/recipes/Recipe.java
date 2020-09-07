package us.timinc.randommachines.recipes;

import us.timinc.randommachines.recipes.ingredients.IngredientWrapper;
import us.timinc.randommachines.recipes.outputs.Output;

import java.util.Map;

public abstract class Recipe<T extends IngredientWrapper> {
  public String type;

  public abstract boolean matches(T ingredientWrapper);

  public abstract Map<String, Output> getOutputs();
}
