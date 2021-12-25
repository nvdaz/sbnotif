package xyz.nvda.sbnotifications;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

public class ConfigManager {
    private static ConfigManager instance = new ConfigManager();

    public static ConfigManager getInstance() {
        return instance;
    }

    private Configuration config;

    private Property scale;
    private Property maxNotifications;
    private Property bgColor;
    private Property fgColor;

    private ConfigManager() {
        File configFile = new File(Loader.instance().getConfigDir(), "sbnotifications.cfg");
        this.config = new Configuration(configFile);
        this.config.load();

        this.scale = this.config.get(Configuration.CATEGORY_CLIENT, "scale", 0.75F);
        this.maxNotifications = this.config.get(Configuration.CATEGORY_CLIENT, "maxNotifications", 1);
        this.bgColor = this.config.get(Configuration.CATEGORY_CLIENT, "bg", 0x2b3142);
        this.fgColor = this.config.get(Configuration.CATEGORY_CLIENT, "fg", 0xcccccc);

        this.handleUpdate();
    }

    private void handleUpdate() {
        if (this.config.hasChanged()) this.config.save();
    }

    public float getScale() {
        return (float) this.scale.getDouble();
    }

    public void setScale(float scale) {
        this.scale.set(scale);
        this.handleUpdate();
    }

    public int getMaxNotifications() {
        return this.maxNotifications.getInt();
    }

    public void setMaxNotifications(int maxNotifications) {
        this.maxNotifications.set(maxNotifications);
        this.handleUpdate();
    }

    public int getBgColor() {
        return this.bgColor.getInt();
    }

    public void setBgColor(int bgColor) {
        this.bgColor.set(bgColor);
        this.handleUpdate();
    }

    public int getFgColor() {
        return this.fgColor.getInt();
    }
}
