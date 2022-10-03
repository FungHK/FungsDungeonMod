package fung.dungeonmod.commands;

import fung.dungeonmod.features.core.Feature;
import fung.dungeonmod.utils.ConfigUtils;
import fung.dungeonmod.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainCommand extends CommandBase implements ICommand {

    @Override
    public String getCommandName() {
        return "fungdungeonmod";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return EnumChatFormatting.RED + "Usage: /fungdungeonmod <feature> <settings>";
    }

    @Override
    public void processCommand(ICommandSender arg0, String[] arg1) {
        if (arg1.length == 1 && arg1[0].toLowerCase().equals("reloadconfig")) {
            ConfigUtils.reloadConfig();
            return;
        }
        Utils.addChatMessage("&lThanks for using Fung's Dungeon Mod, here is some tips for you:\n" +
                             " &2Feature Settings Command:\n" +
                             "  &a/feature <Feature Name> <toggle/setting> [Setting Value]\n" +
                             "  &aFor example: /feature runview toggle\n" +
                             " &2Auto Kick Command:\n" +
                             "  &a/autokick <add/remove/list> <Player Name> [Reason]\n" +
                             "  &aFor example: /autokick add zapdragon dumb ass shitter"
        );
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("fung", "fdm", "fungdungeonmod");
    }
}
