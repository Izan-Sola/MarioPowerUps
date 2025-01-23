package me.ShinyShadow_.MarioPowerUps.Abilities;

import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class CustomItemRecipeListener implements Listener {
    List<Material> RainbowEssenceMaterials = Arrays.asList(
            Material.RED_DYE,
            Material.BLUE_DYE,
            Material.YELLOW_DYE,
            Material.GREEN_DYE,
            Material.PURPLE_DYE,
            Material.GLOWSTONE_DUST
    );


    ItemStack[] cloudflowermatrix = new ItemStack[9];

    private final List<Color> rainbowColors = Arrays.asList(Color.RED, Color.YELLOW, Color.ORANGE, Color.PURPLE, Color.BLUE, Color.GREEN);
    boolean hasCloudBucket = false;
    private String cauldronState = "none";
    Set<Material> itemsInCaudronList = new HashSet<>();
    private int correctItemCount = 0;
    private final JavaPlugin plugin;
    private int airBottlesCount = 0;
    private BukkitTask EssenceMix;
    private BukkitTask EssenceBrewing;
    public CustomItemRecipeListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
        @EventHandler
        public void onPrepareCraft(PrepareItemCraftEvent event) {
            CraftingInventory inventory = event.getInventory();
            hasCloudBucket = false;
            correctItemCount = 0;
            //i fucking hate THIS
            ItemStack[] matrix = inventory.getMatrix();
            cloudflowermatrix[0] = new ItemStack(Material.GLASS_BOTTLE);
            cloudflowermatrix[1] = new ItemStack(Material.GLASS_BOTTLE);
            cloudflowermatrix[2] = new ItemStack(Material.GLASS_BOTTLE);
            cloudflowermatrix[3] = new ItemStack(Material.FEATHER);
            cloudflowermatrix[4] = new ItemStack(Material.OXEYE_DAISY);
            cloudflowermatrix[5] = new ItemStack(Material.FEATHER);
            cloudflowermatrix[6] = new ItemStack(Material.FEATHER);
            cloudflowermatrix[7] = new ItemStack(Material.BUCKET);
            cloudflowermatrix[8] = new ItemStack(Material.FEATHER);

            airBottlesCount = 0;

            boolean matches = compareMatrices(matrix, cloudflowermatrix);

            if (matches) {
                if(!hasCloudBucket || airBottlesCount != 3) {
                    inventory.setResult(null);
                }
            }
                }


    private boolean compareMatrices(ItemStack[] matrix, ItemStack[] customMatrix) {
        for (int i = 0; i < customMatrix.length; i++) {
            ItemStack slot = matrix[i];
            ItemStack expected = customMatrix[i];

            if (slot == null && expected == null) {
                continue;
            }
            if (slot != null && slot.isSimilar(ItemManager.Air_Bottle)) {
                airBottlesCount +=1;
            }
            if(slot != null && slot.isSimilar(ItemManager.Cloud_Bucket)){
                hasCloudBucket = true;
            }

            if (slot == null || expected == null || !slot.isSimilar(expected)) {
                return false;
            }
        }
        return true;
    }
        @EventHandler
        public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Block clickedBlock = event.getClickedBlock();
            if(clickedBlock.getType() != null && clickedBlock.getType() == Material.WATER_CAULDRON) {


                Collection<Entity> itemsInCauldron = clickedBlock.getWorld().getNearbyEntities(clickedBlock.getLocation(), 1, 1, 1);
                itemsInCaudronList.removeAll(RainbowEssenceMaterials);
                for (Entity item : itemsInCauldron) {

                    if (item instanceof Item) {

                        if(((Item) item).getItemStack().isSimilar(ItemManager.RefinedRainbow_Essence)) {
                            clickedBlock.getWorld().playSound(clickedBlock.getLocation(), Sound.ENTITY_BREEZE_JUMP, 0.6f, 0.6f);
                            RainbowEssenceMix(clickedBlock, true);
                            item.remove();
                        }

                        Material itemType = ((Item) item).getItemStack().getType();

                        if(cauldronState.equals("refinedrainbow") && itemType == Material.NETHER_STAR) {


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
                                        player.sendMessage("GIVERAINBOWSTAR");
                                        EssenceMix.cancel();
                                        EssenceBrewing.cancel();
                                        cauldronState = "none";
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(plugin, 0L, 25L);



                        }
                        if(RainbowEssenceMaterials.contains(itemType)) {
                            itemsInCaudronList.add(itemType);
                        }
                        if(RainbowEssenceMaterials.containsAll(itemsInCaudronList) && itemsInCaudronList.size() == RainbowEssenceMaterials.size()) {
                            RainbowEssenceMix(clickedBlock, false);
                            cauldronState = "rainbow";
                            itemsInCaudronList.removeAll(RainbowEssenceMaterials);

                            for (Entity i : itemsInCauldron) {
                                i.remove();
                            }
                        }
                    }
                }

                if(cauldronState.equals("rainbow") && player.getInventory().getItemInMainHand().getType() == Material.GLASS_BOTTLE) {
                    player.getInventory().getItemInMainHand().setAmount(0);
                    player.getInventory().addItem(ItemManager.Rainbow_Essence);
                    player.sendMessage("GIVERAINBOWESSENCE");
                    cauldronState = "none";
                }
            }
        }
    }


    public void RainbowEssenceMix(Block cauldron, Boolean isRefined) {

   // List<Color> rainbowColors = Arrays.asList(Color.RED, Color.YELLOW, Color.ORANGE, Color.PURPLE, Color.BLUE, Color.GREEN);
        Random r = new Random();
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
                // Play the brewing sound repeatedly
                cauldron.getWorld().playSound(cauldron.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1.0f, 0.3F);
                cauldron.getWorld().playSound(cauldron.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 0.3f, 0.3f);
            }
        }.runTaskTimer(plugin, 0L, 12L);
    }
}

