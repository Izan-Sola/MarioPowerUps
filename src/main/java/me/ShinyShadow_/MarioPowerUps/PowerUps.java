package me.ShinyShadow_.MarioPowerUps;

import me.ShinyShadow_.MarioPowerUps.Abilities.FireFlowerListener;
import me.ShinyShadow_.MarioPowerUps.Commands.Commands;
import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;


public final class PowerUps extends JavaPlugin {

	
	public void onEnable() {

		ItemManager.init();
		getCommand("givefireflower").setExecutor(new Commands());

		Bukkit.getPluginManager().registerEvents(new FireFlowerListener(this), this);

		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "PowerUps"), ItemManager.Fire_Flower);
		recipe.shape("FFF", "FTF", "BMB");
		recipe.setIngredient('F', Material.FIRE_CHARGE);
		recipe.setIngredient('B', Material.BLAZE_ROD);
		recipe.setIngredient('T', Material.TORCHFLOWER);
		recipe.setIngredient('M', Material.MAGMA_BLOCK);

     	Bukkit.addRecipe(recipe);
	}
	
	public void onDisable() {
		
	}
}
