package fung.dungeonmod.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
	public static String blackList, latestVersion, discordLink;

	public static String getLatestProfileID(String UUID, String key) {

		// Get profiles
		System.out.println("Fetching profiles...");

		JsonObject profilesResponse = getResponse("https://api.hypixel.net/skyblock/profiles?uuid=" + UUID + "&key=" + key, true);
		if (!profilesResponse.get("success").getAsBoolean()) {
			String reason = profilesResponse.get("cause").getAsString();
			Utils.addChatMessage("Failed with reason: " + reason);
			return null;
		}
		if (profilesResponse.get("profiles").isJsonNull()) {
			Utils.addChatMessage("This player doesn't appear to have played SkyBlock.");
			return null;
		}

		// Loop through profiles to find latest
		System.out.println("Looping through profiles...");
		String latestProfile = "";
		long latestSave = 0;
		JsonArray profilesArray = profilesResponse.get("profiles").getAsJsonArray();

		for (JsonElement profile : profilesArray) {
			JsonObject profileJSON = profile.getAsJsonObject();
			long profileLastSave = 1;
			if (profileJSON.get("members").getAsJsonObject().get(UUID).getAsJsonObject().has("last_save")) {
				profileLastSave = profileJSON.get("members").getAsJsonObject().get(UUID).getAsJsonObject().get("last_save").getAsLong();
			}

			if (profileLastSave > latestSave) {
				latestProfile = profileJSON.get("profile_id").getAsString();
				latestSave = profileLastSave;
			}
		}

		return latestProfile;
	}

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

	public static void setup() {
		JsonObject uuidResponse = getResponse("https://raw.githubusercontent.com/FungHK/FungsDungeonMod/main/ModStuff", false);
		if (uuidResponse == null) return;
		blackList = uuidResponse.get("black_list").getAsString();
		latestVersion = uuidResponse.get("version").getAsString();
		discordLink = uuidResponse.get("discord").getAsString();
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
