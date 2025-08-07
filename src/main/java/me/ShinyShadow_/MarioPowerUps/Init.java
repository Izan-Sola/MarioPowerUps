package me.ShinyShadow_.MarioPowerUps;

import me.ShinyShadow_.MarioPowerUps.Abilities.CloudFlower.CloudFlowerListener;
import me.ShinyShadow_.MarioPowerUps.Abilities.FireFlower.FireFlowerListener;
import me.ShinyShadow_.MarioPowerUps.Abilities.IceFlower.IceFlowerListener;
import me.ShinyShadow_.MarioPowerUps.Abilities.LightBoxListener;
import me.ShinyShadow_.MarioPowerUps.Abilities.OneUpMushroomListener;
import me.ShinyShadow_.MarioPowerUps.Abilities.RainbowStarListener;
import me.ShinyShadow_.MarioPowerUps.Abilities.RedStarListener;
import me.ShinyShadow_.MarioPowerUps.Abilities.RockMushroom.RockMushroomListener;
import me.ShinyShadow_.MarioPowerUps.Commands.Commands;
import me.ShinyShadow_.MarioPowerUps.Stuff.CustomItemRecipeListener;
import me.ShinyShadow_.MarioPowerUps.Stuff.PreventWeirdStuffFromHappeningListener;
import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class Init extends JavaPlugin {

	public static List<Block> tempBlocks = new ArrayList<>();

	private final List<String> commands = Arrays.asList(
			"givefireflower", "giverockmushroom", "givecloudflower", "giveairbottle", "givecloudbucket",
			"giverefinedrainbowessence", "giverainbowstar", "give1upmushroom", "giveredstar", "giverainbowessence", "giveiceflower"
	);

	private final List<Listener> listeners = Arrays.asList(
			new FireFlowerListener(this), new RockMushroomListener(this), new CloudFlowerListener(this),
			new CustomItemRecipeListener( this), new RainbowStarListener( this), new PreventWeirdStuffFromHappeningListener( this),
			new RedStarListener( this), new OneUpMushroomListener(this), new IceFlowerListener(this), new LightBoxListener( this)
	);

	public static List<ItemStack> itemStacksWithCD = new ArrayList<>();

	public void onEnable() {

		itemStacksWithCD.add(ItemManager.Red_Star);
		itemStacksWithCD.add(ItemManager.Rainbow_Star);

		ItemManager.init();

		for (String command : commands) {
			getCommand(command).setExecutor(new Commands());
		}
		for (Listener listener : listeners) {
			Bukkit.getPluginManager().registerEvents(listener, this);
		}

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
		CloudFlowerRecipe.shape("AAA", "FOF", "FAF");
		CloudFlowerRecipe.setIngredient('A', Material.BUCKET);
		CloudFlowerRecipe.setIngredient('F', Material.FEATHER);
		CloudFlowerRecipe.setIngredient('O', Material.OXEYE_DAISY);

		ShapedRecipe IceFlowerRecipe = new ShapedRecipe(new NamespacedKey(this, "iceflowerrecipe"), ItemManager.Ice_Flower);
		IceFlowerRecipe.shape("SSS", "SBS", "IPI");
		IceFlowerRecipe.setIngredient('S', Material.SNOW);
		IceFlowerRecipe.setIngredient('I', Material.ICE);
		IceFlowerRecipe.setIngredient('B', Material.BLUE_ORCHID);
		IceFlowerRecipe.setIngredient('P', Material.POWDER_SNOW_BUCKET);

		ShapedRecipe BeamBoxRecipe = new ShapedRecipe(new NamespacedKey(this, "beamboxrecipe"), ItemManager.Beam_Box);
		BeamBoxRecipe.shape("RRR", "RLW", "RRR");
		BeamBoxRecipe.setIngredient('R', Material.RED_CONCRETE);
		BeamBoxRecipe.setIngredient('L', Material.LANTERN);
		BeamBoxRecipe.setIngredient('W', Material.WAXED_COPPER_GRATE);


		Bukkit.addRecipe(BeamBoxRecipe);
		Bukkit.addRecipe(IceFlowerRecipe);
		Bukkit.addRecipe(CloudFlowerRecipe);
		Bukkit.addRecipe(FireFlowerRecipe);
     	Bukkit.addRecipe(RockMushroomRecipe);
	}
	
	public void onDisable() {

		removeAndClearStuff();
	}

	public static void removeAndClearStuff() {
		for(Block tempBlocks : tempBlocks ) {
			tempBlocks.setBlockData(Material.AIR.createBlockData());
		}
	}
}
