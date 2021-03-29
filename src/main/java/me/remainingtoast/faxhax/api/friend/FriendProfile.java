package me.remainingtoast.faxhax.api.friend;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FriendProfile {

    String name;
    UUID uuid;
    ProfileType type;

    public FriendProfile(@NotNull String name, @NotNull UUID uuid, @NotNull ProfileType type) {
        this.name = name;
        this.uuid = uuid;
        this.type = type;
    }

    public FriendProfile(@NotNull GameProfile profile, @NotNull ProfileType type) {
        this.name = profile.getName();
        this.uuid = profile.getId();
        this.type = type;
    }

    public FriendProfile(@NotNull PlayerEntity playerEntity, @NotNull ProfileType type) {
        this.name = playerEntity.getGameProfile().getName();
        this.uuid = playerEntity.getGameProfile().getId();
        this.type = type;
    }

}
