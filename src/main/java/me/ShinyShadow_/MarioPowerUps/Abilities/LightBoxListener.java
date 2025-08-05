package me.ShinyShadow_.MarioPowerUps.Abilities;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

public class LightBoxListener implements Listener {
    private final JavaPlugin plugin;
    private final Random random = new Random();

    // Cone parameters made instance variables
    private Vector dir;
    private double tanAngle;
    private double maxDistance;
    private Location eyeLoc;
    private World world;

    public LightBoxListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR &&
                player.getInventory().getItemInMainHand().getType() == Material.GOLD_BLOCK) {

            this.world = player.getWorld();
            BlockDisplay display1 = world.spawn(player.getLocation(), BlockDisplay.class);
            BlockDisplay display2 = world.spawn(player.getLocation(), BlockDisplay.class);
            BlockDisplay display3 = world.spawn(player.getLocation(), BlockDisplay.class);


            display3.setBlock(Bukkit.createBlockData(Material.COPPER_GRATE));
            display3.setTransformation(new Transformation(
                    new Vector3f(0, 0, 0),
                    new Quaternionf(),
                    new Vector3f(0.7f, 0.7f, 0.7f), // scale
                    new Quaternionf()
            ));
            display3.setBillboard(Display.Billboard.FIXED);

            display2.setBlock(Bukkit.createBlockData(Material.GOLD_BLOCK));
            display2.setTransformation(new Transformation(
                    new Vector3f(0, 0, 0),
                    new Quaternionf(),
                    new Vector3f(0.65f, 0.65f, 0.65f), // scale
                    new Quaternionf()
            ));

            display2.setBillboard(Display.Billboard.FIXED);

            display1.setBrightness(new Display.Brightness(15, 15));
            display1.setBlock(Bukkit.createBlockData(Material.RED_CONCRETE));
            display1.setBillboard(Display.Billboard.FIXED);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline() || player.isDead()) {
                        display1.remove();
                        cancel();
                        return;
                    }

                    // Head positioning
                    Location headCenter = player.getLocation().clone().add(0, 1.25, 0);
                    Vector offset = new Vector(-0.5, 0, -0.75);
                    Vector offset2 = new Vector(0.18, 0.15, 0.55);
                    Vector offset3 = new Vector(-0.025, 0.04, 0.);
                    offset.rotateAroundY(-Math.toRadians(headCenter.getYaw()));
                    offset2.rotateAroundY(-Math.toRadians(headCenter.getYaw()));
                    offset3.rotateAroundY(-Math.toRadians(headCenter.getYaw()));
                    display1.teleport(headCenter.add(offset));
                    display2.teleport(headCenter.add(offset2));
                    display3.teleport(headCenter.add(offset3));

                    // Set cone parameters
                    eyeLoc = player.getEyeLocation();
                    dir = eyeLoc.getDirection().normalize();
                    maxDistance = 12;
                    double coneAngleDegrees = 20;
                    tanAngle = Math.tan(Math.toRadians(coneAngleDegrees));


                    double step = 0.20;
                    Location origin = eyeLoc.clone();
                    for (double i = 0; i <= maxDistance; i += step) {
                        int samples = 8;
                        for (int s = 0; s < samples; s++) {
                            double fixedAngle = (2 * Math.PI / samples) * s;
                            Vector perp1 = dir.clone().crossProduct(new Vector(0, 1, 0));
                            if (perp1.lengthSquared() == 0) {
                                perp1 = dir.clone().crossProduct(new Vector(1, 0, 0));
                            }
                            perp1.normalize();
                            Vector perp2 = dir.clone().crossProduct(perp1).normalize();
                            double currentRadius = tanAngle * i;
                            Vector currentOffset = perp1.multiply(currentRadius * Math.cos(fixedAngle))
                                    .add(perp2.multiply(currentRadius * Math.sin(fixedAngle)));
                            Vector targetVec = dir.clone().multiply(i).add(currentOffset);
                            Location targetLoc = origin.clone().add(targetVec);

                            Block block = targetLoc.getBlock();
                            if (block.getType() == Material.AIR) {
                                block.setBlockData(Bukkit.createBlockData("minecraft:light[level=12]"), false);
                                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                    if (block.getType() == Material.LIGHT) {
                                        block.setType(Material.AIR);
                                    }
                                }, 10L);
                            }
                        }
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline() || player.isDead()) {
                        cancel();
                        return;
                    }

                    // Spawn random particles using the instance variables
                    for (int i = 0; i < 9 + random.nextInt(6); i++) {
                        double distance = Math.sqrt(random.nextDouble()) * maxDistance;
                        double angle = random.nextDouble() * 2 * Math.PI;
                        double radius = tanAngle * distance * random.nextDouble();

                        Vector perp = new Vector(
                                random.nextDouble() - 0.5,
                                random.nextDouble() - 0.5,
                                random.nextDouble() - 0.5
                        ).crossProduct(dir).normalize();

                        Vector offsetVec = perp.rotateAroundAxis(dir, angle).multiply(radius);
                        Vector targetVec = dir.clone().multiply(distance).add(offsetVec);
                        Location particleLoc = eyeLoc.clone().add(targetVec);

                        world.spawnParticle(
                                Particle.DUST,
                                particleLoc,
                                1,
                                0, 0, 0,
                                0,
                                new Particle.DustOptions(Color.fromRGB(255, 255, 0), 1.0f)
                        );
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L);
        }
    }
}