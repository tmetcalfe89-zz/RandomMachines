package us.timinc.randommachines.recipes.parts;

public abstract class Part<T> {
  public abstract boolean matches(T ingredientWrapper);
}
