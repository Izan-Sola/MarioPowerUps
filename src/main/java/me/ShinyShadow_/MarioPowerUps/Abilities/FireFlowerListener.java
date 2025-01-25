package me.ShinyShadow_.MarioPowerUps.Abilities;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;

public class FireFlowerListener implements Listener{
    private ItemStack onHandItem;
    private final JavaPlugin plugin;
    private Location eyeLoc;
    private Player player;
    private NamespacedKey ns = null;

    public FireFlowerListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvilInv = event.getInventory();

        ItemStack firstItem = anvilInv.getItem(0);
        ItemStack secondItem = anvilInv.getItem(1);

        if (firstItem == null || secondItem == null) {
            return;
        }
        List<String> lore = firstItem.getItemMeta().getLore();

        if (lore.contains("This innocent looking flower ") && secondItem.getType() == Material.BLAZE_POWDER) {

                 ItemStack repairedItem = firstItem.clone();
                 Damageable rMeta = (Damageable) repairedItem.getItemMeta();
                 rMeta.setDamage(rMeta.getDamage()-(secondItem.getAmount()*5));
                 repairedItem.setItemMeta(rMeta);

                 event.setResult(repairedItem);
                 plugin.getServer().getScheduler().runTask(plugin, () -> event.getInventory().setRepairCost(1));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        player = event.getPlayer();

        if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Fire Flower")) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        player = event.getPlayer();
        onHandItem = player.getInventory().getItemInMainHand();

        if(onHandItem.getItemMeta() == null || onHandItem.getItemMeta().getLore() == null) {
            return;
        }

        eyeLoc = player.getEyeLocation();


        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        Damageable onHandDMGMeta = (Damageable) onHandItem.getItemMeta();

        //Recharge Fire Flower
        if(offHandItem.getType() == Material.BLAZE_POWDER && onHandItem.getItemMeta().getLore().contains("This innocent looking flower ") &&
                onHandDMGMeta.getDamage() >= 5) {

            float angle = player.getLocation().add(0, 0.25, 0).getYaw() / 60;
            Location rechargeEffect = player.getLocation().clone().add(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(-1));

            player.getWorld().spawnParticle(Particle.FLAME, rechargeEffect.add(0, 1, 0), 21, 0.02, 0.02, 0.02, 0.05);

            //ItemStack rItem = player.getInventory().getItemInMainHand();
            Damageable rMeta = (Damageable) onHandItem.getItemMeta();
            rMeta.setDamage(rMeta.getDamage() - 5);
            onHandItem.setItemMeta(rMeta);
            offHandItem.setAmount(offHandItem.getAmount()-1);

            return;

        }
        //Shoot Fire Balls
        if (onHandItem.getItemMeta().getLore() != null &&  onHandItem.getItemMeta().getLore().contains("This innocent looking flower ")) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {


                ItemMeta meta = (Damageable) onHandItem.getItemMeta();
                Damageable rMeta = (Damageable) onHandItem.getItemMeta();

                if (player.getCooldown(onHandItem) <= 0 && rMeta.getDamage() != 20) {

                    ((Damageable) meta).setDamage( ((Damageable) meta).getDamage()+1);
                    onHandItem.setItemMeta(meta);
                    player.playSound(player.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 0.1f, 0.1f);
                    player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 0.6f, 0.3f);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5f, 0.4f);

                    float angle = player.getLocation().add(0, 0.25, 0).getYaw() / 60;
                    Location spawn = player.getLocation().clone().add(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(-1));

                    player.getWorld().spawnParticle(Particle.FLAME, spawn.add(0, 1, 0), 21, 0.02, 0.02, 0.02, 0.05);
                    player.getWorld().spawnParticle(Particle.FALLING_LAVA, spawn, 21, 0.11, 0.11, 0.11, 1);

                    new FlowerFireBall(eyeLoc, player, plugin, spawn);
                    player.setCooldown(onHandItem, 20);

                }
            }
        }

    }

}
