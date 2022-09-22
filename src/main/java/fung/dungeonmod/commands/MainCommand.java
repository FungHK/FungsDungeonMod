package fung.dungeonmod.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

public class MainCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "fungdungeonmod";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return EnumChatFormatting.RED + "Usage: /fungdungeonmod";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return Arrays.asList("reloadconfig", "discord");
    }

    @Override
    public void processCommand(ICommandSender arg0, String[] arg1) {

    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("fungdungeonmod", "fung", "dungeonmod", "fdm");
    }


    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
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
