package me.ShinyShadow_.MarioPowerUps.Abilities;

import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

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
    private boolean isRefined = false;
    private String cauldronState = "none";
    private BukkitTask extractBrewingEffect;
    private BukkitTask extractBrewingSound;

    private final List<Color> redPallete = Arrays.asList(
            Color.fromRGB(255, 0, 0),
            Color.fromRGB(220, 20, 60),
            Color.fromRGB(178, 34, 34),
            Color.fromRGB(255, 69, 0),
            Color.fromRGB(255, 99, 71),
            Color.fromRGB(204, 0, 0),
            Color.fromRGB(139, 0, 0),
            Color.fromRGB(165, 42, 42),
            Color.fromRGB(233, 150, 122),
            Color.fromRGB(255, 105, 97)
    );

    List<Material> CrimsonExtractMaterials = Arrays.asList(
            Material.REDSTONE,
            Material.INK_SAC,
            Material.GLOWSTONE_DUST
    );

    public RedStarListener(JavaPlugin plugin) {
    this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR && event.getHand() == EquipmentSlot.HAND && itemInHand.isSimilar(ItemManager.Red_Star)
                && player.getCooldown(ItemManager.Red_Star) <= 0) {
            if (isGliding) {
                flightTask.cancel();
                isGliding = false;
            } else {
                flight(player, plugin);

                if (!timerRunning) {
                    BossBar powerUpBar = Bukkit.createBossBar("Red Star Duration", BarColor.RED, BarStyle.SEGMENTED_6);
                    powerUpBar.setProgress(1);
                    powerUpBar.addPlayer(player);

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            powerUpBar.setProgress(powerUpBar.getProgress() - 0.00041665);

                            if (powerUpBar.getProgress() <= 0.001) {
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
        if (event.getAction() == Action.LEFT_CLICK_AIR && itemInHand.isSimilar(ItemManager.Red_Star) && boostReady
                && player.getCooldown(ItemManager.Red_Star) <= 0) {

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

        Block cauldron = event.getClickedBlock();
        if (cauldron != null && cauldron.getType() == Material.WATER_CAULDRON) {

            Set<Material> itemsInCaudronList = new HashSet<>();
            Collection<Entity> itemsInCauldron = cauldron.getWorld().getNearbyEntities(cauldron.getLocation(), 1, 1, 1);
            for (Entity item : itemsInCauldron) {

                if(item instanceof Item) {
                Material itemType = ((Item) item).getItemStack().getType();
                if (cauldronState.equals("refinedExtractBrewing") && itemType == Material.NETHER_STAR && event.getHand() != EquipmentSlot.HAND) {
                    player.sendMessage("WOWERSLOLERS");
                    new BukkitRunnable() {
                        int pulseCount = 0;

                        @Override
                        public void run() {
                            cauldron.getWorld().playSound(cauldron.getLocation(), Sound.ENTITY_BREEZE_JUMP, 1.5f, 0.6f);
                            cauldron.getLocation().getWorld().spawnParticle(Particle.ELECTRIC_SPARK, cauldron.getLocation().add(0.5, 0.75, 0.5), 25, 0.5, 0.5, 0.5, 0.6);
                            cauldron.getLocation().getWorld().spawnParticle(Particle.SMOKE, cauldron.getLocation().add(0.5, 0.75, 0.5), 20, 0.5, 0.5, 0.5, 0.6);
                            for (Color color : redPallete) {
                                cauldron.getLocation().getWorld().spawnParticle(Particle.DUST, cauldron.getLocation().add(0.5, 0.95, 0.5), 35, 0.5, 0.5, 0.5, 1.5,
                                        new Particle.DustOptions(color, 1));
                            }
                            pulseCount += 1;

                            if (pulseCount == 3) {
                                cauldron.getWorld().playSound(cauldron.getLocation(), Sound.ENTITY_BREEZE_JUMP, 2.5f, 1.1f);
                                cauldron.getWorld().playSound(cauldron.getLocation(), Sound.ENTITY_BREEZE_SHOOT, 1.5f, 0.7f);
                                cauldron.getLocation().getWorld().spawnParticle(Particle.ELECTRIC_SPARK, cauldron.getLocation().add(0.5, 0.75, 0.5), 40, 0.5, 0.5, 0.5, 0.6);
                                cauldron.getLocation().getWorld().spawnParticle(Particle.SMOKE, cauldron.getLocation().add(0.5, 0.75, 0.5), 80, 0.5, 0.5, 0.5, 1);
                                cauldron.getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, cauldron.getLocation().add(0.5, 0.75, 0.5), 40, 0.5, 0.5, 0.5, 0.6);
                                for (Color color : redPallete) {
                                    cauldron.getLocation().getWorld().spawnParticle(Particle.DUST, cauldron.getLocation().add(0.5, 0.95, 0.5), 50, 0.5, 0.5, 0.5, 1.5,
                                            new Particle.DustOptions(color, 1));
                                }
                                item.remove();
                                Entity rs = cauldron.getWorld().dropItem(cauldron.getLocation().add(0.5, 0.5, 0.5), ItemManager.Red_Star);
                                rs.setVelocity(new Vector(0, 0.75, 0));

                                pulseCount = 0;
                                extractBrewingEffect.cancel();
                                extractBrewingSound.cancel();
                                cauldronState = "none";
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 0L, 25L);
                }

                    if (CrimsonExtractMaterials.contains(itemType)) {
                        itemsInCaudronList.add(itemType);
                    }

                    if (((Item) item).getItemStack().isSimilar(ItemManager.RefinedCrimson_Extract)) {
                        extractBrewing(player, plugin, cauldron);
                        cauldronState = "refinedExtractBrewing";
                        item.remove();
                    }

                    if (itemsInCaudronList.containsAll(CrimsonExtractMaterials)) {
                        for (Entity i : itemsInCauldron) {
                            i.remove();
                        }

                        itemsInCaudronList.clear();
                        extractBrewing(player, plugin, cauldron);
                        cauldronState = "extractBrewing";

                    }
                }
            }

            if(cauldronState.equals("extractBrewing") && itemInHand.getType() == Material.BOWL) {
                  itemInHand.setAmount(0);
                  player.getInventory().addItem(ItemManager.Crimson_Extract);
                  extractBrewingSound.cancel();
                  extractBrewingEffect.cancel();
                  cauldronState = "none";
            }

        }
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();

        if(event.getAction() == Action.RIGHT_CLICK_AIR && itemInOffHand != null && itemInHand != null  &&
           itemInHand.isSimilar(ItemManager.Crimson_Extract) && itemInOffHand.getType() == Material.BLAZE_ROD ) {

            itemInOffHand.setAmount(itemInOffHand.getAmount()-1);
            itemInHand.setAmount(itemInHand.getAmount()-1);

            player.getInventory().addItem(ItemManager.RefinedCrimson_Extract);
        }


}

            public void extractBrewing(Player player, JavaPlugin plugin, Block cauldron) {

                extractBrewingEffect = new BukkitRunnable() {
                    Random r = new Random();

                    @Override
                    public void run() {
                        int randomColor = r.nextInt(6);

                        cauldron.getWorld().spawnParticle(Particle.DUST, cauldron.getLocation().add(0.5, 0.7, 0.5), 60, 0.22, 0.2, 0.22, 0.12,
                                new Particle.DustOptions(redPallete.get(randomColor), 1));
                        cauldron.getWorld().spawnParticle(Particle.BUBBLE_POP,  cauldron.getLocation().add(0.5, 1, 0.5), 8, 0.2, 0.2, 0.2, 0);
                        cauldron.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,  cauldron.getLocation().add(0.5, 0.6, 0.5), 2, 0.1, 0.2, 0.1, 0);

                        if (cauldronState.equals("refinedExtractBrewing")) {

                            cauldron.getWorld().spawnParticle(Particle.ELECTRIC_SPARK,  cauldron.getLocation().add(0.5, 0.75, 0.5), 8, 0.3, 0.3, 0.3, 0.2);
                            cauldron.getWorld().spawnParticle(Particle.EFFECT,  cauldron.getLocation().add(0.5, 0.8, 0.5), 3, 0.1, 0.1, 0.1, 0.15);
                            cauldron.getWorld().spawnParticle(Particle.DUST, cauldron.getLocation().add(0.5, 0.95, 0.5), 5, 0.3, 0.4, 0.3, 0.8,
                                    new Particle.DustOptions(redPallete.get(randomColor), 1));
                        }

                        if(!(cauldron.getLocation().getBlock().getType() == Material.WATER_CAULDRON)) {
                            extractBrewingSound.cancel();
                            cauldronState = "none";
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0L, 2L);

                extractBrewingSound = new BukkitRunnable() {
                    @Override
                    public void run() {
                        cauldron.getWorld().playSound(cauldron.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 0.75f, 0.15F);
                        cauldron.getWorld().playSound(cauldron.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 0.25f, 0.12f);
                    }
                }.runTaskTimer(plugin, 0L, 15L);

            }

            public void flight (Player player, JavaPlugin plugin) {

                isGliding = true;

                flightTask = new BukkitRunnable() {
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

                        if (!boost) {
                            player.setVelocity(direction.multiply(0.07));
                        } else {
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
                                player.spawnParticle(Particle.DUST, particleLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(255, 70, 70), 1));
                            }
                        }

                        if (location.add(0, -1, 0).getBlock().getType() != Material.AIR) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0L, 1L);
            }

    }