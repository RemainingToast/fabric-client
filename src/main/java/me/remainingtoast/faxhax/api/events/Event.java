package me.remainingtoast.faxhax.api.events;

import me.zero.alpine.event.type.Cancellable;
import net.minecraft.client.MinecraftClient;

public class Event extends Cancellable {

    private final Era era = Era.PRE;
    private final Float ticks = MinecraftClient.getInstance().getTickDelta();

    public enum Era {
        PRE, POST
    }

    public Era getEra() {
        return era;
    }

    public Float getTicks() {
        return ticks;
    }
}
