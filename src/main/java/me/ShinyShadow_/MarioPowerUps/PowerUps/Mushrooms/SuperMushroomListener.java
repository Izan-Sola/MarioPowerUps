package me.ShinyShadow_.MarioPowerUps.PowerUps.Mushrooms;

import com.comphenix.protocol.ProtocolManager;
import me.ShinyShadow_.MarioPowerUps.Stuff.PowerupsUtil;
import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
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

public class SuperMushroomListener implements Listener {

    private ProtocolManager protocolManager;
    private JavaPlugin plugin;
    private Player player;
    private Boolean SuperMixReady = false;
    List<Material> SuperMixIngredients = Arrays.asList(
            Material.SHORT_GRASS,
            Material.BONE_MEAL,
            Material.POTION
    );
    List<Material> MushroomIngredient = Arrays.asList(
            Material.RED_MUSHROOM
    );
    BukkitTask SuperMixTask;
    Collection<Entity> itemsInCauldron;
    public SuperMushroomListener(JavaPlugin plugin) {
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
        if (eatenItem.getItemMeta().getLore().contains("One Super Mushroom a day, ") ) {

                player.setHealth(player.getMaxHealth());
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 10000, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 10000, 0));
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.removePotionEffect(PotionEffectType.RESISTANCE);
                    player.removePotionEffect(PotionEffectType.STRENGTH);
                }, 6000);
            }
        }



    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        ItemStack inHandItem = player.getInventory().getItemInMainHand();
        if( itemsInCauldron != null ) itemsInCauldron.clear();



        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock != null && clickedBlock.getType() == Material.WATER_CAULDRON) {
            itemsInCauldron = clickedBlock.getWorld().getNearbyEntities(clickedBlock.getLocation(), 1, 1, 1);
            itemsInCauldronList.clear();

            Boolean checkIngredients = PowerupsUtil.checkIngredients(itemsInCauldron, itemsInCauldronList, SuperMixIngredients, MushroomIngredient);

            if (checkIngredients) {
                SuperMix(clickedBlock);
            }
            if (itemsInCauldronList.contains(Material.RED_MUSHROOM)  && SuperMixReady && event.getHand() == EquipmentSlot.HAND ) {
                itemsInCauldron = clickedBlock.getWorld().getNearbyEntities(clickedBlock.getLocation(), 1, 1, 1);
                player.sendMessage("HERE3");
                player.playSound(clickedBlock.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0.5f);
                Entity UpMushroom = clickedBlock.getWorld().dropItem(clickedBlock.getLocation(), ItemManager.Super_Mushroom);
                UpMushroom.setVelocity(new Vector(0, 0.45, 0));
                SuperMixReady = false;

            }

        }
    }
    public void SuperMix(Block clickedBlock) {
        Block cauldron = clickedBlock;
        SuperMixReady = true;

        SuperMixTask = new BukkitRunnable() {

            @Override
            public void run() {
                if(!SuperMixReady) cancel();
                cauldron.getWorld().spawnParticle(Particle.DUST, cauldron.getLocation().add(0.5, 0.6, 0.5), 20, 0.18, 0.2, 0.18, 0.1,
                        new Particle.DustOptions(Color.RED, 1));
                cauldron.getWorld().spawnParticle(Particle.CRIT,cauldron.getLocation().add(0.5, 0.6, 0.5), 8, 0.18, 0.2, 0.18, 0.1);
                cauldron.getWorld().spawnParticle(Particle.BUBBLE_POP,  cauldron.getLocation().add(0.5, 1, 0.5), 6, 0.2, 0.2, 0.2, 0);
                cauldron.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,  cauldron.getLocation().add(0.5, 0.6, 0.5), 2, 0.1, 0.2, 0.1, 0);

            }
        }.runTaskTimer(plugin, 0L, 2L);
    }
}


