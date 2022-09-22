package fung.dungeonmod.commands;

import fung.dungeonmod.features.core.Feature;
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
    public String getCommandUsage(ICommandSender sender) {
        return EnumChatFormatting.RED + "Usage: /fungdungeonmod <feature> <settings>";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return Arrays.asList("toggle");
    }

    @Override
    public void processCommand(ICommandSender arg0, String[] arg1) {
        if (arg1.length == 0) {
            Utils.addChatMessage(getCommandUsage(arg0));
            return;
        }
        switch (arg1[0].toLowerCase()) {
            case "setting":
                if (arg1.length != 2) {
                    sendToggleUsage();
                    return;
                } else {
                    for (Feature feature : Feature.features) {
                        if (arg1[1].toLowerCase().equals(feature.getCommandName().toLowerCase().replaceAll(" ", ""))) {

                        }
                    }
                }
                break;
            default:
                Utils.addChatMessage(getCommandUsage(arg0));
        }
    }

    public void sendToggleUsage() {
        Utils.addChatMessage("&cUsage: /fungdungeonmod toggle <runreview>");
    }
}
