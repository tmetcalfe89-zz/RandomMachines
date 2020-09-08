package us.timinc.randommachines;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
  @GameRegistry.ObjectHolder("randommachines:lid")
  public static Item lidItem;

  @SideOnly(Side.CLIENT)
  public static void initModels() {
    ModelLoader.setCustomModelResourceLocation(lidItem, 0, new ModelResourceLocation(lidItem.getRegistryName(), "inventory"));
  }
}
