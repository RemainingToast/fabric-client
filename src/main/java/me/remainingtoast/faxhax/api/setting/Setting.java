package me.remainingtoast.faxhax.api.setting;

import me.remainingtoast.faxhax.api.module.Module;
import me.remainingtoast.faxhax.api.setting.types.BooleanSetting;
import me.remainingtoast.faxhax.api.setting.types.EnumSetting;
import me.remainingtoast.faxhax.api.setting.types.NumberSetting;
import me.remainingtoast.faxhax.api.util.FaxColor;

import java.awt.*;
import java.util.List;

public class Setting {

    private final String name;
    private final String configName;
    private final Module parent;
    private final Module.Category faxCategory;
    private final Type type;
    private boolean visible;

    public Setting(
            final String name,
            final Module parent,
            final Module.Category faxCategory,
            final Type type) {
        this.name = name;
        this.configName = name.replace(" ", "");
        this.parent = parent;
        this.type = type;
        this.faxCategory = faxCategory;
        this.visible = true;
    }

    public String getName() {
        return this.name;
    }

    public String getConfigName() {
        return this.configName;
    }

    public Module getParent() {
        return this.parent;
    }

    public Type getType() {
        return this.type;
    }

    public Module.Category getCategory() {
        return this.faxCategory;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean newValue) {
        visible = newValue;
    }

    public enum Type {
        INTEGER,
        DOUBLE,
        BOOLEAN,
        MODE,
        COLOR
    }

    public static class Integer extends Setting implements NumberSetting {

        private int value;
        private final int min;
        private final int max;

        public Integer(
                final String name,
                final Module parent,
                final Module.Category faxCategory,
                final int value,
                final int min,
                final int max) {
            super(name, parent, faxCategory, Type.INTEGER);
            this.value = value;
            this.min = min;
            this.max = max;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(final int value) {
            this.value = value;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        @Override
        public double getNumber() {
            return this.value;
        }

        @Override
        public void setNumber(double value) {
            this.value= (int) Math.round(value);
        }

        @Override
        public double getMaximumValue() {
            return this.max;
        }

        @Override
        public double getMinimumValue() {
            return this.min;
        }

        @Override
        public int getPrecision() {
            return 0;
        }
    }

    public static class Double extends Setting implements NumberSetting {

        private double value;
        private final double min;
        private final double max;

        public Double(
                final String name,
                final Module parent,
                final Module.Category faxCategory,
                final double value,
                final double min,
                final double max) {
            super(name, parent, faxCategory, Type.DOUBLE);
            this.value = value;
            this.min = min;
            this.max = max;
        }

        public double getValue() {
            return this.value;
        }

        public void setValue(final double value) {
            this.value = value;
        }

        public double getMin() {
            return this.min;
        }

        public double getMax() {
            return this.max;
        }

        @Override
        public double getNumber() {
            return this.value;
        }

        @Override
        public void setNumber(double value) {
            this.value = value;
        }

        @Override
        public double getMaximumValue() {
            return this.max;
        }

        @Override
        public double getMinimumValue() {
            return this.min;
        }

        @Override
        public int getPrecision() {
            return 2;
        }
    }

    public static class Boolean extends Setting implements BooleanSetting {

        private boolean value;

        public Boolean(
                final String name,
                final Module parent,
                final Module.Category faxCategory,
                final boolean value) {
            super(name, parent, faxCategory, Type.BOOLEAN);
            this.value = value;
        }

        public boolean getValue(){
            return this.value;
        }

        public void setValue(final boolean value) {
            this.value = value;
        }

        @Override
        public void toggle() {
            this.value =! this.value;
        }

        @Override
        public boolean enabled() {
            return this.value;
        }
    }

    public static class Mode extends Setting implements EnumSetting {

        private String value;
        private final List<String> modes;

        public Mode(final String name, final Module parent, final Module.Category faxCategory, final List<String> modes, final String value) {
            super(name, parent, faxCategory, Type.MODE);
            this.value = value;
            this.modes = modes;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        public List<String> getModes() {
            return this.modes;
        }

        @Override
        public void increment() {
            int modeIndex = modes.indexOf(value);
            modeIndex = (modeIndex + 1) % modes.size();
            setValue(modes.get(modeIndex));
        }

        @Override
        public String getValueName() {
            return this.value;
        }
    }

    public static class ColorSetting extends Setting implements me.remainingtoast.faxhax.api.setting.types.ColorSetting {

        private boolean rainbow;
        private FaxColor value;

        public ColorSetting(final String name, final Module parent, final Module.Category faxCategory, boolean rainbow, final FaxColor value) {
            super(name, parent, faxCategory, Type.COLOR);
            this.rainbow=rainbow;
            this.value=value;
        }

        public FaxColor getValue() {
            if (rainbow) {
                return FaxColor.fromHSB((System.currentTimeMillis()%(360*32))/(360f * 32),1,1);
            }
            return this.value;
        }

        public void setValue(boolean rainbow, final FaxColor value) {
            this.rainbow = rainbow;
            this.value = value;
        }

        public int toInteger() {
            return this.value.getRGB()&0xFFFFFF+(rainbow?1:0)*0x1000000;
        }

        public void fromInteger(int number) {
            this.value = new FaxColor(number&0xFFFFFF);
            this.rainbow = ((number&0x1000000)!=0);
        }

        public FaxColor getColor() {
            return this.value;
        }

        @Override
        public boolean getRainbow() {
            return this.rainbow;
        }

        @Override
        public void setValue(Color value) {
            setValue(getRainbow(), new FaxColor(value));
        }

        @Override
        public void setRainbow(boolean rainbow) {
            this.rainbow=rainbow;
        }
    }
}
