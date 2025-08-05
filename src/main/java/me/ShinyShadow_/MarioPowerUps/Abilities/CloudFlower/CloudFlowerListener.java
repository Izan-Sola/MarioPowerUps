package me.ShinyShadow_.MarioPowerUps.Abilities.CloudFlower;

import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CloudFlowerListener implements Listener {
   private final JavaPlugin plugin;
   private boolean isPowerUpActive = false;
   private BukkitTask effectsTask;

    public CloudFlowerListener(JavaPlugin plugin) {

        this.plugin = plugin;
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack ItemInHand =  player.getInventory().getItemInMainHand();
        ItemStack ItemOffHand = player.getInventory().getItemInOffHand();
        ItemMeta inMeta = ItemInHand.getItemMeta();
        ItemMeta offMeta = ItemOffHand.getItemMeta();

        Damageable inHandDMGMeta = (Damageable) ItemInHand.getItemMeta();
            if (inMeta != null && ItemInHand.isSimilar(ItemManager.Cloud_Flower)) {
                isPowerUpActive = true;
                effects(player);
        }

            if(isPowerUpActive && inMeta != null && inMeta.hasLore() && inMeta.getLore().contains("This flower has a tendency ")&&
               !player.getLocation().add(0, -1, 0).getBlock().getType().isSolid() && inHandDMGMeta.getDamage() != 20) {
               inHandDMGMeta.setDamage(inHandDMGMeta.getDamage() + 1);
               ItemInHand.setItemMeta(inHandDMGMeta);
                new CloudFlowerCloud(player, plugin);

            }


        if (inMeta != null && inMeta.hasLore() &&
                offMeta != null && offMeta.hasLore() &&
                inMeta.getLore().contains("This flower has a tendency ") &&
                offMeta.getLore().contains("A bucket full of clouds!") &&
                inHandDMGMeta.getDamage() == 20) {

            player.sendMessage("hello");
            inHandDMGMeta.setDamage(inHandDMGMeta.getDamage() - 20);
            ItemInHand.setItemMeta(inHandDMGMeta);
            ItemOffHand.setAmount(0);
        }

    }

    public void effects(Player player) {

        effectsTask = new BukkitRunnable() {
                ItemMeta inMeta = player.getInventory().getItemInMainHand().getItemMeta();
                @Override

                public void run () {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 20, 7));
                  //  player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 0));

                    if(inMeta != null && inMeta.hasDisplayName() && inMeta.getDisplayName().contains("Cloud Flower")) {
                      //  isPowerUpActive = false;
                        this.cancel();
                    }

                }
            }.runTaskTimer(plugin, 0L, 2L);

    }
}

