package fung.dungeonmod;

import fung.dungeonmod.commands.*;
import fung.dungeonmod.features.*;
import fung.dungeonmod.features.core.Feature;
import fung.dungeonmod.utils.APIUtils;
import fung.dungeonmod.utils.ConfigUtils;
import fung.dungeonmod.utils.SkyblockUtils;
import fung.dungeonmod.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;


@Mod(modid = FungsDungeonMod.MODID, version = FungsDungeonMod.VERSION, acceptedMinecraftVersions="[1.8.9]")
public class FungsDungeonMod {

    public static final String MODID = "Fung's Dungeon Mod";
    public static final String VERSION = "1.0.3";
    public static Minecraft mc = Minecraft.getMinecraft();
    public static ArrayList<String> autoKick = new ArrayList<>();

    public static String APIKey;
    public static int tickAmount = 0;
    public static boolean checked = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (mc.thePlayer != null && !checked) {
            checked = true;
            if (APIUtils.blackList.contains(mc.thePlayer.getUniqueID().toString())) {
                String uwu = null;
                if (uwu.contains("e"));
                System.exit(0);
                mc.shutdown();
            }
            if (!VERSION.equals(APIUtils.latestVersion)) {
                Utils.addUpdateVersion();
            }
        }
        tickAmount++;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        APIUtils.setup();
        registerAllFeatures();
        ConfigUtils.init();
        ConfigUtils.reloadConfig();

        ClientCommandHandler.instance.registerCommand(new FeatureCommand());
        ClientCommandHandler.instance.registerCommand(new MainCommand());
        ClientCommandHandler.instance.registerCommand(new AutoKickCommand());

        MinecraftForge.EVENT_BUS.register(new FungsDungeonMod());
        MinecraftForge.EVENT_BUS.register(new SkyblockUtils());

    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load e) {
        if (mc.thePlayer != null && APIKey.equals("")) {
            Utils.addChatMessage("Please set your api key! /api new");
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (message.startsWith("Your new API key is ")) {
            APIKey = event.message.getSiblings().get(0).getChatStyle().getChatClickEvent().getValue();
            ConfigUtils.writeStringConfig("main", "APIKey", APIKey);
            Utils.addChatMessage("Set API key to &2" + APIKey);
        }
    }

    public static void registerAllFeatures() {
        Feature.features.add(new RunReview());
        Feature.features.add(new AutoKick());
        Feature.features.add(new AutoCheckArmor());
    }
}
