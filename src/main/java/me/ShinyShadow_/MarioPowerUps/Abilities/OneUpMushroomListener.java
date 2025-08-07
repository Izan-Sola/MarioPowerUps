package me.ShinyShadow_.MarioPowerUps.Abilities;

import com.comphenix.protocol.ProtocolManager;
import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class OneUpMushroomListener implements Listener {

    private ProtocolManager protocolManager;
    private JavaPlugin plugin;
    private Player player;
    private Boolean HerbsMixReady = false;
    List<Material> HerbsMixIngredients = Arrays.asList(
            Material.SHORT_GRASS,
            Material.BONE_MEAL,
            Material.PUMPKIN_SEEDS
    );
    List<Material> MushroomIngredient = Arrays.asList(
            Material.BROWN_MUSHROOM
    );
    BukkitTask HerbsMixTask;
    Collection<Entity> itemsInCauldron;
    public OneUpMushroomListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    Set<Material> itemsInCauldronList = new HashSet<>();

    @EventHandler
    public void onEat(FoodLevelChangeEvent event) {

        Player player = (Player) event.getEntity();

        ItemStack eatenItem = event.getItem();
        if(eatenItem == null || eatenItem.getItemMeta().getLore() == null ) {
            return;
        }
        if (eatenItem.getItemMeta().getLore().contains("A 1Up mushroom a day, ") ) {
            if(player.getMaxHealth() > 20) {
                player.sendMessage("You can only eat this mushroom when under 21 HP!");
            } else {
                player.setMaxHealth(player.getMaxHealth() + 20);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 4));
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.setMaxHealth(player.getMaxHealth() - 20);
                }, 6000);
            }
        }
    //        protocolManager = ProtocolLibrary.getProtocolManager();

    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        ItemStack inHandItem = player.getInventory().getItemInMainHand();
        if( itemsInCauldron != null ) itemsInCauldron.clear();

         if (inHandItem.hasItemMeta() && inHandItem.getItemMeta().hasLore() &&
                 inHandItem.getItemMeta().getLore().contains("A 1Up mushroom a day, ")) {
                player.setFoodLevel(player.getFoodLevel() - 1);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.setFoodLevel(20), 1L);
        }


        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock != null && clickedBlock.getType() == Material.WATER_CAULDRON) {
            itemsInCauldron = clickedBlock.getWorld().getNearbyEntities(clickedBlock.getLocation(), 1, 1, 1);
            itemsInCauldronList.clear();

            for (Entity item : itemsInCauldron) {
                if (item instanceof Item) {
                    Material itemType = ((Item) item).getItemStack().getType();
                    itemsInCauldronList.add(itemType);

                    if(HerbsMixIngredients.contains(itemType) || MushroomIngredient.contains(itemType)) item.remove();
                }
            }
            player.sendMessage("Items in cauldron: " + itemsInCauldronList.toString());
            player.sendMessage("Expected ingredients: " + HerbsMixIngredients.toString());

            if (!itemsInCauldronList.isEmpty() && new HashSet<>(itemsInCauldronList).containsAll(HerbsMixIngredients)) {
                player.sendMessage("erhiujbggewijhwegrjkhet");
                HerbsMix(clickedBlock);
            }
            if (itemsInCauldronList.contains(Material.BROWN_MUSHROOM)  && HerbsMixReady && event.getHand() != EquipmentSlot.HAND ) {
                itemsInCauldron = clickedBlock.getWorld().getNearbyEntities(clickedBlock.getLocation(), 1, 1, 1);

                    player.playSound(clickedBlock.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0.5f);
                    Entity UpMushroom = clickedBlock.getWorld().dropItem(clickedBlock.getLocation(), ItemManager.OneUp_Mushroom);
                    UpMushroom.setVelocity(new Vector(0, 0.45, 0));
                    HerbsMixReady = false;

            }

        }
    }
    public void HerbsMix(Block clickedBlock) {
        Block cauldron = clickedBlock;
        HerbsMixReady = true;

        HerbsMixTask = new BukkitRunnable() {

            @Override
            public void run() {
                if(!HerbsMixReady) cancel();
                cauldron.getWorld().spawnParticle(Particle.DUST, cauldron.getLocation().add(0.5, 0.6, 0.5), 20, 0.18, 0.2, 0.18, 0.1,
                        new Particle.DustOptions(Color.GREEN, 1));
                cauldron.getWorld().spawnParticle(Particle.HAPPY_VILLAGER,cauldron.getLocation().add(0.5, 0.6, 0.5), 8, 0.18, 0.2, 0.18, 0.1);
                cauldron.getWorld().spawnParticle(Particle.BUBBLE_POP,  cauldron.getLocation().add(0.5, 1, 0.5), 6, 0.2, 0.2, 0.2, 0);
                cauldron.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,  cauldron.getLocation().add(0.5, 0.6, 0.5), 2, 0.1, 0.2, 0.1, 0);

            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
}


