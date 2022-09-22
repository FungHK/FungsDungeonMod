package fung.dungeonmod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;


@Mod(modid = FungsDungeonMod.MODID, version = FungsDungeonMod.VERSION, acceptedMinecraftVersions="[1.8.9]")
public class FungsDungeonMod {

    public static final String MODID = "Fung's Dungeon Mod";
    public static final String VERSION = "1.0.0";
    public static Minecraft mc = Minecraft.getMinecraft();

    public static int tickAmount = 0;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }
}
