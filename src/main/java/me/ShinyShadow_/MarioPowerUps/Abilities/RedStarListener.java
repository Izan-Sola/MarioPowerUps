package me.ShinyShadow_.MarioPowerUps.Abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RedStarListener implements Listener {

    private JavaPlugin plugin;
    private Player player;
    Set<Material> ignoredBlocks = Set.of(Material.AIR, Material.GRASS_BLOCK, Material.TALL_GRASS);
    List<Location> barrierLocations =  new ArrayList<>();;

    private List<Block> lineOfSight;
    private Vector direction;
    private Block lastBlock;
    private boolean setBarrier;;

    public RedStarListener(JavaPlugin plugin) {
    this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        player = event.getPlayer();

        if(event.getAction() == Action.RIGHT_CLICK_AIR && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Red Star")) {

            flight(player, plugin);
          //  player.setGliding(true);
        }
    }



    public void flight(Player player, JavaPlugin plugin) {


        setBarrier = true;
        new BukkitRunnable() {

            @Override
            public void run() {
                player.setGliding(true);
                lineOfSight = player.getLineOfSight(ignoredBlocks, 5);
                direction = lineOfSight.getLast().getLocation().toVector().subtract(player.getEyeLocation().toVector());


                 player.setVelocity(direction.multiply(0.07));

                 lastBlock = lineOfSight.getLast();


            }
        }.runTaskTimer(plugin, 0L, 1L);


    }
}
