package me.ShinyShadow_.MarioPowerUps.PowerUps.HeadBoxes;

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
    private static Boolean wearingBeamBox = false;
    private static BukkitTask particleTask;
    private static BukkitTask lightTask;
    //public static List<Block> tempLightBlocks = new ArrayList<>();
    private static BlockDisplay box;
    private static BlockDisplay light;
    private static BlockDisplay bars;
    private static BlockDisplay borderTop;


    public LightBoxListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemStack itemOffHand = player.getInventory().getItemInOffHand();
        Damageable itemInHandDMGMeta = ((Damageable) itemInHand.getItemMeta());

        if (event.getAction() == Action.RIGHT_CLICK_AIR && itemInHand.hasItemMeta() && itemInHand.getItemMeta().hasLore() &&
                itemInHand.getItemMeta().getLore().contains("Wear it on your head") && itemInHandDMGMeta.getDamage() != 300) {

            if(wearingBeamBox) {
                removeBeamBox();
                return;

            } else {
                wearBeamBox(player, itemInHand, itemInHandDMGMeta);
                wearingBeamBox = true;

            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR && itemInHand.hasItemMeta() && itemInHand.getItemMeta().hasLore() &&
                itemInHand.getItemMeta().getLore().contains("Wear it on your head") && itemInHandDMGMeta.getDamage() != 0 &&
                itemOffHand.getType() == Material.COAL) {

            itemInHandDMGMeta.setDamage(itemInHandDMGMeta.getDamage() - 10);
            itemInHand.setItemMeta(itemInHandDMGMeta);
            itemOffHand.setAmount(itemOffHand.getAmount()-1);
        }

    }



    public void wearBeamBox(Player player, ItemStack BeamBoxItem, Damageable BeamBoxDMGMeta) {


        this.world = player.getWorld();

        box = world.spawn(player.getLocation(), BlockDisplay.class);
        light = world.spawn(player.getLocation(), BlockDisplay.class);
        bars = world.spawn(player.getLocation(), BlockDisplay.class);
        borderTop = world.spawn(player.getLocation(), BlockDisplay.class);

        borderTop.setBlock(Bukkit.createBlockData(Material.LIGHT_WEIGHTED_PRESSURE_PLATE));
        borderTop.setTransformation(new Transformation(
                new Vector3f(0, 0, 0),
                new Quaternionf(),
                new Vector3f(0.82f, 0.875f, 0.45f),
                new Quaternionf()
        ));

        bars.setBlock(Bukkit.createBlockData(Material.COPPER_GRATE));
        bars.setTransformation(new Transformation(
                new Vector3f(0, 0, 0),
                new Quaternionf(),
                new Vector3f(0.7f, 0.7f, 0.7f),
                new Quaternionf()
        ));
        bars.setBillboard(Display.Billboard.FIXED);

        light.setBlock(Bukkit.createBlockData(Material.OCHRE_FROGLIGHT));
        light.setTransformation(new Transformation(
                new Vector3f(0, 0, 0),
                new Quaternionf(),
                new Vector3f(0.65f, 0.65f, 0.65f),
                new Quaternionf()
        ));

        light.setBillboard(Display.Billboard.FIXED);

        box.setBrightness(new Display.Brightness(15, 15));
        box.setBlock(Bukkit.createBlockData(Material.RED_CONCRETE));
        box.setBillboard(Display.Billboard.FIXED);

        lightTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || player.isDead()) {
                    box.remove();
                    cancel();
                    return;
                }

                // Head positioning
                Location playerLocation = player.getLocation().clone();
                float yawDegrees = playerLocation.getYaw();
                float yawRadians = (float) Math.toRadians(-yawDegrees); // negative because of your original code

// zero out pitch, so the location is flat on the horizontal plane
                playerLocation.setPitch(0);
                playerLocation.setYaw(yawDegrees);

                Vector offset = new Vector(-0.55, 0, -0.7);
                Vector offset2 = new Vector(-0.35, 0.12, -0.2);
                Vector offset3 = new Vector(-0.375, 0.108, -0.2);
                Vector offset4 = new Vector(-0.425, 0.8,0.25);


                offset.rotateAroundY(yawRadians);
                offset2.rotateAroundY(yawRadians);
                offset3.rotateAroundY(yawRadians);
                offset4.rotateAroundY(yawRadians);

                Location headCenter = playerLocation.add(0, 1.25, 0);

                box.teleport(headCenter.clone().add(offset));
                light.teleport(headCenter.clone().add(offset2));
                bars.teleport(headCenter.clone().add(offset3));
                borderTop.teleport(headCenter.clone().add(offset4));

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

    public static void removeBeamBox() {

        wearingBeamBox = false;
        if(particleTask != null) particleTask.cancel();
        if(lightTask != null) lightTask.cancel();
        if(box != null && light != null && bars != null && borderTop != null) {
            box.remove();
            light.remove();
            bars.remove();
            borderTop.remove();
        }
    }

}
