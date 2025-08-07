package me.ShinyShadow_.MarioPowerUps.Abilities;

import me.ShinyShadow_.MarioPowerUps.Init;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

public class LightBoxListener implements Listener {
    private final JavaPlugin plugin;
    private final Random random = new Random();

    private Vector dir;
    private double tanAngle;
    private double maxDistance;
    private Location eyeLoc;
    private World world;
    private Boolean wearingBeamBox = false;
    private BukkitTask particleTask;
    private BukkitTask lightTask;
    //public static List<Block> tempLightBlocks = new ArrayList<>();
    BlockDisplay display1;
    BlockDisplay display2;
    BlockDisplay display3;
    public LightBoxListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemStack itemOffHand = player.getInventory().getItemInOffHand();
        Damageable itemInHandDMGMeta = ((Damageable) itemInHand.getItemMeta());

        if (event.getAction() == Action.RIGHT_CLICK_AIR && itemInHand.getItemMeta().hasLore() &&
                itemInHand.getItemMeta().getLore().contains("Wear it on your head") && itemInHandDMGMeta.getDamage() != 300) {

            if(wearingBeamBox) {
                removeBeamBox();
                return;

            } else {
                wearBeamBox(player, itemInHand, itemInHandDMGMeta);
                wearingBeamBox = true;

            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR && itemInHand.getItemMeta().hasLore() &&
                itemInHand.getItemMeta().getLore().contains("Wear it on your head") && itemInHandDMGMeta.getDamage() != 0 &&
                itemOffHand.getType() == Material.COAL) {

                itemInHandDMGMeta.setDamage(itemInHandDMGMeta.getDamage() - 10);
                itemInHand.setItemMeta(itemInHandDMGMeta);
                itemOffHand.setAmount(itemOffHand.getAmount()-1);
        }

    }



    public void wearBeamBox(Player player, ItemStack BeamBoxItem, Damageable BeamBoxDMGMeta) {

        this.world = player.getWorld();
        display1 = world.spawn(player.getLocation(), BlockDisplay.class);
        display2 = world.spawn(player.getLocation(), BlockDisplay.class);
        display3 = world.spawn(player.getLocation(), BlockDisplay.class);

        display3.setBlock(Bukkit.createBlockData(Material.COPPER_GRATE));
        display3.setTransformation(new Transformation(
                new Vector3f(0, 0, 0),
                new Quaternionf(),
                new Vector3f(0.7f, 0.7f, 0.7f),
                new Quaternionf()
        ));
        display3.setBillboard(Display.Billboard.FIXED);

        display2.setBlock(Bukkit.createBlockData(Material.GOLD_BLOCK));
        display2.setTransformation(new Transformation(
                new Vector3f(0, 0, 0),
                new Quaternionf(),
                new Vector3f(0.65f, 0.65f, 0.65f),
                new Quaternionf()
        ));

        display2.setBillboard(Display.Billboard.FIXED);

        display1.setBrightness(new Display.Brightness(15, 15));
        display1.setBlock(Bukkit.createBlockData(Material.RED_CONCRETE));
        display1.setBillboard(Display.Billboard.FIXED);

        lightTask = new BukkitRunnable() {
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
                maxDistance = 10;
                double coneAngleDegrees = 6;
                tanAngle = Math.tan(Math.toRadians(coneAngleDegrees));


                double step = 0.25;
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
                            Init.tempBlocks.add(block);

                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                Block r = block;
                                if (block.getType() == Material.LIGHT) {
                                    block.setType(Material.AIR);
                                    Init.tempBlocks.remove(r);
                                }

                            }, 12L);
                        }
                    }
                }

            }
        }.runTaskTimer(plugin, 0L, 1L);

        particleTask = new BukkitRunnable() {
            @Override
            public void run() {

                if (!player.isOnline() || player.isDead()) {
                    removeBeamBox();
                    cancel();
                    return;
                }
                // Spawn random particles
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

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        world.spawnParticle(Particle.DUST, particleLoc, 1, 0, 0, 0, 0,
                                new Particle.DustOptions(Color.fromRGB(255, 255, 0), 1.0f)
                        );
                    }, 10*i);
                }
                for (int slot = 0; slot < player.getInventory().getSize(); slot++) {
                    ItemStack item = player.getInventory().getItem(slot);
                    if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() &&
                            item.getItemMeta().getLore().contains("Wear it on your head")) {

                        ItemMeta meta = item.getItemMeta();
                        if (!(meta instanceof Damageable)) return;

                        Damageable dmgMeta = (Damageable) meta;
                        dmgMeta.setDamage(dmgMeta.getDamage() + 1);
                        item.setItemMeta(dmgMeta);

                        if(dmgMeta.getDamage() == 300) removeBeamBox();
                        break;

                    }
                }

            }


        }.runTaskTimer(plugin, 0L, 20L);


    }

    public void removeBeamBox() {

        wearingBeamBox = false;
        particleTask.cancel();
        lightTask.cancel();
        display1.remove();
        display2.remove();
        display3.remove();

    }

    @EventHandler
    public void onServerReload(ServerLoadEvent event) {
        if(event.getType() == ServerLoadEvent.LoadType.RELOAD) {
            wearingBeamBox = false;
            particleTask.cancel();
            lightTask.cancel();
            display1.remove();
            display2.remove();
            display3.remove();
        }
    }
}
