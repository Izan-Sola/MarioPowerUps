package me.ShinyShadow_.MarioPowerUps.Abilities;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FireFlowerListener implements Listener{
    private final JavaPlugin plugin;
    private Location eyeLoc;
    private Player player;

    private float powerUpDuration = 20;
    private boolean powerUpActive = false;

    public FireFlowerListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        player = event.getPlayer();


        if(player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 2) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        player = event.getPlayer();
        eyeLoc = player.getEyeLocation();

        if(event.getAction() == Action.RIGHT_CLICK_AIR ||
           event.getAction() == Action.RIGHT_CLICK_BLOCK &&
           player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 2) {
           player.sendMessage(""+ player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData());
            if (!powerUpActive) {
                player.sendMessage("You feel empowered by the Fire Flower! (20s)");
                powerUpActive = true;
         //  empowerPlayer();
                }
                else if(powerUpActive && player.getCooldown(player.getInventory().getItemInMainHand()) <= 0) {
                     player.playSound(player.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 0.1f, 0.1f);
                     player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 0.6f, 0.3f);
                     player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5f, 0.4f);
                     new FlowerFireBall(eyeLoc, player, plugin);
                     player.setCooldown(player.getInventory().getItemInMainHand(), 20);
                }

    }
//    public void empowerPlayer() {
//        if (powerUpActive  && powerUpDuration > 0) {
//
//        }
    }

}


