package fung.dungeonmod.features.core.settings;

import fung.dungeonmod.features.core.Setting;

public class BooleanSetting extends Setting {
    public boolean value;
    public boolean defaultValue;

    public BooleanSetting(String name, boolean defaultValue) {
        super(name);
        this.defaultValue = defaultValue;
    }

    public boolean isEnabled() {
        return this.value;
    }
}
