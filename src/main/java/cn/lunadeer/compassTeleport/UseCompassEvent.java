package cn.lunadeer.compassTeleport;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.CompassMeta;

public class UseCompassEvent implements Listener {

    @EventHandler
    public void onUseCompass(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != org.bukkit.Material.COMPASS) return;
        CompassMeta meta = (CompassMeta) event.getItem().getItemMeta();
        if (meta == null) return;
        if (!meta.hasLodestone()) return;
        Location loc = meta.getLodestone();
        if (loc == null) return;
        if (!meta.getPersistentDataContainer().has(CompassTeleport.key, org.bukkit.persistence.PersistentDataType.BOOLEAN))
            return;
        if (!meta.isLodestoneTracked()) {
            event.getPlayer().sendMessage("§b[指南针] §7此指南针未追踪磁石位置（已失效），无法传送！");
            return;
        }
        event.getPlayer().sendMessage("§b[指南针] §7正在传送到磁石位置...");
        event.getPlayer().teleportAsync(loc);
        event.setCancelled(true);
    }
}
