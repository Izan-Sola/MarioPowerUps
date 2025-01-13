package me.ShinyShadow_.MarioPowerUps;

import me.ShinyShadow_.MarioPowerUps.Abilities.FireFlowerListener;
import me.ShinyShadow_.MarioPowerUps.Commands.Commands;
import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class PowerUps extends JavaPlugin {

	
	public void onEnable() {
		
		ItemManager.init();
		getCommand("givefireflower").setExecutor(new Commands());

		Bukkit.getPluginManager().registerEvents(new FireFlowerListener(this), this);
		
	//	Bukkit.getPluginManager().registerEvents(new ItemListener(this), this);
		
//		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "PowerUps"), ItemManager.Fire_Flower);
//		recipe.shape("MMM", "BEB", "MMM");
//		recipe.setIngredient('M', Material.PHANTOM_MEMBRANE);
//		recipe.setIngredient('B', Material.BLAZE_POWDER);
//		recipe.setIngredient('E', Material.ENDER_EYE);
//
//		Bukkit.addRecipe(recipe);
	}
	
	public void onDisable() {
		
	}
}
