package fung.dungeonmod.utils;

import fung.dungeonmod.FungsDungeonMod;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class SkyblockUtils {
    public static boolean inSkyblock;
    public static boolean inDungeon;
    public static boolean inDungeonBoss;
    public static boolean watcherDone;

    private static Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (message.equals("[BOSS] The Watcher: You have proven yourself. You may pass.")) {
            watcherDone = true;
        }
        if (watcherDone && message.startsWith("[BOSS] ") && !message.startsWith("[BOSS] The Watcher:")) {
            inDungeonBoss = true;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) return;
        if (FungsDungeonMod.tickAmount % 20 == 0) {
            checkForSkyblock();
            checkForDungeon();

        }
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load e) {
        inDungeonBoss = false;
        inDungeon = false;
        watcherDone = false;
    }

    public static void checkForSkyblock() {
        if (mc.theWorld != null && !mc.isSingleplayer()) {
            ScoreObjective scoreboardObj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreboardObj != null) {
                String scObjName = ScoreboardUtils.cleanSB(scoreboardObj.getDisplayName());
                if (scObjName.contains("SKYBLOCK")) {
                    inSkyblock = true;
                    return;
                }
            }
        }
        inSkyblock = false;
    }

    public static void checkForDungeon() {
        if (inSkyblock) {
            List<String> scoreboard = ScoreboardUtils.getSidebarLines();
            for (String s : scoreboard) {
                String sCleaned = ScoreboardUtils.cleanSB(s);
                if (sCleaned.contains("The Catacombs") || (sCleaned.contains("Cleared:") && sCleaned.contains("% ("))) {
                    inDungeon = true;
                    return;
                }
            }
        }
        inDungeon = false;
    }
}
