package me.ShinyShadow_.MarioPowerUps.Abilities;

import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItemRecipeListener implements Listener {

    private final JavaPlugin plugin;
    private int airBottlesCount = 0;
    public CustomItemRecipeListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
        @EventHandler
        public void onPrepareCraft(PrepareItemCraftEvent event) {
            CraftingInventory inventory = event.getInventory();
            ItemStack[] matrix = inventory.getMatrix();
            airBottlesCount = 0;
            boolean hasCloudBucket = false;
            for (ItemStack item : matrix) {
                if (item != null && item.isSimilar(ItemManager.Cloud_Bucket)) {
                    hasCloudBucket = true;
                }
                if (item != null && item.isSimilar(ItemManager.Air_Bottle)) {
                    airBottlesCount += 1;
                }
            }
            if (!hasCloudBucket || airBottlesCount != 3) {
                inventory.setResult(null);
            }
        }
    }

