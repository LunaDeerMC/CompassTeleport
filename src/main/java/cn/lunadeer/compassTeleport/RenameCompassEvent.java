package cn.lunadeer.compassTeleport;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class RenameCompassEvent implements Listener {

    @EventHandler
    public void onRenameCompass(PrepareAnvilEvent event) {
        if (event.getResult() == null) return;
        ItemStack result = event.getResult();
        if (result.getType() != org.bukkit.Material.COMPASS) return;
        CompassMeta meta = (CompassMeta) result.getItemMeta();
        if (meta == null) return;
        if (!meta.hasLodestone()) return;
        Location loc = meta.getLodestone();
        if (loc == null) return;
        List<Component> lores = new ArrayList<>();
        lores.add(Component.text("[传送指南针]", TextColor.color(0, 217, 255)).decoration(TextDecoration.ITALIC, false));
        lores.add(Component.text("· ———————————", TextColor.color(27, 64, 127)).decoration(TextDecoration.ITALIC, false));
        lores.add(Component.text("· 手持此指南针右键", TextColor.color(51, 133, 255)).decoration(TextDecoration.ITALIC, false));
        lores.add(Component.text("· 可传送到磁石位置", TextColor.color(51, 133, 255)).decoration(TextDecoration.ITALIC, false));
        lores.add(Component.text("· ———————————", TextColor.color(27, 64, 127)).decoration(TextDecoration.ITALIC, false));
        lores.add(Component.text("· 世界：" + loc.getWorld().getName(), TextColor.color(51, 133, 255)).decoration(TextDecoration.ITALIC, false));
        lores.add(Component.text("· 坐标：", TextColor.color(51, 133, 255)).decoration(TextDecoration.ITALIC, false));
        lores.add(Component.text("·   # X: " + loc.getBlockX(), TextColor.color(51, 133, 255)).decoration(TextDecoration.ITALIC, false));
        lores.add(Component.text("·   # Y: " + loc.getBlockY(), TextColor.color(51, 133, 255)).decoration(TextDecoration.ITALIC, false));
        lores.add(Component.text("·   # Z: " + loc.getBlockZ(), TextColor.color(51, 133, 255)).decoration(TextDecoration.ITALIC, false));
        lores.add(Component.text("· ———————————", TextColor.color(27, 64, 127)).decoration(TextDecoration.ITALIC, false));
        meta.lore(lores);
        meta.getPersistentDataContainer().set(
                CompassTeleport.key,
                PersistentDataType.BOOLEAN,
                true
        );
        result.setItemMeta(meta);
        event.setResult(result);
    }
}
