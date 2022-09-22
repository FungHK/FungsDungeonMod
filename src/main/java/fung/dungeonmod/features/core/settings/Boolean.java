package fung.dungeonmod.features.core.settings;

import fung.dungeonmod.features.core.Setting;

public class Boolean extends Setting {
    public boolean value;
    public boolean defaultValue;

    public Boolean(String name, boolean defaultValue) {
        super(name);
        this.defaultValue = defaultValue;
    }

    public boolean isEnabled() {
        return this.value;
    }
}
