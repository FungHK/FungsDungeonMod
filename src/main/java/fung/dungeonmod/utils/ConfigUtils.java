package fung.dungeonmod.utils;

import fung.dungeonmod.FungsDungeonMod;
import fung.dungeonmod.features.core.Feature;
import fung.dungeonmod.features.core.Setting;
import fung.dungeonmod.features.core.settings.BooleanSetting;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigUtils {
    public static Configuration config;
    public final static String file = "config/FungsDungeonMod.cfg";

    public static void reloadConfig() {
        if (!hasKey("main", "APIKey")) writeStringConfig("main", "APIKey", "");
        FungsDungeonMod.APIKey = getString("main", "APIKey");

        //ignore list
        if (!hasKey("main", "AutoKickList")) writeStringConfig("main", "AutoKickList", "");
        FungsDungeonMod.autoKick = Utils.translateStringToArrayList(getString("main", "AutoKickList"));

        //Feature config
        for (Feature feature : Feature.features) {
            if (!hasKey("feature-toggle", feature.name)) writeBooleanConfig("feature-toggle", feature.name, false);
            feature.enabled = getBoolean("feature-toggle", feature.name);
            if (!feature.settings.isEmpty()) {
                for (Setting setting : feature.settings) {
                    if (setting instanceof BooleanSetting) {
                        if (!hasKey("feature-settings", feature.name + "-" + setting.name)) writeBooleanConfig("feature-settings", feature.name + "-" + setting.name, false);
                        feature.enabled = getBoolean("feature-settings", feature.name + "-" + setting.name);
                    }
                }
            }
        }

    }

    public static void init() {
        config = new Configuration(new File(file));
        try {
            config.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
    }


    public static int getInt(String category, String key) {
        category = category.toLowerCase();
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, 0).getInt();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
        return 0;
    }

    public static double getDouble(String category, String key) {
        category = category.toLowerCase();
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, 0D).getDouble();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
        return 0D;
    }

    public static String getString(String category, String key) {
        category = category.toLowerCase();
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, "").getString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
        return "";
    }

    public static boolean getBoolean(String category, String key) {
        category = category.toLowerCase();
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.getCategory(category).containsKey(key)) {
                return config.get(category, key, false).getBoolean();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
        return true;
    }

    public static void writeIntConfig(String category, String key, int value) {
        category = category.toLowerCase();
        config = new Configuration(new File(file));
        try {
            config.load();
            int set = config.get(category, key, value).getInt();
            config.getCategory(category).get(key).set(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
    }

    public static void writeDoubleConfig(String category, String key, double value) {
        category = category.toLowerCase();
        config = new Configuration(new File(file));
        try {
            config.load();
            double set = config.get(category, key, value).getDouble();
            config.getCategory(category).get(key).set(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
    }

    public static void writeStringConfig(String category, String key, String value) {
        category = category.toLowerCase();
        config = new Configuration(new File(file));
        try {
            config.load();
            String set = config.get(category, key, value).getString();
            config.getCategory(category).get(key).set(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
    }

    public static void writeBooleanConfig(String category, String key, boolean value) {
        category = category.toLowerCase();
        config = new Configuration(new File(file));
        try {
            config.load();
            boolean set = config.get(category, key, value).getBoolean();
            config.getCategory(category).get(key).set(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
    }

    public static boolean hasKey(String category, String key) {
        category = category.toLowerCase();
        config = new Configuration(new File(file));
        try {
            config.load();
            if (!config.hasCategory(category)) return false;
            return config.getCategory(category).containsKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
        return false;
    }

    public static void deleteCategory(String category) {
        category = category.toLowerCase();
        config = new Configuration(new File(file));
        try {
            config.load();
            if (config.hasCategory(category)) {
                config.removeCategory(new ConfigCategory(category));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            config.save();
        }
    }

}
