package cn.lunadeer.compassTeleport;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.CompassMeta;

public class UseCompassEvent implements Listener {

    private static final TextComponent tag = Component.text("[指南针] ", TextColor.fromHexString("#00c1ff"));

    @EventHandler
    public void onUseCompass(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() != org.bukkit.Material.COMPASS) return;
        CompassMeta meta = (CompassMeta) event.getItem().getItemMeta();
        if (meta == null) return;
        if (!meta.getPersistentDataContainer().has(CompassTeleport.key, org.bukkit.persistence.PersistentDataType.BOOLEAN))
            return;
        if (!meta.isLodestoneTracked() || !meta.hasLodestone()) {
            event.getPlayer().sendMessage(Component.text()
                    .append(tag)
                    .append(Component.text("此指南针无法追踪磁石位置（已失效），无法传送！").color(TextColor.fromHexString("#ff76aa")))
            );
            return;
        }
        Location loc = meta.getLodestone();
        if (loc == null) return;
        event.getPlayer().sendMessage(Component.text()
                .append(tag)
                .append(Component.text("正在传送到磁石位置...").color(TextColor.fromHexString("#67ff7b")))
        );
        event.getPlayer().teleportAsync(loc.add(0.5, 1.5, 0.5));
        event.setCancelled(true);
    }
}
