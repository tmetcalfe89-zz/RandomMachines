package us.timinc.randommachines.recipes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import us.timinc.randommachines.recipes.ingredients.IngredientWrapper;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Recipes {
  private ArrayList<Recipe> recipes = new ArrayList<>();
  private Gson gson;

  public Recipes() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Recipe.class, new RecipeDeserializer());
    gson = gsonBuilder.create();

    loadRecipes();
  }

  private void loadRecipes() {
    File globalDir = new File("config/recipes/randommachines/");
    if (!globalDir.exists())
      globalDir.mkdirs();
    String[] files = globalDir.list();
    files = Arrays.stream(files).filter(x -> x.endsWith(".json")).toArray(String[]::new);
    for (int i = 0; i < files.length; i++) {
      try {
        addRecipesFrom(new File(globalDir, files[i]));
      } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  private void addRecipesFrom(File file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
    Recipe[] newRecipes = gson.fromJson(new FileReader(file), Recipe[].class);
    for (int i = 0; i < newRecipes.length; i++) {
      add(newRecipes[i]);
    }
  }

  private void add(Recipe recipe) {
    recipes.add(recipe);
  }

  public void reload() {
    recipes = new ArrayList<>();
    loadRecipes();
  }

  public Object getRecipeCount() {
    return recipes.size();
  }

  @Nullable
  public Recipe findMatching(IngredientWrapper ingredient) {
    for (Recipe recipe : recipes) {
      if (recipe.matches(ingredient)) {
        return recipe;
      }
    }
    return null;
  }
}
