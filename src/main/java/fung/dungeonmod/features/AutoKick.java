package fung.dungeonmod.features;

import fung.dungeonmod.FungsDungeonMod;
import fung.dungeonmod.features.core.Feature;
import fung.dungeonmod.features.core.settings.BooleanSetting;
import fung.dungeonmod.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoKick extends Feature {
    public static BooleanSetting sendReason, partyFinder, partyJoin;

    public AutoKick() {
        super("AutoKick");
        this.registerSetting(sendReason = new BooleanSetting("Send reason", true));
        this.registerSetting(partyFinder = new BooleanSetting("Party finder", true));
        this.registerSetting(partyJoin = new BooleanSetting("Party join", false));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if (msg.startsWith("Dungeon Finder > ") && msg.contains(" joined the dungeon group! (") && partyFinder.isEnabled()) {
            String Playername = msg.substring(17, msg.indexOf(" joined the dungeon group!"));
            for (String AutoKickName : FungsDungeonMod.autoKick) {
                String[] w = AutoKickName.split("%01&");
                if (w[0].toLowerCase().equals(Playername.toLowerCase())) {
                    if (!sendReason.isEnabled()) {
                        Utils.sendMessage("/p kick " + Playername);
                    } else {
                        new Thread(() -> {
                            try {
                                Utils.sendMessage("/pc You have been kicked for reason: " + w[1]);
                                Thread.sleep(500);
                                Utils.sendMessage("/p kick " + Playername);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }

                }
            }
        }
        if (msg.endsWith(" joined the party.") && !msg.contains(":") && partyJoin.isEnabled()) {
            String Playername = Utils.getUnrankedMessage(msg).replace(" joined the party.", "");
            for (String AutoKickName : FungsDungeonMod.autoKick) {
                String[] w = AutoKickName.split("%01&");
                if (w[0].toLowerCase().equals(Playername.toLowerCase())) {
                    if (!sendReason.isEnabled()) {
                        Utils.sendMessage("/p kick " + Playername);
                    } else {
                        new Thread(() -> {
                            try {
                                Utils.sendMessage("/pc You have been kicked for reason: " + w[1]);
                                Thread.sleep(500);
                                Utils.sendMessage("/p kick " + Playername);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                }
            }
        }
    }

    public static boolean isAutoKickList(String playerName) {
        return false;
    }

}
