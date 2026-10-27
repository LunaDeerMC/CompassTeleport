package cn.lunadeer.compassTeleport;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Copy an existing teleport compass onto a plain compass in an anvil.
 * The output contains both compasses, because vanilla anvil logic consumes
 * the left input when the player takes the result.
 */
public class CopyCompassEvent implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack source = event.getInventory().getItem(0);
        ItemStack blank = event.getInventory().getItem(1);
        if (source == null || blank == null) return;
        if (source.getType() != Material.COMPASS || blank.getType() != Material.COMPASS) return;

        // Refuse stacked source compasses: the anvil consumes its entire left slot.
        if (source.getAmount() != 1) return;

        if (!(source.getItemMeta() instanceof CompassMeta meta)) return;
        if (!meta.getPersistentDataContainer().has(CompassTeleport.key, PersistentDataType.BOOLEAN)) return;
        if (!Boolean.TRUE.equals(meta.getPersistentDataContainer().get(CompassTeleport.key, PersistentDataType.BOOLEAN))) return;
        if (!meta.hasLodestone() || meta.getLodestone() == null || !meta.isLodestoneTracked()) return;

        // Only an untouched compass may serve as a blank: no name, PDC,
        // custom data, lodestone or other metadata may be overwritten.
        if (blank.hasItemMeta()) return;

        ItemStack copied = source.clone();
        copied.setAmount(2);
        event.setResult(copied);
        event.getView().setRepairCost(Configuration.costLevel);
        event.getInventory().setRepairCostAmount(1);
    }
}
