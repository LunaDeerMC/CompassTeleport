package cn.lunadeer.compassTeleport.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Misc {

    public static boolean isPaper() {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.ScheduledTask");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static String formatString(String str, Object... args) {
        String formatStr = str;
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                args[i] = "[null for formatString (args[" + i + "])]";
            }
            formatStr = formatStr.replace("{" + i + "}", args[i].toString());
        }
        return formatStr;
    }

    public static String getLocationString(Location location) {
        return location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    public static Location getLocationFromString(String str, World world) {
        String[] parts = str.split(",");
        if (parts.length != 5) {
            return null;
        }
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);
        float yaw = Float.parseFloat(parts[3]);
        float pitch = Float.parseFloat(parts[4]);
        return new Location(world, x, y, z, yaw, pitch);
    }
}
