package us.timinc.randommachines.recipes.outputs;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

public class ItemStackOutput extends Output<ItemStack> {
  public String name = "";
  public int amount = 0;
  public int damage = 0;
  public String tag = "";

  @Override
  public ItemStack create() {
    if (amount == 0) {
      return ItemStack.EMPTY;
    }

    ItemStack createdItemStack = new ItemStack(Item.getByNameOrId(name));
    createdItemStack.setCount(amount);
    createdItemStack.setItemDamage(damage);
    if (!tag.isEmpty()) {
      try {
        createdItemStack.setTagCompound(JsonToNBT.getTagFromJson(tag));
      } catch (NBTException e) {
        System.err.println("Could not parse tag.");
        e.printStackTrace();
      }

    }
    return createdItemStack;
  }
}
