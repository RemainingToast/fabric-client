package club.faxhax.client.api.notification;

import net.minecraft.client.util.math.MatrixStack;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {

    private static final LinkedBlockingQueue<Notification> pendingNotifications = new LinkedBlockingQueue<>();
    private static Notification currentNotification = null;

    public static void show(Notification notification){
        pendingNotifications.add(notification);
    }


    public static void update() {
        if (currentNotification != null && !currentNotification.isShown()) {
            currentNotification = null;
        }

        if (currentNotification == null && !pendingNotifications.isEmpty()) {
            currentNotification = pendingNotifications.poll();
            currentNotification.show();
        }

    }

    public static void render(MatrixStack matrices) {
        update();

        if (currentNotification != null)
            currentNotification.render(matrices);
    }
}
