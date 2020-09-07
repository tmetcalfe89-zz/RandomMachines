package us.timinc.randommachines.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
  public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 2);
  private static final AxisAlignedBB AABB_JAR = new AxisAlignedBB(3.5D / 16, 0.0D, 3.5D / 16, 12.5D / 16, 12.0D / 16, 12.5D / 16);

  public JarBlock() {
    super(Material.ROCK, MapColor.ADOBE);
    setHardness(1.5f);
    setResistance(6);
    setSoundType(SoundType.STONE);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, 0));
  }

  @Override
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && worldIn.isSideSolid(pos.down(), EnumFacing.UP);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB_JAR;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, VARIANT);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(VARIANT, meta);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(VARIANT);
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

          world.setBlockState(pos, state.withProperty(VARIANT, 0));
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
      } else if (!jarEntity.hasLid()) {
        if (FluidUtil.getFluidHandler(heldItemStack) != null) {
          System.out.println("Interact with fluid item.");
          FluidUtil.interactWithFluidHandler(player, hand, world, pos, facing);
        } else {
          if (heldItemStack.getItem().equals(ModItems.lidItem)) {
            Recipe matching = RandomMachines.instance.recipes.findMatching(jarEntity.getIngredientWrapper());
            if (matching != null) {
              System.out.println("Adding lid.");

              player.inventory.setInventorySlotContents(player.inventory.currentItem, jarEntity.addLid(heldItemStack));
              world.setBlockState(pos, state.withProperty(VARIANT, 1));
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
