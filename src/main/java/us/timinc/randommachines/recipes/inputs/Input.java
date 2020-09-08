package us.timinc.randommachines.recipes.inputs;

public abstract class Input<T> {
  public abstract boolean matches(T ingredientWrapper);
}
