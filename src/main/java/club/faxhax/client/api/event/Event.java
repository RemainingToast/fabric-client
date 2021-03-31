package club.faxhax.client.api.event;

import meteordevelopment.orbit.ICancellable;

public class Event implements ICancellable {

    private boolean cancelled;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

}
