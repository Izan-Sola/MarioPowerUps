package me.ShinyShadow_.MarioPowerUps.Abilities;

import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomItemRecipeListener implements Listener {

    ItemStack[] cloudflowermatrix = new ItemStack[9];
    boolean hasCloudBucket = false;

    private final JavaPlugin plugin;
    private int airBottlesCount = 0;


    public CustomItemRecipeListener(JavaPlugin plugin) {
        this.plugin = plugin;
        cloudflowermatrix[0] = new ItemStack(Material.GLASS_BOTTLE);
        cloudflowermatrix[1] = new ItemStack(Material.GLASS_BOTTLE);
        cloudflowermatrix[2] = new ItemStack(Material.GLASS_BOTTLE);
        cloudflowermatrix[3] = new ItemStack(Material.FEATHER);
        cloudflowermatrix[4] = new ItemStack(Material.OXEYE_DAISY);
        cloudflowermatrix[5] = new ItemStack(Material.FEATHER);
        cloudflowermatrix[6] = new ItemStack(Material.FEATHER);
        cloudflowermatrix[7] = new ItemStack(Material.BUCKET);
        cloudflowermatrix[8] = new ItemStack(Material.FEATHER);


    }
        @EventHandler
        public void onPrepareCraft(PrepareItemCraftEvent event) {
            CraftingInventory inventory = event.getInventory();
            hasCloudBucket = false;
            ItemStack[] matrix = inventory.getMatrix();


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
            else if(slot != null && slot.isSimilar(ItemManager.Cloud_Bucket)){
                hasCloudBucket = true;
            }
            else if (slot == null || expected == null || !slot.isSimilar(expected)) {
                return false;
            }
        }
        return true;
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if(event.getAction() == Action.RIGHT_CLICK_AIR) {

            if ( itemInHand.getType() == Material.BUCKET && player.getLocation().getY() >= 190 ) {
                itemInHand.setAmount(0);
                player.getInventory().addItem(ItemManager.Cloud_Bucket);
            }
            else if ( itemInHand.getType() == Material.GLASS_BOTTLE && !itemInHand.isSimilar(ItemManager.Cloud_Flower) && player.getLocation().getY() >= 20 && !itemInHand.isSimilar(ItemManager.Cloud_Flower)) {
                itemInHand.setAmount(0);
                player.getInventory().addItem(ItemManager.Air_Bottle);
            }
        }
    }
}

