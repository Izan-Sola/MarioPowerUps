package me.ShinyShadow_.MarioPowerUps.Stuff;

import me.ShinyShadow_.MarioPowerUps.Init;
import me.ShinyShadow_.MarioPowerUps.PowerUps.Mushrooms.RockMushroom.RockMushroomListener;
import me.ShinyShadow_.MarioPowerUps.PowerUps.Stars.RainbowStarListener;
import me.ShinyShadow_.MarioPowerUps.PowerUps.Stars.RedStarListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class PreventWeirdStuffFromHappeningListener implements Listener {


    private Player player;
    private JavaPlugin plugin;
    private ItemStack itemInHand;
    private List<String> customItems = Arrays.asList(
            "Fire Flower", "★ Rainbow Star ★", "Cloud Flower", "Air Bottle",
            "Cloud Bucket", "Rainbow Essence", "Refined Rainbow Essence", "★ Red Star ★"
    );




    public PreventWeirdStuffFromHappeningListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static Boolean isAnyConflictivePowerActive() {

        if(RedStarListener.PowerUpActive || RockMushroomListener.isRockMushRoomPowerActive || RainbowStarListener.PowerUpActive) {
            return true;
        } else {
            return false;
        }

    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        player = event.getPlayer();
        itemInHand = player.getInventory().getItemInMainHand();

        for (String name : customItems) {
            if (itemInHand != null && itemInHand.getItemMeta().getDisplayName().contains(name)) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent event) {
        player = event.getPlayer();
        itemInHand = player.getInventory().getItemInMainHand();

        for (String name : customItems) {
            if (itemInHand != null && itemInHand.getItemMeta().getDisplayName().contains(name)) {
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        player = event.getPlayer();
        itemInHand = player.getInventory().getItemInMainHand();

        for (String name : customItems) {
            if (itemInHand != null && itemInHand.getItemMeta().getDisplayName().contains(name)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Init.removeAndClearStuff();
    }
    @EventHandler
    public void onLogOff(PlayerQuitEvent event) {
        Init.removeAndClearStuff();
    }
    @EventHandler
    public void onServerReload(ServerLoadEvent event) {
        Init.removeAndClearStuff();
    }



}




