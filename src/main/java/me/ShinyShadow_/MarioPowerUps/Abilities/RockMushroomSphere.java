package me.ShinyShadow_.MarioPowerUps.Abilities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class RockMushroomSphere {
    private BukkitTask task1;
    private BukkitTask task2;
    private BukkitTask task3;
    private Location endpoint;
    private Vector direction;
    private List<Block> lineOfSight;
    private double coveredDistance;
    private double chargeTime = 0D;
    private Location currentLocation;
    private int lastBlock = 8;
    private Vector knockBackDirection;
    private boolean breakBlocks = false;
    private double duration = 30;
    Set<Material> ignoredBlocks = Set.of(Material.AIR, Material.GRASS_BLOCK, Material.TALL_GRASS);

    public RockMushroomSphere(Player player, JavaPlugin plugin, ProtocolManager protocolManager) {
        BossBar powerUpBar = Bukkit.createBossBar("RockMushroom Duration", BarColor.WHITE, BarStyle.SOLID);
        powerUpBar.setProgress(1);
        powerUpBar.addPlayer(player);

        protocolManager.addPacketListener(new PacketAdapter(
                plugin,
                ListenerPriority.NORMAL,
                PacketType.Play.Server.EXPLOSION
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {

                event.setCancelled(true);
            }
        });

        task1 = new BukkitRunnable() {
            double phi = 0;

            @Override
            public void run() {
                powerUpBar.setProgress(powerUpBar.getProgress()-0.00162);
                duration -= 0.05;
                if(duration <= 0) {
                    RockMushroomListener.isRockMushRoomPowerActive = false;
                    powerUpBar.removePlayer(player);
                    task2.cancel();
                    cancel();
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20, 2));
                phi += Math.PI / 10;
                for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 40) {
                    Location loc = player.getLocation();
                    double r = 1.5;
                    double x = r * cos(theta) * sin(phi);
                    double y = r * cos(phi) + 1.5;
                    double z = r * sin(theta) * sin(phi);
                    loc.add(x, y, z);
                    player.getWorld().spawnParticle(Particle.DUST, loc, 3, 0, 0, 0, 0.1, new Particle.DustOptions(Color.fromRGB(57, 38, 19), 1));
                    loc.subtract(x, y, z);
                }

                if (player.isSneaking() && RockMushroomListener.isRockMushRoomPowerActive && chargeTime < 3D) {

                    chargeTime += 0.2D;
                }

                if (chargeTime >= 3D && !player.isSneaking()) {
                    lineOfSight = player.getLineOfSight(ignoredBlocks, 18);
                    if ((lineOfSight.size() - 1) < 8) {
                        lastBlock = lineOfSight.size() - 1;
                    }
                    endpoint = lineOfSight.getLast().getLocation().add(0, -1, 0);
                    if (endpoint.getY() > player.getLocation().getY() + 1) {
                        player.sendMessage("Cant dash to the air");
                        return;
                    }
                    doDash(player, plugin);
                    chargeTime = 0D;
                }
            }

        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void doDash(Player player, JavaPlugin plugin) {

        task2 = new BukkitRunnable() {

            double distance = 12;

            @Override
            public void run() {

                player.playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 2.5f, 0.7f);
               // player.playSound(player.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.5f, 0.3f);
                player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 2f, 0.6f);
                if (coveredDistance <= distance) {

                    lineOfSight = player.getLineOfSight(ignoredBlocks, 2);
                    direction = lineOfSight.getLast().getLocation().toVector().subtract(player.getEyeLocation().toVector());
                    if (lineOfSight.getLast().getType().isSolid()) {
                        coveredDistance = 20;
                        breakBlocks = true;
                    } else if (lineOfSight.getLast().getLocation().add(0, -1, 0).getBlock().getType().isSolid()) {
                        player.sendMessage("hello");
                        player.teleport(player.getLocation().add(0, 2, 0));
                    } else if (lineOfSight.getLast().getLocation().getY() > player.getLocation().getY() + 1) {
                        return;
                    } else if (lineOfSight.getLast().isLiquid()) {
                        coveredDistance = 20;
                    }
                    coveredDistance += 0.2;
                    currentLocation = player.getEyeLocation().clone().add(direction.clone().multiply(coveredDistance));
                    //player.teleport(currentLocation);
                    player.setVelocity(direction);
                } else if (breakBlocks) {
                    player.sendMessage("wall");
                    player.setVelocity(new Vector(0, 0, 0));
                    player.sendMessage("" + lineOfSight.getLast().getType());
                    //   player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.5f, 0.6f);
                    for (int i = 4; i > 0; i--) {
                        player.playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 2.5f, 0.7f);
                        player.playSound(player.getLocation(), Sound.BLOCK_ROOTED_DIRT_BREAK, 2.5f, 0.65f);
                        player.playSound(player.getLocation(), Sound.BLOCK_WOOD_BREAK, 2.5f, 0.7f);
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.25f, 0.5f);
                    coveredDistance = 0;
                    chargeTime = 0D;
                    TNTPrimed tnt = (TNTPrimed) player.getLocation().getWorld().spawnEntity(player.getLocation().add(0, 3, 0), EntityType.TNT);

                    tnt.setYield(4F);
                    tnt.setFuseTicks(1);
                    breakBlocks = false;
                    cancel();
                } else if (coveredDistance >= distance) {
                    chargeTime = 0D;
                    coveredDistance = 0;
                    cancel();
                }
                Collection<Entity> nearbyEntities = player.getWorld().getNearbyEntities(player.getLocation(), 2, 2, 2);
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
                        knockBackDirection = entity.getLocation().toVector().subtract(player.getLocation().toVector());
                        entity.setVelocity(new Vector(knockBackDirection.getX(), 1, knockBackDirection.getZ()));
                        ((LivingEntity) entity).damage(6);

                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }


}