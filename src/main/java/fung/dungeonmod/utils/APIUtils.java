package fung.dungeonmod.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fung.dungeonmod.FungsDungeonMod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class APIUtils {
	public static int getSecretCount(String username) {
		JsonObject secretResponse = getResponse("https://api.hypixel.net/player?key=" + FungsDungeonMod.APIKey + "&uuid=" + getUUID(username), true);
		if (secretResponse == null) return 0;
		return secretResponse.get("player")
				.getAsJsonObject()
				.get("achievements")
				.getAsJsonObject()
				.get("skyblock_treasure_hunter"
				).getAsInt();
	}

	public static String getBlackList() {
		JsonObject uuidResponse = getResponse("https://raw.githubusercontent.com/FungHK/FungsDungeonMod/main/ModStuff", false);
		if (uuidResponse == null) return null;
		return uuidResponse.get("black_list").getAsString();
	}

	public static String getUUID(String username) {
		JsonObject uuidResponse = getResponse("https://api.mojang.com/users/profiles/minecraft/" + username, false);
		if (uuidResponse == null) return null;
		return uuidResponse.get("id").getAsString();
	}

	public static JsonObject getResponse(String urlString, boolean hasError) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "Dsm/1.0");

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
				String input;
				StringBuilder response = new StringBuilder();

				while ((input = in.readLine()) != null) {
					response.append(input);
				}
				in.close();

				Gson gson = new Gson();

				return gson.fromJson(response.toString(), JsonObject.class);
			} else {
				if (hasError) {
					InputStream errorStream = conn.getErrorStream();
					try (Scanner scanner = new Scanner(errorStream)) {
						scanner.useDelimiter("\\Z");
						String error = scanner.next();

						Gson gson = new Gson();
						return gson.fromJson(error, JsonObject.class);
					}
				} else if (urlString.startsWith("https://api.mojang.com/users/profiles/minecraft/") && conn.getResponseCode() == 204) {
					Utils.addChatMessage("&cFailed with reason: Player does not exist.");
				} else {
					Utils.addChatMessage("&cRequest failed. HTTP Error Code: " + conn.getResponseCode());
				}
			}
		} catch (IOException ex) {
			Utils.addChatMessage("&cAn error has occured. See logs for more details.");
			ex.printStackTrace();
		}

		return new JsonObject();
	}
}
