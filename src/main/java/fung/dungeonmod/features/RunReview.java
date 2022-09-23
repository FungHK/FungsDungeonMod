package fung.dungeonmod.features;

import fung.dungeonmod.FungsDungeonMod;
import fung.dungeonmod.features.core.Feature;
import fung.dungeonmod.features.core.settings.BooleanSetting;
import fung.dungeonmod.utils.APIUtils;
import fung.dungeonmod.utils.SkyblockUtils;
import fung.dungeonmod.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunReview extends Feature {
    public static BooleanSetting debug, sendParty;

    private static boolean sent, dungeonEnding = false;
    private static int delimiter = -1;
    private static ArrayList<String> party = new ArrayList<>();
    private static ArrayList<String> partyData = new ArrayList<>();

    public RunReview() {
        super("Run Review");
        this.registerSetting(debug = new BooleanSetting("Debug", true));
        this.registerSetting(sendParty = new BooleanSetting("Send party", true));
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (FungsDungeonMod.tickAmount % 20 == 0 && !dungeonEnding && SkyblockUtils.inDungeonBoss) {
            dungeonEnding = true;
            if (debug.isEnabled()) Utils.addChatMessage("Boss room");
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    if (debug.isEnabled())Utils.addChatMessage("Start counting");
                    for (int i = 0; i < partyData.size(); i++) {
                        String player = partyData.get(i).split(",")[0];
                        if (debug.isEnabled()) Utils.addChatMessage("checking " + player);
                        partyData.set(i, player + "," + (APIUtils.getSecretCount(player) - Integer.parseInt(partyData.get(i).split(",")[1])));
                    }
                } catch (Exception ignored) {}
            }).start();
        }
    }

    private static void reset() {
        party.clear();
        partyData.clear();
        delimiter = -1;
        dungeonEnding = false;
        sent = false;
    }

    public void onEnable() {
        reset();
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load e) {
        reset();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();

        //dungeon start
        if (message.equals("Dungeon starts in 1 second.")) {
            delimiter = 0;
            Utils.sendMessage("/pl");
        }

        //dungeon end
        if (!sent && event.message.getFormattedText().contains("§r§c☠ §r§eDefeated §r")) {
            sent = true;
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    String message2 = "Secrets found:";
                    String msg = "/pc Secrets found: ";
                    for (String data : partyData) {
                        String name = data.split(",")[0];
                        String secret = data.split(",")[1];
                        message2 += "\n &d" + name + ": &5" + secret;
                        msg += ", " + name + " (" + secret + ")";
                    }
                    if (debug.isEnabled()) Utils.addChatMessage(message2);
                    if (sendParty.isEnabled()) {
                        Thread.sleep(4000);
                        Utils.sendMessage(msg.replaceFirst(", ", ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }
        /*if (!sent && message.startsWith("You are not currently in a party.")) {
            reset();
        }*/
        //Detect party member
        if (delimiter != -1) {
            if (message.contains("--------------")) {
                delimiter++;
            }
            if (message.startsWith("Party M") || message.startsWith("Party Leader")) {
                Pattern party_start_pattern = Pattern.compile("^Party Members \\((\\d+)\\)$");
                Pattern leader_pattern = Pattern.compile("^Party Leader: (?:\\[.+?] )?(\\w+) ●$");
                Pattern members_pattern = Pattern.compile(" (?:\\[.+?] )?(\\w+) ●");
                Matcher members = members_pattern.matcher(message);
                while (members.find()) {
                    party.add(members.group(1));
                    Utils.addChatMessage(members.group(1));
                }
            }
            if (delimiter == 2) {
                delimiter = -1;
                addData();
            }
            event.setCanceled(!message.equals("Dungeon starts in 1 second."));
        }
    }

    public static void addData() {
        new Thread(() -> {
            for (String player : party) {
                int secrets = APIUtils.getSecretCount(player);
                if (debug.isEnabled()) Utils.addChatMessage("adding " + player + " to current database, " + secrets);
                partyData.add(player + "," + secrets);
            }
        }).start();
    }


    public static float calcAverage(int totalSecret, int totalRuns) {
        return Math.round(((((float) totalSecret) / ((float) totalRuns)) * 10) / 10);
    }

    public static float calcAverage(String totalSecret, String totalRuns) {
        return calcAverage(Integer.parseInt(totalSecret), Integer.parseInt(totalRuns));
    }
}
