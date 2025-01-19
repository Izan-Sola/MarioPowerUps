package me.ShinyShadow_.MarioPowerUps.Abilities;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class RockMushroomListener implements Listener {

    private final JavaPlugin plugin;
    private int lastBlock = 8;
    public static boolean isRockMushRoomPowerActive = false;
    private Location endpoint;
    private Vector direction;
    private List<Block> lineOfSight;
    private double coveredDistance;
    private double chargeTime = 0D;
    private Location currentLocation;
    private double phi = 0;
    private ProtocolManager protocolManager;
    Set<Material> ignoredBlocks = Set.of(Material.AIR, Material.GRASS_BLOCK, Material.TALL_GRASS);
    public RockMushroomListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSneakToggle(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        return;
    }

//    @EventHandler
//    public void onExplosionPrime(ExplosionPrimeEvent event) {
//        if (event.getEntity() instanceof TNTPrimed) {
//            event.setRadius(2F);
//        }
//    }
    @EventHandler
    public void onEat(FoodLevelChangeEvent event) {

        Player player = (Player) event.getEntity();

        ItemStack eatenItem = event.getItem();
        if(eatenItem == null) {
            return;
        }
        if (eatenItem.getItemMeta().getLore() == null) {
            return;
        }

        List<String> lore = eatenItem.getItemMeta().getLore();
        if (!isRockMushRoomPowerActive ) {

            protocolManager = ProtocolLibrary.getProtocolManager();

            player.sendMessage("POWER UP ACTIVE FOR 30s");
            player.sendMessage("CRACK CRACK GULP");
            isRockMushRoomPowerActive = true;
            new RockMushroomSphere(player, plugin, protocolManager);

        }

    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(player.getFoodLevel() != 20) {
            return;
        }
        else if (player.getInventory().getItemInMainHand().getItemMeta() != null &&
                 player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Rock Mushroom")) {
            player.setFoodLevel(19);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.setFoodLevel(20), 1L);
        }
    }
}
