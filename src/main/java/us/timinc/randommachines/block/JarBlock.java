package us.timinc.randommachines.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.timinc.randommachines.ModItems;
import us.timinc.randommachines.RandomMachines;
import us.timinc.randommachines.recipes.Recipe;
import us.timinc.randommachines.tileentity.JarEntity;

import javax.annotation.Nullable;

public class JarBlock extends Block {
  public JarBlock() {
    super(Material.ROCK, MapColor.ADOBE);
    setHardness(1.5f);
    setResistance(6);
    setSoundType(SoundType.STONE);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new JarEntity();
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (!world.isRemote) {
      JarEntity jarEntity = (JarEntity) world.getTileEntity(pos);
      ItemStack heldItemStack = player.getHeldItem(hand);

      if (heldItemStack.isEmpty()) {
        if (jarEntity.hasLid()) {
          System.out.println("Retrieving lid.");

          ItemStack lid = jarEntity.removeLid();

          if (!player.inventory.addItemStackToInventory(lid)) {
            EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), lid);
            world.spawnEntity(entityItem);
          } else {
            player.openContainer.detectAndSendChanges();
          }
          return true;
        }

        if (jarEntity.hasItemIngredient()) {
          System.out.println("Retrieving item.");

          ItemStack stack = jarEntity.removeItemIngredient();

          if (!player.inventory.addItemStackToInventory(stack)) {
            EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), stack);
            world.spawnEntity(entityItem);
          } else {
            player.openContainer.detectAndSendChanges();
          }
          return true;
        }
      } else {
        if (FluidUtil.getFluidHandler(heldItemStack) != null) {
          System.out.println("Interact with fluid item.");
          FluidUtil.interactWithFluidHandler(player, hand, world, pos, facing);
        } else {
          if (heldItemStack.getItem().equals(ModItems.lidItem)) {
            Recipe matching = RandomMachines.instance.recipes.findMatching(jarEntity.getIngredientWrapper());
            if (matching != null) {
              System.out.println("Adding lid.");

              player.inventory.setInventorySlotContents(player.inventory.currentItem, jarEntity.addLid(heldItemStack));
            }
          } else {
            System.out.println("Adding item.");

            player.inventory.setInventorySlotContents(player.inventory.currentItem, jarEntity.addItemIngredient(heldItemStack));
          }
          player.openContainer.detectAndSendChanges();
        }
      }
    }

    return true;
  }

  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {
    JarEntity jarEntity = (JarEntity) world.getTileEntity(pos);

    ItemStack stack = jarEntity.removeItemIngredient();
    EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), stack);
    world.spawnEntity(entityItem);

    stack = jarEntity.removeLid();
    entityItem = new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), stack);
    world.spawnEntity(entityItem);

    super.breakBlock(world, pos, state);
  }

  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
  }
}
