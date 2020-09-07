package us.timinc.randommachines;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.timinc.randommachines.block.JarBlock;

public class ModBlocks {
  @GameRegistry.ObjectHolder("randommachines:jar")
  public static JarBlock jarBlock;

  @SideOnly(Side.CLIENT)
  public static void initModels() {
    jarBlock.initModel();
  }

  @SideOnly(Side.CLIENT)
  public static void initItemModels() {
  }
}
