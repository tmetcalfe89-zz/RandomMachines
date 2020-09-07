package us.timinc.randommachines.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import us.timinc.randommachines.ModBlocks;
import us.timinc.randommachines.RandomMachines;
import us.timinc.randommachines.block.JarBlock;
import us.timinc.randommachines.tileentity.JarEntity;

@Mod.EventBusSubscriber
public class CommonProxy {
  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> e) {
    e.getRegistry().register(new JarBlock().setRegistryName(RandomMachines.MODID, "jar").setUnlocalizedName(RandomMachines.MODID + ".jar"));

    GameRegistry.registerTileEntity(JarEntity.class, new ResourceLocation("randommachines:jarentity"));
  }

  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> e) {
    e.getRegistry().register(new Item().setRegistryName(RandomMachines.MODID, "lid").setUnlocalizedName(RandomMachines.MODID + ".lid"));

    e.getRegistry().register(new ItemBlock(ModBlocks.jarBlock).setRegistryName(ModBlocks.jarBlock.getRegistryName()));
  }

  public void postInit(FMLPostInitializationEvent e) {}
}
