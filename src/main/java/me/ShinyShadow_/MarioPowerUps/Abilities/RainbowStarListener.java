package me.ShinyShadow_.MarioPowerUps.Abilities;

import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.*;

public class RainbowStarListener implements Listener {

    private JavaPlugin plugin;
    List<Material> RainbowEssenceMaterials = Arrays.asList(
            Material.RED_DYE,
            Material.BLUE_DYE,
            Material.YELLOW_DYE,
            Material.GREEN_DYE,
            Material.PURPLE_DYE,
            Material.GLOWSTONE_DUST
    );
    Random r = new Random();
  private final List<Color> rainbowColors = Arrays.asList(
          Color.fromRGB(255, 0, 0),
          Color.fromRGB(255, 127, 0),
          Color.fromRGB(255, 255, 0),
          Color.fromRGB(0, 255, 0),
          Color.fromRGB(0, 0, 255),
          Color.fromRGB(75, 0, 130),
          Color.fromRGB(148, 0, 211)
  );
    private final List<ChatColor> glowRainbowColors = Arrays.asList(
            ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW,
            ChatColor.GREEN, ChatColor.AQUA, ChatColor.BLUE,
            ChatColor.LIGHT_PURPLE
    );

    private String cauldronState = "none";
    Set<Material> itemsInCaudronList = new HashSet<>();
    private BukkitTask EssenceMix;
    private BukkitTask EssenceBrewing;
    private int testCount = 0;
    private ItemStack itemInHand;
    private ItemStack itemInOffHand;
    private BukkitTask glow;
    Scoreboard scoreboard;
    Team team;

public RainbowStarListener(JavaPlugin plugin) {

    this.plugin = plugin;
}
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        itemInHand = player.getInventory().getItemInMainHand();
        itemInOffHand = player.getInventory().getItemInOffHand();

