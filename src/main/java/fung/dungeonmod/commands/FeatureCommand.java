package fung.dungeonmod.commands;

import fung.dungeonmod.features.core.Feature;
import fung.dungeonmod.features.core.Setting;
import fung.dungeonmod.features.core.settings.Boolean;
import fung.dungeonmod.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FeatureCommand extends CommandBase implements ICommand {

    @Override
    public String getCommandName() {
        return "feature";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return EnumChatFormatting.RED + "Usage: /feature <feature> <toggle/setting> <value>";
    }

    @Override
    public void processCommand(ICommandSender arg0, String[] arg1) {
        if (arg1.length == 0) {
            Utils.addChatMessage(getCommandUsage(arg0));
            return;
        }
        for (Feature feature : Feature.features) {
            if (arg1[0].toLowerCase().equals(feature.getCommandName().toLowerCase().replaceAll(" ", ""))) {
                if (arg1.length == 2) {
                    if (arg1[1].toLowerCase().equals("toggle")) {
                        feature.toggle();
                        Utils.addChatMessage(feature.getCommandName() + " is now " + (feature.isEnabled() ? "&aEnabled" : "&cDisabled"));
                    } else {
                        if (feature.settings.isEmpty()) {
                            Utils.addChatMessage(feature.getCommandName() + " doesn't have any settings :(");
                        } else {
                            if (arg1.length == 3) {
                                for (Setting setting : feature.settings) {
                                    if (setting.name.toLowerCase().replaceAll(" ", "").equals(arg1[2].toLowerCase())) {
                                        if (setting instanceof Boolean) {
                                            ((Boolean) setting).value = !((Boolean) setting).value;
                                        }
                                    }
                                }
                            } else {
                                String features = "";
                                for (Setting setting : feature.settings) {
                                    features += "/" + setting.getSettingName().toLowerCase().replaceAll(" ", "");
                                }
                                Utils.addChatMessage("&cUsage /feature " + feature.getCommandName().toLowerCase().replaceAll(" ", "") + " setting <" + features.replaceFirst("/", "") + ">");
                            }
                        }
                    }
                } else {
                    Utils.addChatMessage(getCommandUsage(arg0));
                    return;
                }
            }
        }
    }

}
