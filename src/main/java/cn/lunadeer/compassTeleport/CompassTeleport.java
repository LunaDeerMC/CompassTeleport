package cn.lunadeer.compassTeleport;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class CompassTeleport extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new RenameCompassEvent(), this);
        getServer().getPluginManager().registerEvents(new UseCompassEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static NamespacedKey key = new NamespacedKey("compass_teleport", "compass_rename");

}
