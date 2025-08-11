package me.ShinyShadow_.MarioPowerUps.Stuff;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class FlowerBalls {

    public enum BallType { FIRE, ICE, GOLD }

    private JavaPlugin plugin;
    private Player player;
    private final BallType ballType;
    private final Random rInt = new Random();
    private List<Block> lineOfSight;
    private BukkitTask task;
    public int idkbro = 0;
    private Location lastBlock;
    private Location currentLocation;
    private Location curvePoint;
    private Location startPoint;
    private Location endPoint;
    private Vector direction;
    private int bounceDistance = 5;
    private int bounceCount = 0;
    private double bounceHeightModifier = 2.8;
    private double t = 0.0;
    private final Set<Material> ignoredBlocks;
    private final List<Block> tempIceBlocks = new ArrayList<>();

    public FlowerBalls(Location eyeLoc, Player player, JavaPlugin plugin, Location spawn, BallType ballType) {
        this.ballType = ballType;
        this.player = player;
        this.ignoredBlocks = Set.of(Material.AIR, Material.GRASS_BLOCK, Material.TALL_GRASS, Material.SNOW, Material.LIGHT);
        this.plugin = plugin;

        lineOfSight = player.getLineOfSight(ignoredBlocks, 13);
        Marker pather = (Marker) player.getWorld().spawnEntity(spawn, EntityType.MARKER);

        startPoint = pather.getLocation();
        if (bounceDistance > lineOfSight.size() - 1) bounceDistance = lineOfSight.size() - 1;

        curvePoint = lineOfSight.get(bounceDistance / 2).getLocation().add(0, 2.5, 0);
        endPoint = lineOfSight.get(bounceDistance).getLocation();

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (pather.getLocation().getBlock().getType() == Material.WATER) {
                    t = 2;
                    bounceCount = 2;
                }

                if (t > 1.0) {
                    if (bounceCount == 2) {
                        endParticles(player, pather);
                        if (ballType == BallType.ICE) placeSnow(pather.getLocation().getBlock());
                        pather.remove();
                        cancel();
                        return;
                    }
                    if (!endPoint.getBlock().getType().isSolid()) {
                        currentLocation = pather.getLocation().add(0, -0.5, 0);
                        pather.teleport(currentLocation);
                        spawnMidAirEffects(pather);
                        if (bounceHeightModifier < 6) bounceHeightModifier += 0.6;
                        if (!pather.getLocation().add(0, -1, 0).getBlock().isPassable())
                            newPoints(pather, lineOfSight, player);
                    } else if (!endPoint.getBlock().isPassable()) {
                        newPoints(pather, lineOfSight, player);
                    }
                }

                if (t < 1.0) {
                    double x = Math.pow(1 - t, 2) * startPoint.getX() + 2 * (1 - t) * t * curvePoint.getX() + Math.pow(t, 2) * endPoint.getX();
                    double y = Math.pow(1 - t, 2) * startPoint.getY() + 2 * (1 - t) * t * curvePoint.getY() + Math.pow(t, 2) * endPoint.getY() + idkbro;
                    double z = Math.pow(1 - t, 2) * startPoint.getZ() + 2 * (1 - t) * t * curvePoint.getZ() + Math.pow(t, 2) * endPoint.getZ();

                    Location currentLocation = new Location(pather.getWorld(), x, y, z);
                    pather.teleport(currentLocation);

                    pathParticles(pather, currentLocation);

                    for (Entity entity : pather.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                        if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
                            damageEntity((LivingEntity) entity, plugin);
                            t = 2;
                        }
                    }

                    if (!pather.getLocation().getBlock().isPassable()) t = 2;
                    t += 0.12;
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void endParticles(Player player, Marker pather) {
        if (ballType == BallType.FIRE) {
            pather.getWorld().spawnParticle(Particle.FLAME, pather.getLocation(), 120, 0.11, 0.11, 0.11, 0.06);
            pather.getWorld().spawnParticle(Particle.SMOKE, pather.getLocation(), 160, 0.11, 0.11, 0.11, 0.07);
            pather.getWorld().spawnParticle(Particle.FALLING_LAVA, pather.getLocation(), 62, 0.8, 0.6, 0.8, 1.8);
            player.playSound(pather.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.3f, 1.8f);
        } else {
            pather.getWorld().spawnParticle(Particle.SNOWFLAKE, pather.getLocation(), 120, 0.11, 0.11, 0.11, 0.06);
            pather.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, pather.getLocation(), 160, 0.11, 0.11, 0.11, 0.07);
            pather.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, pather.getLocation(), 20, 0.11, 0.11, 0.11, 0.07, Material.ICE.createBlockData());
            player.playSound(pather.getLocation(), Sound.BLOCK_POWDER_SNOW_PLACE, 1.7f, 1.2f);
        }
    }

    private void spawnMidAirEffects(Marker pather) {
        if (ballType == BallType.FIRE) {
            pather.getWorld().spawnParticle(Particle.FLAME, currentLocation, 5, 0.1, 0.1, 0.1, 0);
            pather.getWorld().spawnParticle(Particle.SMOKE, currentLocation, 5, 0.1, 0.1, 0.1, 0);
        } else {
            pather.getWorld().spawnParticle(Particle.SNOWFLAKE, currentLocation, 10, 0.1, 0.1, 0.1, 0);
            pather.getWorld().spawnParticle(Particle.DUST, currentLocation, 4, 0.2, 0.2, 0.2, 0.1, new Particle.DustOptions(Color.fromRGB(252, 252, 252), 1));
            pather.getWorld().spawnParticle(Particle.DUST, currentLocation, 4, 0.2, 0.2, 0.2, 0.1, new Particle.DustOptions(Color.AQUA, 1));
            pather.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, currentLocation, 2, 0.11, 0.11, 0.11, 0.07, Material.ICE.createBlockData());
        }
    }

    private void pathParticles(Marker pather, Location currentLocation) {
        if (ballType == BallType.FIRE) {
            pather.getWorld().spawnParticle(Particle.FLAME, currentLocation, 8, 0.1, 0.1, 0.1, 0);
            pather.getWorld().spawnParticle(Particle.SMOKE, currentLocation, 8, 0.1, 0.1, 0.1, 0);

            Block lightTempBlock = pather.getLocation().getBlock();
            if(!lightTempBlock.getType().isSolid()) {
                lightTempBlock.setBlockData(Bukkit.createBlockData("minecraft:light[level=10]"));
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    lightTempBlock.setBlockData(Material.AIR.createBlockData());
                }, 5L);
            }
        } else {
            pather.getWorld().spawnParticle(Particle.SNOWFLAKE, currentLocation, 10, 0.1, 0.1, 0.1, 0);
            pather.getWorld().spawnParticle(Particle.DUST, currentLocation, 4, 0.2, 0.2, 0.2, 0.1, new Particle.DustOptions(Color.fromRGB(252, 252, 252), 1));
            pather.getWorld().spawnParticle(Particle.DUST, currentLocation, 4, 0.2, 0.2, 0.2, 0.1, new Particle.DustOptions(Color.AQUA, 1));
            pather.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, currentLocation, 2, 0.11, 0.11, 0.11, 0.07, Material.ICE.createBlockData());
        }
    }

    private void damageEntity(LivingEntity entity, JavaPlugin plugin) {
        if (ballType == BallType.FIRE) {
            entity.setFireTicks(80);
            entity.damage(6);
        } else {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1));
            if (rInt.nextInt(3) == 2) {
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 8));
                Block IceBlock1 = entity.getLocation().getBlock();
                Block IceBlock2 = IceBlock1.getRelative(BlockFace.UP);
                IceBlock1.setBlockData(Material.ICE.createBlockData());
                IceBlock2.setBlockData(Material.ICE.createBlockData());
                tempIceBlocks.add(IceBlock1);
                tempIceBlocks.add(IceBlock2);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for (Block tempBlock : tempIceBlocks) if (tempBlock != null) tempBlock.breakNaturally();
                    tempIceBlocks.clear();
                }, 100);
            }
            entity.damage(5);
        }
    }

    private void placeSnow(Block block) {
        Block relative = block.getRelative(BlockFace.DOWN);
        if (relative.getType() == Material.AIR) {
            relative.setBlockData(Material.SNOW.createBlockData());
        } else if (relative.getType().isSolid()) {
            if( relative.getRelative(BlockFace.UP).getType().isSolid()) {
                relative.getRelative(BlockFace.UP).getRelative(BlockFace.UP).setBlockData(Material.SNOW.createBlockData());
            } else {
                relative.getRelative(BlockFace.UP).setBlockData(Material.SNOW.createBlockData());
            }
        }
    }

    public void newPoints(Marker pather, List<Block> lineOfSight, Player player) {
        player.playSound(pather.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.1f, 4f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.1f, 3.4f);

        if (ballType == BallType.ICE) placeSnow(pather.getLocation().getBlock());

        Block touchedBlock = pather.getLocation().getBlock();
        double offSetY = touchedBlock.getY();

        bounceDistance += 4;
        if (bounceDistance > lineOfSight.size() - 1) {
            bounceDistance = lineOfSight.size() - 1;
            lastBlock = lineOfSight.getLast().getLocation();
        } else {
            lastBlock = lineOfSight.get(bounceDistance).getLocation();
        }
        bounceCount++;
        startPoint = pather.getLocation().getBlock().getLocation();
        endPoint = lastBlock.add(0, (-lastBlock.getY()) + offSetY, 0);

        if (!endPoint.getBlock().getLocation().getBlock().isPassable()) {
            endPoint = lastBlock.add(0, (-lastBlock.getY()) + offSetY + 1, 0);
            idkbro = 1;
        }
        double test = lineOfSight.get(bounceDistance - 1).getLocation().getY();
        curvePoint = lineOfSight.get(bounceDistance - 1).getLocation().add(0, (-test) + offSetY + bounceHeightModifier, 0);
        bounceHeightModifier = 2.8;
        t = 0;
    }
}
