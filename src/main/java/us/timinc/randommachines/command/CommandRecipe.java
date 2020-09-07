package us.timinc.randommachines.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import us.timinc.randommachines.RandomMachines;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandRecipe implements ICommand {

  /**
   * Holds the aliases for the root command.
   */
  private final ArrayList aliases;
  /**
   * Holds the tab completes.
   */
  private final ArrayList tabCompletes;

  public CommandRecipe() {
    aliases = new ArrayList();
    aliases.add("rm");

    tabCompletes = new ArrayList();
    tabCompletes.add("reload");
  }

  @Override
  public String getName() {
    return "assets/randommachines";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "interactions <reload>";
  }

  @Override
  public List<String> getAliases() {
    return this.aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length > 0) {
      switch (args[0]) {
        case "reload":
          RandomMachines.instance.recipes.reload();
          sender.sendMessage(new TextComponentString(String.format("Reloaded %s recipes.", RandomMachines.instance.recipes.getRecipeCount())));
          break;
        default:
      }
    }
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
    return tabCompletes;
  }

  @Override
  public boolean isUsernameIndex(String[] args, int index) {
    return false;
  }

  @Override
  public int compareTo(ICommand o) {
    return 0;
  }
}
