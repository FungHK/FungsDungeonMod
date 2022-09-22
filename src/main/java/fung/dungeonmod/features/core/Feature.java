package fung.dungeonmod.features.core;

import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

public class Feature {
    public static ArrayList<Feature> features = new ArrayList<>();

    public boolean enabled = false;
    public String name;
    public ArrayList<Setting> settings = new ArrayList<>();

    public Feature(String name) {
        this.name = name;
        this.settings = new ArrayList<>();
    }

    public String getCommandName() {
        return this.name;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void toggle() {
        if (this.enabled) {
            this.disable();
        } else {
            this.enable();
        }
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void enable() {
        this.enabled = true;
        this.onEnable();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void disable() {
        this.enabled = false;
        this.onDisable();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void registerSetting(Setting setting) {
        this.settings.add(setting);
    }
}
