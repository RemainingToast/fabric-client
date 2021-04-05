package club.faxhax.client.api.notification;

import club.faxhax.client.api.util.FaxColor;
import club.faxhax.client.api.util.TwoDRenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class Notification {

    private NotificationType type;
    private String title;
    private String message;

    private long start;
    private long fadedIn;
    private long fadeOut;
    private long end;

    public Notification(NotificationType type, String title, String message, int length) {
        this.type = type;
        this.title = title;
        this.message = message;

        fadedIn = 200L * length;
        fadeOut = fadedIn + 500L * length;
        end = fadeOut + fadedIn;
    }

    public void show() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= end;
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public void render(MatrixStack matrices) {
        double offset = 0;
        int width = 120;
        int height = 30;
        long time = getTime();

        if (time < fadedIn) offset = Math.tanh(time / (double) (fadedIn) * 3.0) * width;
        else if (time > fadeOut) offset = (Math.tanh(3.0 - (time - fadeOut) / (double) (end - fadeOut) * 3.0) * width);
        else offset = width;

        FaxColor color = new FaxColor(0, 0, 0, 220);
        FaxColor color1;

        if (type == NotificationType.INFO) color1 = new FaxColor(0, 26, 169);
        else if (type == NotificationType.WARNING) color1 = new FaxColor(204, 193, 0);
        else {
            color1 = new FaxColor(204, 0, 18);
            int i = Math.max(0, Math.min(255, (int) (Math.sin(time / 100.0) * 255.0 / 2 + 127.5)));
            color = new FaxColor(i, 0, 0, 220);
        }

        MinecraftClient mc = MinecraftClient.getInstance();

        TextRenderer fontRenderer = mc.textRenderer;

        int mcWidth = mc.getWindow().getWidth();
        int mcHeight = mc.getWindow().getHeight();

        TwoDRenderUtil.drawRect(matrices, (int) (mcWidth - offset),mcHeight - 5 - height,mcWidth, mcHeight - 5, color.getRGB());
        TwoDRenderUtil.drawRect(matrices, (int) (mcWidth - offset), mcHeight - 5 - height, (int) (mcWidth - offset + 4), mcHeight - 5, color1.getRGB());

        fontRenderer.drawWithShadow(matrices, title, (float) (mcWidth - offset + 8), mcHeight - 2 - height, -1);
        fontRenderer.drawWithShadow(matrices, message, (float) (mcWidth - offset + 8), mcHeight - 15, -1);
    }

}
