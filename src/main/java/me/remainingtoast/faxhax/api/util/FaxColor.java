package me.remainingtoast.faxhax.api.util;

import com.mojang.blaze3d.platform.GlStateManager;

import java.awt.*;

public class FaxColor extends java.awt.Color {

    public static FaxColor rainbow() {
        float hue = (System.currentTimeMillis() % (320 * 32)) / (320f * 32);
        return new FaxColor(FaxColor.fromHSB(hue, 1, 1));
    }

    private static final long serialVersionUID = 1L;

    public FaxColor(int rgb) {
        super(rgb);
    }

    public FaxColor(int rgba, boolean hasalpha) {
        super(rgba,hasalpha);
    }

    public FaxColor(int r, int g, int b) {
        super(r,g,b);
    }

    public FaxColor(int r, int g, int b, int a) {
        super(r,g,b,a);
    }

    public FaxColor(java.awt.Color color) {
        super(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
    }

    public FaxColor(FaxColor color, int a) {
        super(color.getRed(),color.getGreen(),color.getBlue(),a);
    }

    public static FaxColor fromHSB (float hue, float saturation, float brightness) {
        return new FaxColor(java.awt.Color.getHSBColor(hue,saturation,brightness));
    }

    public float getHue() {
        return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[0];
    }

    public float getSaturation() {
        return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[1];
    }

    public float getBrightness() {
        return RGBtoHSB(getRed(),getGreen(),getBlue(),null)[2];
    }

    public void glColor() {
        GlStateManager.color4f(getRed()/255.0f,getGreen()/255.0f,getBlue()/255.0f,getAlpha()/255.0f);
    }
}
