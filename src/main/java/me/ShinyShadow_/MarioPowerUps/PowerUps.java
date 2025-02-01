package me.ShinyShadow_.MarioPowerUps;

import me.ShinyShadow_.MarioPowerUps.Abilities.*;
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
		getCommand("givecloudflower").setExecutor(new Commands());
		getCommand("giveairbottle").setExecutor(new Commands());
		getCommand("givecloudbucket").setExecutor(new Commands());
		getCommand("giverefinedrainbowessence").setExecutor(new Commands());
		getCommand("giverainbowstar").setExecutor(new Commands());
		getCommand("give1upmushroom").setExecutor(new Commands());
		getCommand("giveredstar").setExecutor(new Commands());


		Bukkit.getPluginManager().registerEvents(new FireFlowerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new RockMushroomListener(this), this);
		Bukkit.getPluginManager().registerEvents(new CloudFlowerListener(this), this);
		Bukkit.getPluginManager().registerEvents(new CustomItemRecipeListener( this), this);
		Bukkit.getPluginManager().registerEvents(new RainbowStarListener( this), this);
		Bukkit.getPluginManager().registerEvents(new PreventPlayerFromFuckingUpListener( this), this);
		Bukkit.getPluginManager().registerEvents(new RedStarListener( this), this);

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

		ShapedRecipe CloudFlowerRecipe = new ShapedRecipe(new NamespacedKey(this, "cloudflowerrecipe"), ItemManager.Cloud_Flower);
		CloudFlowerRecipe.shape("AAA", "FOF", "FCF");
		CloudFlowerRecipe.setIngredient('A', Material.GLASS_BOTTLE);
		CloudFlowerRecipe.setIngredient('F', Material.FEATHER);
		CloudFlowerRecipe.setIngredient('O', Material.OXEYE_DAISY);
		CloudFlowerRecipe.setIngredient('C', Material.BUCKET);



		Bukkit.addRecipe(CloudFlowerRecipe);
		Bukkit.addRecipe(FireFlowerRecipe);
     	Bukkit.addRecipe(RockMushroomRecipe);
	}
	
	public void onDisable() {
		
	}
}
