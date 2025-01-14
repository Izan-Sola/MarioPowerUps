package me.ShinyShadow_.MarioPowerUps.Abilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FlowerFireBall {

    private Entity fireBall;
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

    public FlowerFireBall(Location eyeLoc, Player player, JavaPlugin plugin) {

        Set<Material> ignoredBlocks = Set.of(Material.AIR, Material.GRASS_BLOCK, Material.TALL_GRASS);
        lineOfSight = player.getLineOfSight(ignoredBlocks, 13);

        Marker fireBallPather = (Marker) player.getWorld() .spawnEntity(eyeLoc.add(0, -0.65, 0), EntityType.MARKER);

        startPoint = fireBallPather.getLocation();
        curvePoint = lineOfSight.get(bounceDistance/2).getLocation().add(0, 2.5, 0);
        endPoint = lineOfSight.get(bounceDistance).getLocation().add(0, 0, 0);

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (t > 1.0) {

                if (bounceCount == 2) {
                    fireBallPather.getWorld().spawnParticle(Particle.FLAME, fireBallPather.getLocation(), 120, 0.11, 0.11, 0.11, 0.06);
                    fireBallPather.getWorld().spawnParticle(Particle.SMOKE, fireBallPather.getLocation(), 160, 0.11, 0.11, 0.11, 0.07);
                    fireBallPather.getWorld().spawnParticle(Particle.FALLING_LAVA, fireBallPather.getLocation(), 62, 0.8, 0.6, 0.8, 1.8);
                    player.playSound(fireBallPather.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.3f, 1.8f);

                   // player.sendMessage(endPoint.getBlock().getLocation()+"AAAAAAAAAAAAAAAAAAAA"+fireBallPather.getLocation());
                    fireBallPather.remove();
                    cancel();
                    return;
                }
                    //Check if the block under is passable, if it is, keep falling until touching the ground and set
                    //new start, curve and end points
                    if (!endPoint.getBlock().getType().isSolid()) {
                        currentLocation = fireBallPather.getLocation().add(0, -0.5, 0);
                        fireBallPather.teleport(currentLocation);
                        fireBallPather.getWorld().spawnParticle(Particle.FLAME, currentLocation, 5, 0.1, 0.1, 0.1, 0);
                        fireBallPather.getWorld().spawnParticle(Particle.SMOKE, currentLocation, 5, 0.1, 0.1, 0.1, 0);

                        if(bounceHeightModifier < 6) {
                            bounceHeightModifier += 0.6;
                        }
                        if(!fireBallPather.getLocation().add(0, -1, 0).getBlock().isPassable()) {
                            newPoints(fireBallPather, lineOfSight, player);
                        }
                    }
                    else if(!endPoint.getBlock().isPassable()) {
                        newPoints(fireBallPather, lineOfSight, player);
                    }
                }
                if(t < 1.0) {
                    double x = Math.pow(1 - t, 2) * startPoint.getX() + 2 * (1 - t) * t * curvePoint.getX() + Math.pow(t, 2) * endPoint.getX();
                    double y = Math.pow(1 - t, 2) * startPoint.getY() + 2 * (1 - t) * t * curvePoint.getY() + Math.pow(t, 2) * endPoint.getY() + idkbro;
                    double z = Math.pow(1 - t, 2) * startPoint.getZ() + 2 * (1 - t) * t * curvePoint.getZ() + Math.pow(t, 2) * endPoint.getZ();

                    Location currentLocation = new Location(fireBallPather.getWorld(), x, y, z);
                    fireBallPather.teleport(currentLocation);

                    fireBallPather.getWorld().spawnParticle(Particle.FLAME, currentLocation, 8, 0.1, 0.1, 0.1, 0);
                    fireBallPather.getWorld().spawnParticle(Particle.SMOKE, currentLocation, 8, 0.1, 0.1, 0.1, 0);
                    Collection<Entity> nearbyEntities = fireBallPather.getWorld().getNearbyEntities(currentLocation, 1, 1, 1);

                    for(Entity entity : nearbyEntities) {
                        if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
                            entity.setFireTicks(80);
                            ((LivingEntity)entity).damage(6);
        
                            t=2;
                        }
                    
                    if(!fireBallPather.getLocation().getBlock().isPassable()){
                        t=2;
                    }
                    t += 0.1;
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);

    }
    public void newPoints(Marker fireBallPather, List<Block> lineOfSight, Player player) {

        fireBallPather.getWorld().spawnParticle(Particle.FLAME, fireBallPather.getLocation(), 14, 0.2, 0.2, 0.2, 0.1);
        fireBallPather.getWorld().spawnParticle(Particle.SMOKE, fireBallPather.getLocation(), 18, 0.2, 0.2, 0.2, 0.2);
        fireBallPather.getWorld().spawnParticle(Particle.FALLING_LAVA, fireBallPather.getLocation(), 12, 0.2, 0.2, 0.2, 1.8);

        player.playSound(fireBallPather.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.1f, 4f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.1f, 3.4f);

        Block touchedBlock = fireBallPather.getLocation().getBlock();

        double offSetY = touchedBlock.getY();

        bounceDistance += 4;
        //Prevent endpoint from being out of bounds withing the line of sight range
        if(bounceDistance > lineOfSight.size()-1) {
            bounceDistance = lineOfSight.size() - 1;
            lastBlock = lineOfSight.getLast().getLocation();
        }
        else {
            lastBlock = lineOfSight.get(bounceDistance).getLocation();
        }
        bounceCount+=1;
        
        //If the Y level of the endpoint is different from the Y of the start point (i.e: you throw the fireball from a cliff)
        //substract the Y value of the endpoint to itself (i.e: lastblock Y = 60, so 60-60 = 0Y)
        //then add the Y of the new startpoint, (which would be the floor level, the block the fireball touched)
        //(i.e now the endpoint Y += start point Y --> 0 + 40 = 40Y) so now both the startpoint and the endpoint are at the same Y level.
        //Then just add a bit of height to the curve (40 + 2 0 42Y) and done.
        
        startPoint = fireBallPather.getLocation().getBlock().getLocation();
        endPoint = lastBlock.add(0, (-lastBlock.getY())+offSetY, 0);

        //In case the endpoint, being at the same Y level as the startpoint is NOT a passable block (meaning stairs, uneven terrain...)
        //it adds +1 to the Y level of the endpoint, so it can jump over different Y levels, unless theres a tall wall,
        //since it only adds +1 to the Y level one time, it wont go over them, but instead collide and explode
        
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
