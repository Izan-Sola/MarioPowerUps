package me.ShinyShadow_.MarioPowerUps.PowerUps.Flowers;

//import me.ShinyShadow_.MarioPowerUps.Abilities.FireFlower.FlowerFireBall;

import me.ShinyShadow_.MarioPowerUps.Stuff.FlowerBalls;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

;

public class IceFlowerListener implements Listener{
    private ItemStack onHandItem;
    private final JavaPlugin plugin;
    private Location eyeLoc;
    private Player player;
    private NamespacedKey ns = null;

    public IceFlowerListener(JavaPlugin plugin) {
        this.plugin = plugin;
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
        if(offHandItem.getType() == Material.POWDER_SNOW_BUCKET && onHandItem.getItemMeta().getLore().contains("This cold flower") &&
                onHandDMGMeta.getDamage() >= 20) {

            float angle = player.getLocation().add(0, 0.25, 0).getYaw() / 60;
            Location rechargeEffect = player.getLocation().clone().add(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(-1));

            player.getWorld().spawnParticle(Particle.FLAME, rechargeEffect.add(0, 1, 0), 21, 0.02, 0.02, 0.02, 0.05);

            //ItemStack rItem = player.getInventory().getItemInMainHand();
            Damageable rMeta = (Damageable) onHandItem.getItemMeta();
            rMeta.setDamage(rMeta.getDamage() - 20);
            onHandItem.setItemMeta(rMeta);
            offHandItem.setAmount(offHandItem.getAmount()-1);

            return;

        }
        //Shoot Ice Balls
        if (onHandItem.getItemMeta().getLore() != null &&  onHandItem.getItemMeta().getLore().contains("This cold flower")) {
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

                    player.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, spawn.add(0, 1, 0), 21, 0.02, 0.02, 0.02, 0.05);
                    player.getWorld().spawnParticle(Particle.SNOWFLAKE, spawn, 21, 0.11, 0.11, 0.11, 1);

                    new FlowerBalls(eyeLoc, player, plugin, spawn, FlowerBalls.BallType.ICE);
                    player.setCooldown(onHandItem, 20);

                }
            }
        }

    }

}
