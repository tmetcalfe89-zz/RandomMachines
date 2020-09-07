package us.timinc.randommachines.recipes.parts;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.regex.Pattern;

public class ItemStackPart extends Part<ItemStack> {
  public String name = "";
  public int damage = 0;
  public int amount = 0;

  @Override
  public boolean matches(ItemStack itemStack) {
    if (itemStack.equals(ItemStack.EMPTY)) {
      return amount == 0;
    }

    if (itemStack.getCount() < amount) {
      return false;
    }

    String gameObject = getItemId(itemStack);
    String recipeObject = name + ":" + damage;

    String fixedRecipeObject = recipeObject.replaceAll("\\*", ".*");
    if (!gameObject.equals("minecraft:air:0") && recipeObject.startsWith("ore:")) {
      int[] x = OreDictionary.getOreIDs(createItemStackFrom(gameObject, 1));
      for (int i : x) {
        if (Pattern.matches(fixedRecipeObject, "ore:" + OreDictionary.getOreName(i) + ":0")) {
          return true;
        }
      }
      return false;
    }
    return Pattern.matches(fixedRecipeObject, gameObject);
  }

  public static String getItemId(ItemStack itemStack) {
    Item item = itemStack.getItem();
    return item.getRegistryName().toString() + ":" + itemStack.getMetadata();
  }

  public static ItemStack createItemStackFrom(String itemId, int count) {
    String[] splitDropItemId = itemId.split(":");
    ItemStack newItemStack = new ItemStack(Item.getByNameOrId(splitDropItemId[0] + ":" + splitDropItemId[1]),
        count);
    newItemStack.setItemDamage(Integer.parseInt(splitDropItemId[2]));
    return newItemStack;
  }
}
