package cn.lunadeer.compassTeleport;

import cn.lunadeer.compassTeleport.utils.configuration.ConfigurationManager;
import cn.lunadeer.compassTeleport.utils.scheduler.Scheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CompassTeleport extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new Scheduler(this);
        try {
            ConfigurationManager.load(Configuration.class, new File(getDataFolder(), "config.yml"));
        } catch (Exception e) {
            getLogger().severe("Failed to load configuration: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(new RenameCompassEvent(), this);
        getServer().getPluginManager().registerEvents(new UseCompassEvent(), this);
        getServer().getPluginManager().registerEvents(new BindLodestoneEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static final TextComponent tag = Component.text("[指南针] ", TextColor.fromHexString("#00c1ff"));


    public static NamespacedKey key = new NamespacedKey("compass_teleport", "compass_rename");
    public static NamespacedKey tp_location_key = new NamespacedKey("compass_teleport", "tp_location");

}
