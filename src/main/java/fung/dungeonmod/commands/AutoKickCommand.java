package fung.dungeonmod.commands;

import fung.dungeonmod.FungsDungeonMod;
import fung.dungeonmod.utils.ConfigUtils;
import fung.dungeonmod.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

public class AutoKickCommand extends CommandBase implements ICommand {

    @Override
    public String getCommandName() {
        return "autokick";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return EnumChatFormatting.RED + "Usage: /autokick <add/remove/list> <Player Name> [Reason]";
    }

    @Override
    public void processCommand(ICommandSender arg0, String[] arg1) {
        if (arg1.length == 0) {
            Utils.addChatMessage(getCommandUsage(arg0));
            return;
        }
        switch (arg1[0].toLowerCase()) {
            case "add":
                if (arg1.length == 1) {
                    Utils.addChatMessage("&cUsage: " + getCommandUsage(arg0));
                    return;
                }
                for (String data : FungsDungeonMod.autoKick) {
                    String player = data.split("%01&")[0];
                    if (player.equalsIgnoreCase(arg1[1])) {
                        Utils.addChatMessage(arg1[1] + " is already exists in your auto kick list");
                        return;
                    }
                }

                String Reason = "";
                if (arg1.length == 2) {
                    Reason = "No reason given";
                } else {
                    for (int a = 2; a < arg1.length; a++) {
                        Reason = Reason + " " + arg1[a];
                    }
                    Reason = Reason.replaceFirst(" ", "");
                }
                Utils.addChatMessage("Added " + arg1[1] + " to your Auto Kick List for reason &2" + Reason);
                FungsDungeonMod.autoKick.add(arg1[1] + "%01&" + Reason);
                ConfigUtils.writeStringConfig("main", "AutoKickList", Utils.translateArrayToString(FungsDungeonMod.autoKick));
                break;
            case "remove":
                if (arg1.length == 1) {
                    Utils.addChatMessage("&cUsage: " + getCommandUsage(arg0));
                    return;
                }
                for (String data : FungsDungeonMod.autoKick) {
                    String player = data.split("%01&")[0];
                    if (player.equalsIgnoreCase(arg1[1])) {
                        Utils.addChatMessage("Removed " + arg1[1] + " from your auto kick list");
                        FungsDungeonMod.autoKick.remove(data);
                        return;
                    }
                }
                Utils.addChatMessage(arg1[1] + " isn't in your auto kick list");
                break;
            case "list":
                if (FungsDungeonMod.autoKick.isEmpty()) {
                    Utils.addChatMessage("Auto Kick List is empty!");
                    return;
                }
                int a = 0;
                Utils.addChatMessage("Auto Kick List:");
                for (String data : FungsDungeonMod.autoKick) {
                    a++;
                    String[] r = data.split("%01&");
                    Utils.addMessageWithoutPrefix(" &a" + a + ". " + r[0] + " - &2" + r[1]);
                }
                break;
            default:
                Utils.addChatMessage(getCommandUsage(arg0));
        }
    }

}
