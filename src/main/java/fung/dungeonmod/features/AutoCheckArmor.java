package fung.dungeonmod.features;

import com.google.gson.JsonObject;
import fung.dungeonmod.FungsDungeonMod;
import fung.dungeonmod.features.core.Feature;
import fung.dungeonmod.utils.APIUtils;
import fung.dungeonmod.utils.Utils;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class AutoCheckArmor extends Feature {
    public AutoCheckArmor() {
        super("Auto Check Armor");
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (message.startsWith("Dungeon Finder > ") && message.contains(" joined the dungeon group! (")) {
            String player = message.substring(17, message.indexOf(" joined"));
            checkArmor(player);
        }
    }


    //Danker's Skyblock Mod
    public static void checkArmor(String username) {
        // MULTI THREAD DRIFTING
        new Thread(() -> {

            // Check key
            String key = FungsDungeonMod.APIKey;
            if (key.equals("")) {
                Utils.addChatMessage("API key not set. Use /api new");
                return;
            }

            // Get UUID for Hypixel API requests
            String uuid;
            Utils.addChatMessage("Checking armour of " + username);
            uuid = APIUtils.getUUID(username);

            // Find stats of latest profile
            String latestProfile = APIUtils.getLatestProfileID(uuid, key);
            if (latestProfile == null) return;

            String profileURL = "https://api.hypixel.net/skyblock/profile?profile=" + latestProfile + "&key=" + key;
            System.out.println("Fetching profile...");
            JsonObject profileResponse = APIUtils.getResponse(profileURL, true);
            if (!profileResponse.get("success").getAsBoolean()) {
                String reason = profileResponse.get("cause").getAsString();
                Utils.addChatMessage("Failed with reason: " + reason);
                return;
            }

            String armourBase64 = profileResponse.get("profile").getAsJsonObject().get("members").getAsJsonObject().get(uuid).getAsJsonObject().get("inv_armor").getAsJsonObject().get("data").getAsString();
            InputStream armourStream = new ByteArrayInputStream(Base64.getDecoder().decode(armourBase64));
            // String armourDecodedGZIP = new String(Base64.getDecoder().decode(armourBase64));

            try {
                NBTTagCompound armour = CompressedStreamTools.readCompressed(armourStream);
                NBTTagList armourList = armour.getTagList("i", 10);

                String helmet = EnumChatFormatting.RED + "None";
                String chest = EnumChatFormatting.RED + "None";
                String legs = EnumChatFormatting.RED + "None";
                String boots = EnumChatFormatting.RED + "None";
                // Loop through armour
                for (int i = 0; i < armourList.tagCount(); i++) {
                    NBTTagCompound armourPiece = armourList.getCompoundTagAt(i);
                    if (armourPiece.hasNoTags()) continue;

                    String armourPieceName = armourPiece.getCompoundTag("tag").getCompoundTag("display").getString("Name");
                    // NBT is served boots -> helmet
                    switch (i) {
                        case 0:
                            boots = armourPieceName;
                            break;
                        case 1:
                            legs = armourPieceName;
                            break;
                        case 2:
                            chest = armourPieceName;
                            break;
                        case 3:
                            helmet = armourPieceName;
                            break;
                        default:
                            System.err.println("An error has occurred.");
                            break;
                    }
                }
                armourStream.close();

                Utils.addChatMessage(username + "'s Armour:\n " +
                        helmet + "\n " +
                        chest + "\n " +
                        legs + "\n " +
                        boots);
            } catch (IOException ex) {
                Utils.addChatMessage("An error has occurred while reading inventory data. See logs for more info.");
                ex.printStackTrace();
            }
        }).start();
    }
}
