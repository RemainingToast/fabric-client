package club.faxhax.client.api.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class CapeUtil {

    public static HashMap<String, Identifier> capes = new HashMap<>();

    public static Identifier fromPlayer(PlayerEntity player) {
        return capes.get(player.getUuidAsString());
    }

    public static void onPlayerJoin(PlayerEntity player) {

        if (((AbstractClientPlayerEntity) player).getCapeTexture() == null) {

            Thread thread = new Thread(() -> setCapeFromURL(player.getUuidAsString(), String.format("http://s.optifine.net/capes/%s.png", player.getEntityName())));
            thread.start();

        }
    }

    public static boolean setCapeFromURL(String uuid, String capeStringURL) {
        try {
            URL capeURL = new URL(capeStringURL);
            setCape(uuid, capeURL.openStream());
            return true;
        } catch (IOException e) {
            capes.put(uuid, null);
            return false;
        }
    }

    public static void setCape(String uuid, InputStream image) {
        NativeImage cape = null;
        try {
            cape = NativeImage.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(cape == null) return;
        NativeImageBackedTexture nIBT = new NativeImageBackedTexture(parseCape(cape));
        Identifier capeTexture = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(uuid.replace("-", ""), nIBT);
        capes.put(uuid, capeTexture);
    }

    public static NativeImage parseCape(NativeImage image) {
        int imageWidth = 64;
        int imageHeight = 32;
        int imageSrcWidth = image.getWidth();
        int srcHeight = image.getHeight();

        for (int imageSrcHeight = image.getHeight(); imageWidth < imageSrcWidth
                || imageHeight < imageSrcHeight; imageHeight *= 2) {
            imageWidth *= 2;
        }

        NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        for (int x = 0; x < imageSrcWidth; x++) {
            for (int y = 0; y < srcHeight; y++) {
                imgNew.setPixelColor(x, y, image.getPixelColor(x, y));
            }
        }
        image.close();
        return imgNew;
    }

}
