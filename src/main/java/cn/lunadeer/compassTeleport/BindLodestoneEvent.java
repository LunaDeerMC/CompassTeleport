package cn.lunadeer.compassTeleport;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static cn.lunadeer.compassTeleport.CompassTeleport.tag;
import static cn.lunadeer.compassTeleport.utils.Misc.getLocationString;

public class BindLodestoneEvent implements Listener {
    @EventHandler
    public void onBindLodestoneEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != org.bukkit.Material.LODESTONE) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != org.bukkit.Material.COMPASS) return;
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.getPersistentDataContainer().set(
                CompassTeleport.tp_location_key,
                PersistentDataType.STRING,
                getLocationString(event.getPlayer().getLocation())
        );
        item.setItemMeta(meta);
        event.getPlayer().sendMessage(Component.text()
                .append(tag)
                .append(Component.text("将指南针命名后可以使用指南针传送到当前位置。").color(TextColor.fromHexString("#67ff7b")))
        );
    }
}
