package me.ShinyShadow_.MarioPowerUps.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemManager {

	public static ItemStack Fire_Flower;
	public static ItemStack Rock_Mushroom;
	public static ItemStack Cloud_Flower;
	public static ItemStack Cloud_Bucket;
	public static ItemStack Air_Bottle;
	public static ItemStack Rainbow_Essence;
	public static ItemStack RefinedRainbow_Essence;
	public static void init() {

		createItems();
	}

	private static void createItems() {

		ItemStack FireFlower = new ItemStack(Material.TORCHFLOWER, 1);
		ItemMeta FireFlowerMeta = FireFlower.getItemMeta();

		ItemStack RockMushroom = new ItemStack(Material.APPLE, 1);
		ItemMeta RockMushroomMeta = RockMushroom.getItemMeta();

		ItemStack CloudFlower = new ItemStack(Material.OXEYE_DAISY, 1);
		ItemMeta CloudFlowerMeta = CloudFlower.getItemMeta();

		ItemStack CloudBucket = new ItemStack(Material.BUCKET, 1);
		ItemMeta CloudBucketMeta = CloudBucket.getItemMeta();

		ItemStack AirBottle = new ItemStack(Material.GLASS_BOTTLE, 1);
		ItemMeta AirBottleMeta = CloudBucket.getItemMeta();

		ItemStack RainbowEssence = new ItemStack(Material.GLASS_BOTTLE, 1);
		ItemMeta RainbowEssenceMeta = RainbowEssence.getItemMeta();

		ItemStack RefinedRainbowEssence = new ItemStack(Material.GLASS_BOTTLE, 1);
		ItemMeta RefinedRainbowEssenceMeta = RefinedRainbowEssence.getItemMeta();



		FireFlowerMeta.setDisplayName(ChatColor.RED +  "Fire Flower" + ChatColor.BOLD);
		NamespacedKey FireFlowerNS = new NamespacedKey("ec", "fireflower");
		FireFlowerMeta.setLore(Arrays.asList("This innocent looking flower ",
									"contains a powerful blazing ",
									"heat that enables you to set ",
									"enemies ablaze by launching ",
									"fire balls.",
									"",
									"Its heat can be reanimated with blaze powder."));
		FireFlowerMeta.setItemModel(FireFlowerNS);
		FireFlowerMeta.setMaxStackSize(1);
		((Damageable) FireFlowerMeta).setMaxDamage(20);


		RockMushroomMeta.setDisplayName(ChatColor.DARK_GRAY +  "Rock Mushroom" + ChatColor.BOLD);
		NamespacedKey RockMushroomNS = new NamespacedKey("ec", "rockmushroom");
		RockMushroomMeta.setLore(Arrays.asList("This mushroom, despite being ",
											 "as hard as a rock, it's edible ",
											 "(although it requires a strong bite) ",
											 "and when consumed provides a great ",
											 "strength and power such that you could ",
											 "crumble walls with your body"));
		RockMushroomMeta.setItemModel(RockMushroomNS);

		CloudFlowerMeta.setDisplayName(ChatColor.WHITE +  "Cloud Flower" + ChatColor.BOLD);
		NamespacedKey CloudFlowerNS = new NamespacedKey("ec", "cloudflower");
		CloudFlowerMeta.setLore(Arrays.asList("This flower has a tendency ",
											  "to float like a cloud, so be ",
				                              "careful not to lose grip of it! ",
											  "Carefully squishing it will summon ",
		                                      "dense clouds that you can jump on."));
		CloudFlowerMeta.setItemModel(CloudFlowerNS);

		CloudBucketMeta.setDisplayName(ChatColor.WHITE +  "Cloud Bucket");
		NamespacedKey CloudBucketNS = new NamespacedKey("ec", "bucketofcloud");
		CloudBucketMeta.setItemModel(CloudBucketNS);


		AirBottleMeta.setDisplayName(ChatColor.WHITE +  "Air Bottle");
		NamespacedKey AirBottleNS = new NamespacedKey("ec", "airbottle");
		AirBottleMeta.setItemModel(AirBottleNS);

		RainbowEssenceMeta.setDisplayName(ChatColor.WHITE +  "Rainbow Essence" + ChatColor.BOLD + ChatColor.ITALIC);
		NamespacedKey RainbowEssenceNS = new NamespacedKey("ec", "rainbowessence");
		RainbowEssenceMeta.setItemModel(RainbowEssenceNS);

		RefinedRainbowEssenceMeta.setDisplayName(ChatColor.WHITE +  "Refined Rainbow Essence" + ChatColor.BOLD + ChatColor.ITALIC);
		NamespacedKey RefinedRainbowEssenceNS = new NamespacedKey("ec", "refinedrainbowessence");
		RefinedRainbowEssenceMeta.setItemModel(RefinedRainbowEssenceNS);

		RefinedRainbowEssence.setItemMeta(RefinedRainbowEssenceMeta);
		RainbowEssence.setItemMeta(RainbowEssenceMeta);
		FireFlower.setItemMeta(FireFlowerMeta);
		RockMushroom.setItemMeta(RockMushroomMeta);
		CloudFlower.setItemMeta(CloudFlowerMeta);
		CloudBucket.setItemMeta(CloudBucketMeta);
		AirBottle.setItemMeta(AirBottleMeta);

		RefinedRainbow_Essence = RefinedRainbowEssence;
		Rainbow_Essence = RainbowEssence;
		Air_Bottle = AirBottle;
		Cloud_Bucket = CloudBucket;
		Fire_Flower = FireFlower;
		Rock_Mushroom = RockMushroom;
		Cloud_Flower = CloudFlower;
	}
}
