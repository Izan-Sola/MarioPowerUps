package me.ShinyShadow_.MarioPowerUps;

import me.ShinyShadow_.MarioPowerUps.Abilities.FireFlowerListener;
import me.ShinyShadow_.MarioPowerUps.Abilities.RockMushroomListener;
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
		getCommand("giverockmushroom").setExecutor(new Commands());

		Bukkit.getPluginManager().registerEvents(new FireFlowerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new RockMushroomListener(this), this);

		ShapedRecipe FireFlowerRecipe = new ShapedRecipe(new NamespacedKey(this, "fireflowerrecipe"), ItemManager.Fire_Flower);
		FireFlowerRecipe.shape("FFF", "FTF", "BMB");
		FireFlowerRecipe.setIngredient('F', Material.FIRE_CHARGE);
		FireFlowerRecipe.setIngredient('B', Material.BLAZE_ROD);
		FireFlowerRecipe.setIngredient('T', Material.TORCHFLOWER);
		FireFlowerRecipe.setIngredient('M', Material.MAGMA_BLOCK);

		ShapedRecipe RockMushroomRecipe = new ShapedRecipe(new NamespacedKey(this, "rockmushroomrecipe"), ItemManager.Rock_Mushroom);
		RockMushroomRecipe.shape("CCC", "CMC", "GRG");
		RockMushroomRecipe.setIngredient('C', Material.COBBLESTONE);
		RockMushroomRecipe.setIngredient('M', Material.BROWN_MUSHROOM);
		RockMushroomRecipe.setIngredient('G', Material.GRAVEL);
		RockMushroomRecipe.setIngredient('R', Material.RAW_IRON_BLOCK);

		Bukkit.addRecipe(FireFlowerRecipe);
     	Bukkit.addRecipe(RockMushroomRecipe);
	}
	
	public void onDisable() {
		
	}
}
