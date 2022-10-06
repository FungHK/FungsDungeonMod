package fung.dungeonmod.commands;

import fung.dungeonmod.features.core.Feature;
import fung.dungeonmod.features.core.Setting;
import fung.dungeonmod.features.core.settings.BooleanSetting;
import fung.dungeonmod.utils.ConfigUtils;
import fung.dungeonmod.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class FeatureCommand extends CommandBase implements ICommand {

    @Override
    public String getCommandName() {
        return "feature";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        String features = "";
        for (Feature feature : Feature.features) {
            features += "/" + feature.name.toLowerCase().replace(" ", "");
        }
        return EnumChatFormatting.RED + "Usage: /feature <" + features.replaceFirst("/", "") + "> <toggle/setting> [value]";
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            ArrayList<String> list = new ArrayList<>();
            for (Feature feature : Feature.features) {
                list.add(feature.name.toLowerCase().replaceAll(" ", ""));
            }
            return getListOfStringsMatchingLastWord(args, list);
        } else if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, "toggle", "setting");
        } else if (args.length == 3) {
            ArrayList<String> list = new ArrayList<>();
            for (Feature feature : Feature.features) {
                if (feature.name.toLowerCase().replaceAll(" ", "").equals(args[0].toLowerCase())) {
                    for (Setting setting : feature.settings) {
                        list.add(setting.name.toLowerCase().replaceAll(" ", ""));
                    }
                    break;
                }
            }
            return getListOfStringsMatchingLastWord(args, list);
        }
        return null;
    }

    @Override
    public void processCommand(ICommandSender arg0, String[] arg1) {
        if (arg1.length == 0) {
            Utils.addChatMessage(getCommandUsage(arg0));
            return;
        }
        boolean found = false;
        for (Feature feature : Feature.features) {
            if (arg1[0].toLowerCase().equals(feature.getCommandName().toLowerCase().replaceAll(" ", ""))) {
                found = true;
                if (arg1.length >= 2) {
                    if (arg1[1].toLowerCase().equals("toggle")) {
                        feature.toggle();
                        Utils.addChatMessage(feature.getCommandName() + " is now " + (feature.isEnabled() ? "&aEnabled" : "&cDisabled"));
                        ConfigUtils.writeBooleanConfig("feature-toggle", feature.name, feature.enabled);
                    } else if (arg1[1].toLowerCase().equals("setting")) {
                        if (feature.settings.isEmpty()) {
                            Utils.addChatMessage("&c" + feature.getCommandName() + " doesn't have any settings :(");
                        } else {
                            if (arg1.length == 3) {
                                for (Setting setting : feature.settings) {
                                    if (setting.name.toLowerCase().replaceAll(" ", "").equals(arg1[2].toLowerCase())) {
                                        if (setting instanceof BooleanSetting) {
                                            ((BooleanSetting) setting).value = !((BooleanSetting) setting).value;
                                            Utils.addChatMessage(setting.name + " set to " + ((BooleanSetting) setting).value);
                                            ConfigUtils.writeBooleanConfig("feature-settings", feature.name + "-" + setting.name, ((BooleanSetting) setting).value);
                                        }
                                        return;
                                    }
                                }
                            }
                            addFeatureSettingUsage(feature);
                        }
                    } else {
                        Utils.addChatMessage("&cUsage: /feature " + feature.getCommandName().toLowerCase().replaceAll(" ", "") + " <toggle/setting> [value]");
                    }
                } else {
                    Utils.addChatMessage(getCommandUsage(arg0));
                    return;
                }
            }
        }
        if (!found) {
            Utils.addChatMessage(getCommandUsage(arg0));
        }
    }

    public static void addFeatureSettingUsage(Feature feature) {
        String features = "";
        for (Setting setting : feature.settings) {
            features += "/" + setting.getSettingName().toLowerCase().replaceAll(" ", "");
        }
        Utils.addChatMessage("&cUsage: /feature " + feature.getCommandName().toLowerCase().replaceAll(" ", "") + " setting <" + features.replaceFirst("/", "") + ">");
    }

}
