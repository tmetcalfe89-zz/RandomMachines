package us.timinc.randommachines.proxy;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import us.timinc.randommachines.ModBlocks;
import us.timinc.randommachines.ModItems;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
  @Override
  public void postInit(FMLPostInitializationEvent e) {
    super.postInit(e);
    ModBlocks.initItemModels();
  }

  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    ModBlocks.initModels();
    ModItems.initModels();
  }
}
