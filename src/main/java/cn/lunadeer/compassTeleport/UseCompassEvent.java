package cn.lunadeer.compassTeleport;

import cn.lunadeer.compassTeleport.utils.scheduler.CancellableTask;
import cn.lunadeer.compassTeleport.utils.scheduler.Scheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static cn.lunadeer.compassTeleport.CompassTeleport.tag;
import static cn.lunadeer.compassTeleport.utils.Misc.getLocationFromString;
import static cn.lunadeer.compassTeleport.utils.Misc.getLocationString;

public class UseCompassEvent implements Listener {

    private static final Map<UUID, Long> lastTeleportTime = new java.util.HashMap<>();
    private static final Map<UUID, CancellableTask> tpTasks = new java.util.HashMap<>();

    @EventHandler
    public void onUseCompass(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() != org.bukkit.Material.COMPASS) return;
        CompassMeta meta = (CompassMeta) event.getItem().getItemMeta();
        if (meta == null) return;
        if (!meta.getPersistentDataContainer().has(CompassTeleport.key, org.bukkit.persistence.PersistentDataType.BOOLEAN))
            return;
        if (!event.getPlayer().hasPermission("compassteleport.use")) {
            event.getPlayer().sendMessage(Component.text()
                    .append(tag)
                    .append(Component.text("你没有使用传送指南针的权限！").color(TextColor.fromHexString("#ff76aa")))
            );
            return;
        }
        if (!meta.isLodestoneTracked() || !meta.hasLodestone() || meta.getLodestone() == null) {
            event.getPlayer().sendMessage(Component.text()
                    .append(tag)
                    .append(Component.text("此指南针无法追踪磁石位置（已失效），无法传送！").color(TextColor.fromHexString("#ff76aa")))
            );
            return;
        }
        Location loc = getLocationFromString(Objects.requireNonNullElse(meta.getPersistentDataContainer().get(CompassTeleport.tp_location_key, PersistentDataType.STRING),
                getLocationString(meta.getLodestone())), meta.getLodestone().getWorld());
        if (loc == null) {
            event.getPlayer().sendMessage(Component.text()
                    .append(tag)
                    .append(Component.text("此指南针无法追踪磁石位置（已失效），无法传送！").color(TextColor.fromHexString("#ff76aa")))
            );
            return;
        }

        // cooldown
        if (lastTeleportTime.containsKey(event.getPlayer().getUniqueId()) &&
                System.currentTimeMillis() - lastTeleportTime.get(event.getPlayer().getUniqueId()) < Configuration.cooldownSec * 1000L) {
            event.getPlayer().sendMessage(Component.text()
                    .append(tag)
                    .append(Component.text("请稍等 " + (Configuration.cooldownSec - (System.currentTimeMillis() - lastTeleportTime.get(event.getPlayer().getUniqueId())) / 1000) + " 秒后再使用！").color(TextColor.fromHexString("#ff76aa")))
            );
            return;
        }
        // cancel previous task
        if (tpTasks.containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(Component.text()
                    .append(tag)
                    .append(Component.text("你有一个未完成的传送，本次传送已取消！").color(TextColor.fromHexString("#ff76aa")))
            );
            return;
        }
        event.getPlayer().sendMessage(Component.text()
                .append(tag)
                .append(Component.text("正在传送到磁石位置...").color(TextColor.fromHexString("#67ff7b")))
        );
        // do the teleport
        if (Configuration.delaySec > 0) {
            event.getPlayer().sendMessage(Component.text()
                    .append(tag)
                    .append(Component.text("传送将在 " + Configuration.delaySec + " 秒后进行...").color(TextColor.fromHexString("#67ff7b")))
            );
        }
        CancellableTask t = Scheduler.runTaskLater(() -> {
            event.getPlayer().teleportAsync(loc).thenAccept(teleported -> {
                if (teleported) {
                    event.getPlayer().sendMessage(Component.text()
                            .append(tag)
                            .append(Component.text("传送成功！").color(TextColor.fromHexString("#67ff7b")))
                    );
                } else {
                    event.getPlayer().sendMessage(Component.text()
                            .append(tag)
                            .append(Component.text("传送失败！").color(TextColor.fromHexString("#ff76aa")))
                    );
                }
                lastTeleportTime.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                tpTasks.remove(event.getPlayer().getUniqueId());
            });
            event.setCancelled(true);
        }, Configuration.delaySec == 0 ? 5 : Configuration.delaySec * 20L);
        tpTasks.put(event.getPlayer().getUniqueId(), t);
    }
}
