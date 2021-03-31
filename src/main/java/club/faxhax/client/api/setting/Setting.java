package club.faxhax.client.api.setting;

import club.faxhax.client.api.module.Module;
import club.faxhax.client.api.setting.types.BooleanSetting;
import club.faxhax.client.api.setting.types.EnumSetting;
import club.faxhax.client.api.setting.types.NumberSetting;
import club.faxhax.client.api.util.FaxColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Setting<T> {

    private final String name;
    private final String configName;
    private final Module parent;
    private final Module.Category faxCategory;
    private final Type type;
    private boolean hidden;
    private boolean grouped;
    private Consumer<T> changedListener;
    private T value;

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
        this.hidden = false;
        this.grouped = false;
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

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setChangedListener(Consumer<T> listener) {
        this.changedListener = listener;
    }

    public void changed() {
        if (changedListener != null) changedListener.accept(value);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

    public boolean isGrouped() {
        return grouped;
    }

    public enum Type {
        DOUBLE,
        BOOLEAN,
        MODE,
        GROUP,
        COLOR
    }

    public static class Double extends Setting<Double> implements NumberSetting {

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

        public int getIntValue() { return (int) this.value; }

        public float getFloatValue() { return (float) this.value; }

        public void setValue(final double value) {
            this.value = value;
            setValue(this);
            changed();
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
            setValue(value);
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

        public Setting.Double onChanged(Consumer<Double> listener){
            setChangedListener(listener);
            return this;
        }
    }

    public static class Boolean extends Setting<Boolean> implements BooleanSetting {

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
            setValue(this);
            changed();
        }

        @Override
        public void toggle() {
            setValue(!this.value);
        }

        @Override
        public boolean enabled() {
            return this.value;
        }

        public Setting.Boolean onChanged(Consumer<Boolean> listener){
            setChangedListener(listener);
            return this;
        }
    }

    public static class Mode extends Setting<Mode> implements EnumSetting {

        private String value;
        private final List<String> modes;

        public Mode(final String name, final Module parent, final Module.Category faxCategory, final String value, final String... modes) {
            super(name, parent, faxCategory, Type.MODE);
            this.value = value;
            this.modes = Arrays.asList(modes);
        }

        public Mode(final String name, final Module parent, final Module.Category faxCategory, final String value, final List<String> modes) {
            super(name, parent, faxCategory, Type.MODE);
            this.value = value;
            this.modes = modes;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(final String value) {
             this.value = value;
             setValue(this);
             changed();
        }

        public List<String> getModes() {
            return modes;
        }

        public boolean toggled(String mode){
            return getValue().equalsIgnoreCase(mode);
        }

        @Override
        public void increment() {
            int modeIndex = modes.indexOf(value);
            modeIndex = (modeIndex + 1) % modes.size();
            setValue(modes.get(modeIndex));
        }

        @Override
        public String getValueName() {
            return this.value.toString();
        }

        public Setting.Mode onChanged(Consumer<Mode> listener){
            setChangedListener(listener);
            return this;
        }
    }

    public static class ColorSetting extends Setting<ColorSetting> implements club.faxhax.client.api.setting.types.ColorSetting {

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
            changed();
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

        public Setting.ColorSetting onChanged(Consumer<ColorSetting> listener){
            setChangedListener(listener);
            return this;
        }
    }

    public static class Group extends Setting<Group> {

        List<Setting<?>> settings;
        boolean expanded;

        public Group(final String name, final Module parent, final Module.Category faxCategory, final Setting<?>... settings) {
            super(name, parent, faxCategory, Type.GROUP);
            this.settings = new ArrayList<>();
            for(Setting<?> setting : settings){
                setting.setGrouped(true);
                this.settings.add(setting);
            }
        }

        public List<Setting<?>> getSettings() {
            return settings;
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }

        public boolean isExpanded() {
            return expanded;
        }
    }
}
