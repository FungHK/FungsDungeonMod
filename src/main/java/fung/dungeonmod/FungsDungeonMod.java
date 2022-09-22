package fung.dungeonmod;

import fung.dungeonmod.commands.FeatureCommand;
import fung.dungeonmod.commands.MainCommand;
import fung.dungeonmod.features.RunReview;
import fung.dungeonmod.features.core.Feature;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;


@Mod(modid = FungsDungeonMod.MODID, version = FungsDungeonMod.VERSION, acceptedMinecraftVersions="[1.8.9]")
public class FungsDungeonMod {

    public static final String MODID = "Fung's Dungeon Mod";
    public static final String VERSION = "1.0.0";
    public static Minecraft mc = Minecraft.getMinecraft();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new FeatureCommand());

        MinecraftForge.EVENT_BUS.register(new RunReview());

        Feature.features.add(new RunReview());
    }
}
