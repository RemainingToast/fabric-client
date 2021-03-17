package me.remainingtoast.faxhax.api.friend;

import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class Friend {

    public String name;

    public UUID uuid;

    public PlayerEntity playerEntity;

    public String lastSeenPosX;

    public String lastSeenPosY;

    public String lastSeenPosZ;

    public Friend(String name, UUID uuid){
        this.name = name;
        this.uuid = uuid;
    }

    public Friend(String name, UUID uuid, PlayerEntity playerEntity){
        this.name = name;
        this.uuid = uuid;
        this.playerEntity = playerEntity;
        this.lastSeenPosX = String.valueOf(playerEntity.getX());
        this.lastSeenPosY = String.valueOf(playerEntity.getY());
        this.lastSeenPosZ = String.valueOf(playerEntity.getZ());
    }
}
