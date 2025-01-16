package me.ShinyShadow_.MarioPowerUps.Abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class RockMushroomListener implements Listener {


    private final JavaPlugin plugin;
    private Vector direction;
    private List<Block> lineOfSight;
    private BukkitTask task;
    private double distance;
    private double coveredDistance;
    private Location endpoint;
    private Location currentLocation;

    Set<Material> ignoredBlocks = Set.of(Material.AIR, Material.GRASS_BLOCK, Material.TALL_GRASS);

    public RockMushroomListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onEat(FoodLevelChangeEvent event) {

        Player player = (Player) event.getEntity();

        ItemStack eatenItem = event.getItem();

        if (eatenItem.getItemMeta().getLore() == null) {
            return;
        }
        List<String> lore = eatenItem.getItemMeta().getLore();

        if (lore.contains("This mushroom, despite being ")) {
            player.sendMessage("CRACK CRACK GULP");

            lineOfSight = player.getLineOfSight(ignoredBlocks, 8);
            endpoint = lineOfSight.get(8).getLocation().add(0, -2, 0);
            distance = 0.7;
            direction = endpoint.toVector().subtract(player.getEyeLocation().toVector());

            task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (coveredDistance <= distance) {
                        coveredDistance += 0.1;
                        currentLocation = player.getEyeLocation().clone().add(direction.clone().multiply(coveredDistance));
                        player.teleport(currentLocation);
                    }
                    else {
                        coveredDistance = 0;
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 2L);
        }
    }
}
