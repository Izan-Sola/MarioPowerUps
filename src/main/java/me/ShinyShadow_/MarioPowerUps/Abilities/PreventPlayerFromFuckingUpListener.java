package me.ShinyShadow_.MarioPowerUps.Abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PreventPlayerFromFuckingUpListener implements Listener {


    private Player player;
    private JavaPlugin plugin;
    private ItemStack itemInHand;
    private List<String> customItems = Arrays.asList(
            "Fire Flower", "★ Rainbow Star ★", "Cloud Flower", "Air Bottle",
            "Cloud Bucket", "Rainbow Essence", "Refined Rainbow Essence", "★ Red Star ★"
    );


    Map<UUID, Integer> itemCooldowns = new LinkedHashMap<>();


//itemStacksWithCD.add(new ItemStack( ItemManager.Red_Star);

    public PreventPlayerFromFuckingUpListener(JavaPlugin plugin) {
        this.plugin = plugin;
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
    public void onBuck(PlayerBucketFillEvent event) {
        player = event.getPlayer();
        itemInHand = player.getInventory().getItemInMainHand();

        for (String name : customItems) {
            if (itemInHand != null && itemInHand.getItemMeta().getDisplayName().contains(name)) {
                event.setCancelled(true);
            }
        }
    }




}