        if (itemInHand.isSimilar(ItemManager.Rainbow_Essence) && itemInOffHand.getType() == Material.PHANTOM_MEMBRANE) {
            itemInHand.setAmount(0);
            itemInOffHand.setAmount(0);
            player.spawnParticle(Particle.ELECTRIC_SPARK, player.getLocation().add(0., 0.8, 0), 45, 0.4, 0.2, 0.4, 0.1);
            player.getInventory().addItem(ItemManager.RefinedRainbow_Essence);
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

                Block clickedBlock = event.getClickedBlock();

                if (clickedBlock.getType() == Material.WATER_CAULDRON) {

                    Collection<Entity> itemsInCauldron = clickedBlock.getWorld().getNearbyEntities(clickedBlock.getLocation(), 1, 1, 1);
                    itemsInCaudronList.removeAll(RainbowEssenceMaterials);
                    for (Entity item : itemsInCauldron) {

                        if (item instanceof Item) {

                            if (((Item) item).getItemStack().isSimilar(ItemManager.RefinedRainbow_Essence)) {
                                clickedBlock.getWorld().playSound(clickedBlock.getLocation(), Sound.ENTITY_BREEZE_JUMP, 0.6f, 0.6f);
                                RainbowEssenceMix(clickedBlock, true);
                                item.remove();
                            }

                            Material itemType = ((Item) item).getItemStack().getType();

                            if (cauldronState.equals("refinedrainbow") && itemType == Material.NETHER_STAR && event.getHand() != EquipmentSlot.HAND) {

                                new BukkitRunnable() {
                                    int pulseCount = 0;

                                    @Override
                                    public void run() {
                                        clickedBlock.getWorld().playSound(clickedBlock.getLocation(), Sound.ENTITY_BREEZE_JUMP, 1.5f, 1.3f);
                                        clickedBlock.getLocation().getWorld().spawnParticle(Particle.ELECTRIC_SPARK, clickedBlock.getLocation().add(0.5, 0.75, 0.5), 25, 0.5, 0.5, 0.5, 0.6);
                                        clickedBlock.getLocation().getWorld().spawnParticle(Particle.SMOKE, clickedBlock.getLocation().add(0.5, 0.75, 0.5), 20, 0.5, 0.5, 0.5, 0.6);
                                        for (Color color : rainbowColors) {
                                            clickedBlock.getLocation().getWorld().spawnParticle(Particle.DUST, clickedBlock.getLocation().add(0.5, 0.95, 0.5), 35, 0.5, 0.5, 0.5, 1.5,
                                                    new Particle.DustOptions(color, 1));
                                        }
                                        pulseCount += 1;

                                        if (pulseCount == 3) {
                                            clickedBlock.getWorld().playSound(clickedBlock.getLocation(), Sound.ENTITY_BREEZE_JUMP, 2.5f, 1.8f);
                                            clickedBlock.getWorld().playSound(clickedBlock.getLocation(), Sound.ENTITY_BREEZE_SHOOT, 1.5f, 1.5f);
                                            clickedBlock.getLocation().getWorld().spawnParticle(Particle.ELECTRIC_SPARK, clickedBlock.getLocation().add(0.5, 0.75, 0.5), 40, 0.5, 0.5, 0.5, 0.6);
                                            clickedBlock.getLocation().getWorld().spawnParticle(Particle.SMOKE, clickedBlock.getLocation().add(0.5, 0.75, 0.5), 80, 0.5, 0.5, 0.5, 1);
                                            clickedBlock.getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, clickedBlock.getLocation().add(0.5, 0.75, 0.5), 40, 0.5, 0.5, 0.5, 0.6);
                                            for (Color color : rainbowColors) {
                                                clickedBlock.getLocation().getWorld().spawnParticle(Particle.DUST, clickedBlock.getLocation().add(0.5, 0.95, 0.5), 50, 0.5, 0.5, 0.5, 1.5,
                                                        new Particle.DustOptions(color, 1));
                                            }
                                            item.remove();
                                            Entity rs = clickedBlock.getWorld().dropItem(clickedBlock.getLocation().add(0.5, 0.5, 0.5), ItemManager.Rainbow_Star);
                                            rs.setVelocity(new Vector(0, 0.75, 0));

                                            pulseCount = 0;
                                            EssenceMix.cancel();
                                            EssenceBrewing.cancel();
                                            cauldronState = "none";
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer(plugin, 0L, 25L);
                            }
                            if (RainbowEssenceMaterials.contains(itemType)) {
                                itemsInCaudronList.add(itemType);
                            }
                            if (RainbowEssenceMaterials.containsAll(itemsInCaudronList) && itemsInCaudronList.size() == RainbowEssenceMaterials.size()) {
                                RainbowEssenceMix(clickedBlock, false);
                                cauldronState = "rainbow";

                                for (Entity i : itemsInCauldron) {
                                    i.remove();
                                }
                                itemsInCaudronList.removeAll(RainbowEssenceMaterials);
                            }
                        }
                    }

                    if (cauldronState.equals("rainbow") && player.getInventory().getItemInMainHand().getType() == Material.GLASS_BOTTLE) {
                        player.getInventory().getItemInMainHand().setAmount(0);
                        player.getInventory().addItem(ItemManager.Rainbow_Essence);
                        EssenceMix.cancel();
                        EssenceBrewing.cancel();
                        cauldronState = "none";
                    }
                }
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            itemInHand = player.getInventory().getItemInMainHand();
            itemInOffHand = player.getInventory().getItemInOffHand();


            Damageable onHandDMGMeta = (Damageable) itemInHand.getItemMeta();
            Damageable rMeta = (Damageable) itemInHand.getItemMeta();

            if(itemInHand.getItemMeta() != null && itemInHand.getItemMeta().getLore() != null && itemInHand.getItemMeta().getLore().contains("Immense and colorful energy is") && player.getCooldown(ItemManager.Rainbow_Star) <= 0) {
                if ( onHandDMGMeta.getDamage() != 1) {

                    player.spawnParticle(Particle.ELECTRIC_SPARK, player.getLocation().add(0., 0.8, 0), 80, 0.6, 0.6, 0.6, 0);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BREEZE_JUMP, 2f, 1.4f);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BREEZE_SHOOT, 2f, 1.2f);
                    BossBar powerUpBar = Bukkit.createBossBar("RainbowStar Duration", BarColor.WHITE, BarStyle.SOLID);
                    powerUpBar.setProgress(1);
                    powerUpBar.addPlayer(player);
                    rainbowGlow(player);
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 1));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 6));
                            int randomColor = r.nextInt(6);

                            player.getLocation().getWorld().spawnParticle(Particle.DUST, player.getLocation().add(0, 0.95, 0), 25, 0.2, 0.5, 0.2, 1.5,
                                    new Particle.DustOptions(rainbowColors.get(randomColor), 1));
                            powerUpBar.setProgress(powerUpBar.getProgress() - 0.00041665);

                            Collection<Entity> nearbyEntities = player.getWorld().getNearbyEntities(player.getLocation(), 1, 1, 1);

                            for (Entity entity : nearbyEntities) {
                                if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
                                    ((LivingEntity) entity).damage(20);
                                }
                            }

                            testCount += 1;
                            if (testCount >= 2400) {
                                powerUpBar.removePlayer(player);
                                team.removeEntry(player.getName());
                                glow.cancel();
                                this.cancel();
                            }
                        }

                    }.runTaskTimer(plugin, 0L, 1L);
                    player.setCooldown(ItemManager.Rainbow_Star, 12000);
                    rMeta.setDamage(rMeta.getDamage() + 1);
                    itemInHand.setItemMeta(rMeta);
                }
            }

            if (itemInHand.getItemMeta() != null && itemInHand.getItemMeta().getLore() != null &&
                    itemInHand.getItemMeta().getLore().contains("Immense and colorful energy is") && itemInOffHand.isSimilar(ItemManager.Rainbow_Essence)) {
                rMeta.setDamage(rMeta.getDamage() - 1);
                itemInHand.setItemMeta(rMeta);
                itemInOffHand.setAmount(0);
            }

        }
    }
    public void rainbowGlow(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        team = scoreboard.getTeam(player.getName());
        if (team == null) {
            team = scoreboard.registerNewTeam(player.getName());
        }
        team.addEntry(player.getName());

        glow = new BukkitRunnable() {
            int currentIndex = 0;

            @Override
            public void run() {
                if (!player.isOnline()) {

                    this.cancel();
                    return;
                }
                ChatColor color = glowRainbowColors.get(currentIndex);
                team.setColor(color);


                currentIndex = (currentIndex + 1) % rainbowColors.size();
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }


    public void RainbowEssenceMix(Block cauldron, Boolean isRefined) {

        EssenceMix = new BukkitRunnable() {

            @Override
            public void run() {

                if(!cauldronState.equals("rainbow") && !isRefined) {
                    this.cancel();
                }

                int randomColor = r.nextInt(6);

                cauldron.getWorld().spawnParticle(Particle.DUST, cauldron.getLocation().add(0.5, 0.7, 0.5), 25, 0.18, 0.2, 0.18, 0.1,
                        new Particle.DustOptions(rainbowColors.get(randomColor), 1));
                cauldron.getWorld().spawnParticle(Particle.BUBBLE_POP,  cauldron.getLocation().add(0.5, 1, 0.5), 6, 0.2, 0.2, 0.2, 0);
                cauldron.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,  cauldron.getLocation().add(0.5, 0.6, 0.5), 2, 0.1, 0.2, 0.1, 0);

                if (isRefined) {
                    cauldronState = "refinedrainbow";
                    cauldron.getWorld().spawnParticle(Particle.ELECTRIC_SPARK,  cauldron.getLocation().add(0.5, 0.75, 0.5), 8, 0.3, 0.3, 0.3, 0.2);
                    cauldron.getWorld().spawnParticle(Particle.EFFECT,  cauldron.getLocation().add(0.5, 0.8, 0.5), 3, 0.1, 0.1, 0.1, 0.15);
                    cauldron.getWorld().spawnParticle(Particle.DUST, cauldron.getLocation().add(0.5, 0.95, 0.5), 5, 0.3, 0.4, 0.3, 0.8,
                            new Particle.DustOptions(rainbowColors.get(randomColor), 1));
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);

        EssenceBrewing = new BukkitRunnable() {
            @Override
            public void run() {
                cauldron.getWorld().playSound(cauldron.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1.0f, 0.3F);
                cauldron.getWorld().playSound(cauldron.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 0.3f, 0.3f);
            }
        }.runTaskTimer(plugin, 0L, 12L);
    }
}
