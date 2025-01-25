package me.ShinyShadow_.MarioPowerUps.Abilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CloudFlowerCloud {

    List<Vector> offsetsList =  Arrays.asList(
            new Vector(0, -1, 0),
            new Vector(1, -1, 0),
            new Vector(1, -1, 1),
            new Vector(-1, -1, 0),
            new Vector(-1, -1, 1),
            new Vector(-1, -1, -1),
            new Vector(1, -1, -1),
            new Vector(0, -1, -1),
            new Vector(0, -1, 1)
    );
    List<Location> placedBlocksList = new ArrayList<>();

    private BukkitTask cloudParticlesTask;

    public CloudFlowerCloud(Player player, JavaPlugin plugin) {

        for (Vector offset : offsetsList) {
            Location bLoc = player.getLocation().add(offset);
            if( !bLoc.getBlock().getType().isSolid()) {
                bLoc.getBlock().setType(Material.PALE_OAK_SLAB);
                placedBlocksList.add(bLoc);
            }
        }
        player.getWorld().spawnParticle(Particle.CLOUD, placedBlocksList.getFirst().getBlock().getLocation().add(0.5, 0, 0.5), 50, 0.4, 0.4, 0.4, 1);
        cloudParticles(player, plugin);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Location block : placedBlocksList) {
                 block.getBlock().setType(Material.AIR);
                 cloudParticlesTask.cancel();
            }
        }, 300);
    }

    public void cloudParticles(Player player, JavaPlugin plugin) {

        cloudParticlesTask = new BukkitRunnable() {

            @Override
            public void run(){
                player.getWorld().spawnParticle(Particle.CLOUD, placedBlocksList.getFirst().getBlock().getLocation().add(0.5, 0.25, 0.5), 160, 1.1, 0.6, 1.1, 0);

            }
        }.runTaskTimer(plugin, 0L, 1L);


    }
}

