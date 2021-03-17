package me.remainingtoast.faxhax.api.friend;

import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendManager {

    public static List<Friend> FRIENDS = new ArrayList<>();

    public static void addFriend(PlayerEntity friend){
        FRIENDS.add(new Friend(friend.getDisplayName().asString(), friend.getUuid(), friend));
    }

    public static void addFriend(String name, UUID uuid){
        FRIENDS.add(new Friend(name, uuid));
    }

    public static void removeFriend(Friend friend){
        FRIENDS.remove(friend);
    }

}
