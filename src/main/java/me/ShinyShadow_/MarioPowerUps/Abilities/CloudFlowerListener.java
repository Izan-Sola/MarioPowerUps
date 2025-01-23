package me.ShinyShadow_.MarioPowerUps.Abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CloudFlowerListener implements Listener {
   private final JavaPlugin plugin;

    public CloudFlowerListener(JavaPlugin plugin) {

        this.plugin = plugin;
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(player.getFoodLevel() != 20) {
            return;
        }
        else if (player.getInventory().getItemInMainHand().getItemMeta() != null &&
                player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Cloud Flower")) {
                player.sendMessage("Power Up Active");
        }
    }
}

