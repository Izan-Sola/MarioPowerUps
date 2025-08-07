package me.ShinyShadow_.MarioPowerUps.Abilities.IceFlower;

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

public class FlowerIceBall {
    private Random rInt = new Random();
    private Entity iceBall;
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
    private double t = 0.0; // Progress along the curve
    Set<Material> ignoredBlocks = Set.of(Material.AIR, Material.GRASS_BLOCK, Material.TALL_GRASS, Material.SNOW, Material.LIGHT);
    List<Block> tempIceBlocks = new ArrayList<>();
    public FlowerIceBall(Location eyeLoc, Player player, JavaPlugin plugin, Location spawn) {

        lineOfSight = player.getLineOfSight(ignoredBlocks, 13);

        Marker iceBallPather = (Marker) player.getWorld() .spawnEntity(spawn, EntityType.MARKER);
        //eyeLoc.add(0, -0.65, 0)
        startPoint = iceBallPather.getLocation();
        if(bounceDistance > lineOfSight.size()-1) {
            bounceDistance = lineOfSight.size()-1;
        }

        curvePoint = lineOfSight.get(bounceDistance/2).getLocation().add(0, 2.5, 0);
        endPoint = lineOfSight.get(bounceDistance).getLocation().add(0, 0, 0);

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if(iceBallPather.getLocation().getBlock().getType() == Material.WATER) {
                    t = 2;
                    bounceCount = 2;
                }
                if (t > 1.0) {
                    //Check if block under is air, if it is, keep falling until touching ground and bounce
                    if (bounceCount == 2) {
                        iceBallPather.getWorld().spawnParticle(Particle.SNOWFLAKE, iceBallPather.getLocation(), 120, 0.11, 0.11, 0.11, 0.06);
                        iceBallPather.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, iceBallPather.getLocation(), 160, 0.11, 0.11, 0.11, 0.07);
                        iceBallPather.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, iceBallPather.getLocation(), 20, 0.11, 0.11, 0.11, 0.07, Material.ICE.createBlockData());
                        player.playSound(iceBallPather.getLocation(), Sound.BLOCK_POWDER_SNOW_PLACE, 1.7f, 1.2f);

                        // player.sendMessage(endPoint.getBlock().getLocation()+"AAAAAAAAAAAAAAAAAAAA"+fireBallPather.getLocation());
                        Block snowCarpetLoc = iceBallPather.getLocation().getBlock();

                        if(snowCarpetLoc.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                            snowCarpetLoc.getLocation().add(0, -1, 0).getBlock().setBlockData(Material.SNOW.createBlockData());
                        } else {
                            snowCarpetLoc.setBlockData(Material.SNOW.createBlockData());
                        }

                        iceBallPather.remove();
                        cancel();
                        return;
                    }
                    if (!endPoint.getBlock().getType().isSolid()) {
                        currentLocation = iceBallPather.getLocation().add(0, -0.5, 0);
                        iceBallPather.teleport(currentLocation);
                        iceBallPather.getWorld().spawnParticle(Particle.FLAME, currentLocation, 5, 0.1, 0.1, 0.1, 0);
                        iceBallPather.getWorld().spawnParticle(Particle.SMOKE, currentLocation, 5, 0.1, 0.1, 0.1, 0);

                        if(bounceHeightModifier < 6) {
                            bounceHeightModifier += 0.6;
                        }
                        if(!iceBallPather.getLocation().add(0, -1, 0).getBlock().isPassable()) {
                            newPoints(iceBallPather, lineOfSight, player);
                        }
                    }
                    else if(!endPoint.getBlock().isPassable()) {
                        newPoints(iceBallPather, lineOfSight, player);
                    }
                }
                if(t < 1.0) {
                    double x = Math.pow(1 - t, 2) * startPoint.getX() + 2 * (1 - t) * t * curvePoint.getX() + Math.pow(t, 2) * endPoint.getX();
                    double y = Math.pow(1 - t, 2) * startPoint.getY() + 2 * (1 - t) * t * curvePoint.getY() + Math.pow(t, 2) * endPoint.getY() + idkbro;
                    double z = Math.pow(1 - t, 2) * startPoint.getZ() + 2 * (1 - t) * t * curvePoint.getZ() + Math.pow(t, 2) * endPoint.getZ();

                    Location currentLocation = new Location(iceBallPather.getWorld(), x, y, z);
                    iceBallPather.teleport(currentLocation);

                    iceBallPather.getWorld().spawnParticle(Particle.SNOWFLAKE, currentLocation, 10, 0.1, 0.1, 0.1, 0);
                    iceBallPather.getWorld().spawnParticle(Particle.DUST, iceBallPather.getLocation(), 4, 0.2, 0.2, 0.2, 0.1, new Particle.DustOptions(Color.fromRGB(252, 252, 252), 1));
                    iceBallPather.getWorld().spawnParticle(Particle.DUST, iceBallPather.getLocation(), 4, 0.2, 0.2, 0.2, 0.1, new Particle.DustOptions(Color.AQUA, 1));
                    iceBallPather.getWorld().spawnParticle(Particle.BLOCK_CRUMBLE, iceBallPather.getLocation(), 2, 0.11, 0.11, 0.11, 0.07, Material.ICE.createBlockData());
                    Collection<Entity> nearbyEntities = iceBallPather.getWorld().getNearbyEntities(currentLocation, 1, 1, 1);

                    for(Entity entity : nearbyEntities) {
                        if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1));
                            int n = rInt.nextInt(3);
                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 8));
                            if(n == 2) {
                                Block IceBlock1 = entity.getLocation().getBlock();
                                Block IceBlock2 = entity.getLocation().getBlock().getRelative(BlockFace.UP);

                                IceBlock1.setBlockData(Material.ICE.createBlockData());
                                IceBlock2.setBlockData(Material.ICE.createBlockData());

                                tempIceBlocks.add(IceBlock1);
                                tempIceBlocks.add(IceBlock2);

                                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                    for (Block tempBlock : tempIceBlocks) {
                                        if(tempBlock != null) tempBlock.breakNaturally();
                                    }
                                    tempIceBlocks.clear();
                                }, 100);


                            }
                            ((LivingEntity)entity).damage(5);
                            t=2;
                        }
                    }

                    if(!iceBallPather.getLocation().getBlock().isPassable()){
                        t=2;
                    }
                    t += 0.12;
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);

    }
    public void newPoints(Marker iceballPather, List<Block> lineOfSight, Player player) {

        // iceballPather.getWorld().spawnParticle(Particle.FA, iceballPather.getLocation(), 14, 0.2, 0.2, 0.2, 0.1);
        // iceballPather.getWorld().spawnParticle(Particle.FALLING_DUST, iceballPather.getLocation(), 18, 0.2, 0.2, 0.2, 0.2, new Particle.DustOptions(Color.WHITE, 1));
        // iceballPather.getWorld().spawnParticle(Particle.SNOWFLAKE, iceballPather.getLocation(), 12, 0.2, 0.2, 0.2, 1.8);

        player.playSound(iceballPather.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.1f, 4f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.1f, 3.4f);

        Block touchedBlock = iceballPather.getLocation().getBlock();
        Block snowCarpetLoc = touchedBlock.getRelative(BlockFace.UP);

        if(snowCarpetLoc.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
            snowCarpetLoc.getLocation().add(0, -1, 0).getBlock().setBlockData(Material.SNOW.createBlockData());
        } else {
            snowCarpetLoc.setBlockData(Material.SNOW.createBlockData());
        }

        double offSetY = touchedBlock.getY();

        bounceDistance += 4;

        if(bounceDistance > lineOfSight.size()-1) {
            bounceDistance = lineOfSight.size() - 1;
            lastBlock = lineOfSight.getLast().getLocation();
        }
        else {
            lastBlock = lineOfSight.get(bounceDistance).getLocation();
        }
        bounceCount+=1;
        startPoint = iceballPather.getLocation().getBlock().getLocation();
        endPoint = lastBlock.add(0, (-lastBlock.getY())+offSetY, 0);

        if(!endPoint.getBlock().getLocation().add(0, 0, 0).getBlock().isPassable()) {
            endPoint = lastBlock.add(0, (-lastBlock.getY())+offSetY+1, 0);
            idkbro = 1;
        }
        double test = lineOfSight.get(bounceDistance-1).getLocation().getY();
        curvePoint = lineOfSight.get(bounceDistance-1).getLocation().add(0, (-test)+offSetY+bounceHeightModifier, 0);
        bounceHeightModifier = 2.8;
        t = 0;
    }
}
