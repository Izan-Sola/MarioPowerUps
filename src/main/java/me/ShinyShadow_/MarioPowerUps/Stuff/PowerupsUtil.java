package me.ShinyShadow_.MarioPowerUps.Stuff;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PowerupsUtil implements Listener {

    public enum MixTypes { SUPER, ONEUP }
    private static JavaPlugin plugin;
    private Player player;

    public PowerupsUtil(JavaPlugin plugin) {

    this.plugin = plugin;

    }



    @EventHandler
    public void onRightClick(PlayerInteractEvent event){

        player = event.getPlayer();
        ItemStack inHandItem = player.getInventory().getItemInMainHand();

        ItemMeta itemMeta = inHandItem.getItemMeta();

        if (inHandItem.hasItemMeta() && itemMeta != null && itemMeta.hasLore()) {
            if (itemMeta.getLore().contains("A 1Up mushroom a day, ") ||
                    itemMeta.getLore().contains("This mushroom, despite being ") ||
                    itemMeta.getLore().contains("One Super Mushroom a day, ")) {

                player.setFoodLevel(player.getFoodLevel() - 1);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.setFoodLevel(20), 1L);
            }
        }
    }

    public static boolean checkIngredients(MixTypes mixType, Collection<Entity> itemsInCauldron, Set<Material> itemsInCauldronList, List<Material> ingredientsToCheck, List<Material> mushroomIngredients) {
        for (Entity item : itemsInCauldron) {
            if (item instanceof Item) {
                Material itemType = ((Item) item).getItemStack().getType();
                itemsInCauldronList.add(itemType);

                if (ingredientsToCheck.contains(itemType) || mushroomIngredients.contains(itemType)) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        item.remove();
                    }, 10L);
                }
            }
        }
        if(new HashSet<>(itemsInCauldronList).containsAll(ingredientsToCheck)) {
            
            return true;
        } else {
            return false;
        }
    }


}
