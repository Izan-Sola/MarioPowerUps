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

//Either hard to obtain permanent uses or easy to obtain but it is consumed/destroyed or needs to be recharged.
//If it has permanent uses i will make a craftable custom item  (mario related too) for the recipe to make it harder to craft and obtain
//If it needs to be recharged, it will also need a custom item for recharge. Maybe after 3 uses of the item it runs out of power and u need to recharge it.




		//ignore this recipe
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
