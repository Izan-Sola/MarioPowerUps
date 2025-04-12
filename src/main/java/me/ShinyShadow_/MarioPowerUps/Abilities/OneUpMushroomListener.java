package me.ShinyShadow_.MarioPowerUps.Abilities;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OneUpMushroomListener implements Listener {

    private ProtocolManager protocolManager;
    private JavaPlugin plugin;
    private Player player;
    public OneUpMushroomListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }


//this is a test
    @EventHandler
    public void onEat(FoodLevelChangeEvent event) {

        Player player = (Player) event.getEntity();

        ItemStack eatenItem = event.getItem();
        if(eatenItem == null || eatenItem.getItemMeta().getLore() == null ) {
            return;
        }
        if (eatenItem.getItemMeta().getLore().contains("A 1Up mushroom a day, ")) {
            player.setMaxHealth(player.getMaxHealth()+20);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 4));
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.setMaxHealth(20);
            }, 6000);
        }
            protocolManager = ProtocolLibrary.getProtocolManager();

    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(player.getFoodLevel() != 20) {
            return;
        }
        else if (player.getInventory().getItemInMainHand().getItemMeta() != null &&
                player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("1-Up Mushroom")) {
            if (player.getMaxHealth() == 20) {
                player.setFoodLevel(19);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.setFoodLevel(20), 1L);
            }
            else {
                event.setCancelled(true);
            }
        }
    }
}


