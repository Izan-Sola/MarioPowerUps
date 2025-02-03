package me.ShinyShadow_.MarioPowerUps.Abilities;

import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class RedStarListener implements Listener {

    private JavaPlugin plugin;
    private Player player;
    Set<Material> ignoredBlocks = Set.of(Material.AIR, Material.GRASS_BLOCK, Material.TALL_GRASS);
    private List<Block> lineOfSight;
    private Vector direction;
    private boolean isGliding = false;
    private boolean timerRunning = false;
    private BukkitTask flightTask;
    private boolean boostReady = true;
    private boolean boost = false;
    public RedStarListener(JavaPlugin plugin) {
    this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        player = event.getPlayer();

        if(event.getAction() == Action.RIGHT_CLICK_AIR && event.getHand() == EquipmentSlot.HAND && player.getInventory().getItemInMainHand().isSimilar(ItemManager.Red_Star)) {
            if(isGliding) {
                flightTask.cancel();
                isGliding = false;
            }
            else {
                flight(player, plugin);

                if(!timerRunning) {
                    BossBar powerUpBar = Bukkit.createBossBar("Red Star Duration", BarColor.RED, BarStyle.SEGMENTED_6);
                    powerUpBar.setProgress(1);
                    powerUpBar.addPlayer(player);

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            powerUpBar.setProgress(powerUpBar.getProgress()-0.00041665);

                            if(powerUpBar.getProgress() <= 0.001) {
                                powerUpBar.removePlayer(player);
                                flightTask.cancel();
                                this.cancel();

                            }

                        }
                    }.runTaskTimer(plugin, 0L, 1L);

                    timerRunning = true;
                }
            }
        }
        if (event.getAction() == Action.LEFT_CLICK_AIR && player.getInventory().getItemInMainHand().isSimilar(ItemManager.Red_Star) &&
                boostReady) {
            boost = true;
            player.sendMessage("hello");
            lineOfSight = player.getLineOfSight(ignoredBlocks, 5);
            direction = lineOfSight.getLast().getLocation().toVector().subtract(player.getEyeLocation().toVector());
           player.setVelocity(direction.multiply(0.15));

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                boostReady = true;
            }, 100);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                boost = false;
            }, 40);
            boostReady = false;
        }
    }


    public void flight(Player player, JavaPlugin plugin) {
        isGliding = true;

        flightTask =  new BukkitRunnable() {
            private double currPoint;
            double radius = 1;
            double speed = 0.25;
            double angle = 0;
            @Override
            public void run() {
                player.setGliding(true);
                lineOfSight = player.getLineOfSight(ignoredBlocks, 5);
                direction = lineOfSight.getLast().getLocation().toVector().subtract(player.getEyeLocation().toVector());

                Location location = player.getLocation();
                Vector forward = location.getDirection().normalize();

                Vector right = forward.clone().crossProduct(new Vector(0, 1, 0)).normalize().multiply(0.55);
                Vector left = right.clone().multiply(-1);
                Vector center = right.clone().multiply(-0.5);

                Vector handForwardOffset = forward.clone().multiply(1.15);
                Vector centerForwardOffset = forward.clone().multiply(4.44);

                Location rightHand = location.clone().add(right).add(handForwardOffset).add(0, -0.15, 0);
                Location leftHand = location.clone().add(left).add(handForwardOffset).add(0, -0.15, 0);
                Location body = location.clone().add(center).add(centerForwardOffset).add(0, -0.15, 0);

                player.spawnParticle(Particle.DUST, rightHand, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(255, 70, 70), 1));
                player.spawnParticle(Particle.DUST, leftHand, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(255, 70, 70), 1));

                player.spawnParticle(Particle.ELECTRIC_SPARK, rightHand, 12, 0.08, 0.08, 0.08, 0.2);
                player.spawnParticle(Particle.ELECTRIC_SPARK, leftHand, 12, 0.08, 0.08, 0.08, 0.2);

                if(!boost) {
                    player.setVelocity(direction.multiply(0.07));
                }
                else {
                    Vector direction = body.getDirection();
                    Vector rightv = direction.clone().crossProduct(new Vector(0, 1, 0)).normalize(); //yaw
                    Vector up = rightv.clone().crossProduct(direction).normalize(); //pitch

                    for (int i = 0; i < 3; i++) {
                        angle += speed;
                        if (angle >= 2 * Math.PI) {
                            angle = 0;
                        }

                        double x = body.getX() + radius * Math.cos(angle) * right.getX() + radius * Math.sin(angle) * direction.getX();
                        double y = body.getY() + radius * Math.sin(angle) * up.getY();
                        double z = body.getZ() + radius * Math.cos(angle) * right.getZ() + radius * Math.sin(angle) * direction.getZ();

                        Location particleLocation = new Location(player.getWorld(), x, y, z);
                        player.spawnParticle(Particle.DUST, particleLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1));
                    }

                }

                if(location.add(0,-1, 0).getBlock().getType() != Material.AIR) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

}
