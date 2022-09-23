package fung.dungeonmod.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class Utils {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void sendMessage(String message) {
        mc.thePlayer.sendChatMessage(message);
    }

    public static String translateAlternateColorCodes(String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrZz".indexOf(b[i + 1]) > -1) {
                b[i] = '§';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    public static void addChatMessage(String Message) {
        mc.thePlayer.addChatMessage(new ChatComponentText("§a[Fung] " + translateAlternateColorCodes(Message)));
    }

    public static void addUpdateVersion() {
        final ChatComponentText chatComponentText = new ChatComponentText("§a[Fung] You are using older version, I recommend to update it (click to join server)");
        final ChatStyle chatStyle = new ChatStyle();
        chatStyle.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, APIUtils.discordLink));
        chatComponentText.setChatStyle(chatStyle);
        Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponentText);
    }
}
