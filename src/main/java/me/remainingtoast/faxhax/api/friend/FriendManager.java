package me.remainingtoast.faxhax.api.friend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.GameProfile;
import me.remainingtoast.faxhax.FaxHax;
import me.remainingtoast.faxhax.api.config.ConfigManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

public class FriendManager {

    private static final HashMap<UUID, FriendProfile> FRIENDS = new HashMap<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void load(){
        if (!ConfigManager.GAME_DIR.exists()) {
            ConfigManager.GAME_DIR.mkdirs();
        }
        try {
            if (ConfigManager.FRIEND_CONFIG.exists()) {
                Reader reader = Files.newBufferedReader(ConfigManager.FRIEND_CONFIG.toPath());
                Type type = new TypeToken<HashMap<UUID, FriendProfile>>() {}.getType();
                FRIENDS.putAll(GSON.fromJson(reader, type));
                reader.close();
            }
        } catch (Exception e) {
            FaxHax.LOGGER.fatal("Failed to load friends!");
        }

    }

    public static void save() {
        try {
            OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(ConfigManager.FRIEND_CONFIG), StandardCharsets.UTF_8);
            output.write(GSON.toJson(FRIENDS));
            output.close();
        } catch (IOException e) {
            FaxHax.LOGGER.fatal("Failed to save friends!");
        }
    }

    public static FriendProfile addFriend(FriendProfile friend){
        FRIENDS.put(friend.uuid, friend);
        return FRIENDS.get(friend.uuid);
    }

    public static void removeFriend(FriendProfile friend){
        FRIENDS.remove(friend.uuid);
    }

    public static FriendProfile getFriend(UUID uuid){
        return FRIENDS.get(uuid);
    }

    public static boolean isFriend(FriendProfile friend){
        return friend.type == ProfileType.FRIEND;
    }

    public static boolean isEnemy(FriendProfile friend){
        return friend.type == ProfileType.ENEMY;
    }

    public static boolean isFriend(UUID uuid){
        FriendProfile friend = FRIENDS.get(uuid);
        return friend != null && friend.type == ProfileType.FRIEND;
    }

    public static boolean isEnemy(UUID uuid){
        FriendProfile friend = FRIENDS.get(uuid);
        return friend != null && friend.type == ProfileType.ENEMY;
    }

    public static boolean isFriend(GameProfile profile){
        FriendProfile friend = FRIENDS.get(profile.getId());
        return friend != null && friend.type == ProfileType.FRIEND;
    }

    public static boolean isEnemy(GameProfile profile){
        FriendProfile friend = FRIENDS.get(profile.getId());
        return friend != null && friend.type == ProfileType.ENEMY;
    }
}
