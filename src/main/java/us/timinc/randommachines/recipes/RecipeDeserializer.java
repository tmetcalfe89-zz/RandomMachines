package us.timinc.randommachines.recipes;

import com.google.gson.*;
import us.timinc.randommachines.RandomMachines;

import java.lang.reflect.Type;
import java.util.HashMap;

public class RecipeDeserializer implements JsonDeserializer<JarRecipe> {
  private static HashMap<String, Type> recipeClasses = new HashMap<>();

  public static void registerDescription(String name, Type type) {
    RandomMachines.LOG("Registering recipe deserializer: %s", name);
    recipeClasses.put(name, type);
  }

  @Override
  public JarRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    String type = jsonObject.get("type").getAsString();
    RandomMachines.LOG("Deserializing recipe: %s", type);
    return context.deserialize(jsonObject, recipeClasses.get(type));
  }
}
