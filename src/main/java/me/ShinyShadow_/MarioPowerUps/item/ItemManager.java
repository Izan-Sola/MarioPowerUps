package me.ShinyShadow_.MarioPowerUps.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
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
	public static ItemStack Rainbow_Star;
	public static ItemStack OneUp_Mushroom;
	public static ItemStack Red_Star;
	public static ItemStack Crimson_Extract;
	public static ItemStack RefinedCrimson_Extract;

	public static void init() {

		createItems();
	}

	private static void createItems() {

		ItemStack FireFlower = new ItemStack(Material.TORCHFLOWER, 1);
		ItemMeta FireFlowerMeta = FireFlower.getItemMeta();

		ItemStack RockMushroom = new ItemStack(Material.APPLE, 1);
		ItemMeta RockMushroomMeta = RockMushroom.getItemMeta();

		ItemStack CloudFlower = new ItemStack(Material.BUCKET, 1);
		ItemMeta CloudFlowerMeta = CloudFlower.getItemMeta();

		ItemStack CloudBucket = new ItemStack(Material.BUCKET, 1);
		ItemMeta CloudBucketMeta = CloudBucket.getItemMeta();

		ItemStack AirBottle = new ItemStack(Material.BUCKET, 1);
		ItemMeta AirBottleMeta = CloudBucket.getItemMeta();

		ItemStack RainbowEssence = new ItemStack(Material.BUCKET, 1);
		ItemMeta RainbowEssenceMeta = RainbowEssence.getItemMeta();

		ItemStack RefinedRainbowEssence = new ItemStack(Material.BUCKET, 1);
		ItemMeta RefinedRainbowEssenceMeta = RefinedRainbowEssence.getItemMeta();

		ItemStack RainbowStar = new ItemStack(Material.POTION, 1);
		ItemMeta RainbowStarMeta = RainbowStar.getItemMeta();

		ItemStack OneUpMushroom = new ItemStack(Material.APPLE, 1);
		ItemMeta OneUpMushroomMeta = OneUpMushroom.getItemMeta();

		ItemStack RedStar = new ItemStack(Material.POTION, 1);
		ItemMeta RedStarMeta = RainbowStar.getItemMeta();

		ItemStack CrimsonExtract = new ItemStack(Material.BOWL, 1);
		ItemMeta CrimsonExtractMeta = CrimsonExtract.getItemMeta();

		ItemStack RefinedCrimsonExtract = new ItemStack(Material.BOWL, 1);
		ItemMeta RefinedCrimsonExtractMeta = CrimsonExtract.getItemMeta();

		RefinedCrimsonExtractMeta.setDisplayName(ChatColor.RED +  "Refined Crimson Extract");
		NamespacedKey RefinedCrimsonExtractNS = new NamespacedKey("ec", "refinedcrimsonextract");
		RefinedCrimsonExtractMeta.setItemModel(RefinedCrimsonExtractNS);

		CrimsonExtractMeta.setDisplayName(ChatColor.DARK_RED +  "Crimson Extract");
		NamespacedKey CrimsonExtractNS = new NamespacedKey("ec", "crimsonextract");
		CrimsonExtractMeta.setItemModel(CrimsonExtractNS);



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
		CloudFlowerMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		((Damageable) CloudFlowerMeta).setMaxDamage(20);

		CloudBucketMeta.setDisplayName(ChatColor.WHITE +  "Cloud Bucket");
		NamespacedKey CloudBucketNS = new NamespacedKey("ec", "bucketofcloud");
		CloudBucketMeta.setItemModel(CloudBucketNS);
		CloudBucketMeta.setLore(Arrays.asList("A bucket full of clouds!"));


		AirBottleMeta.setDisplayName(ChatColor.WHITE +  "Air Bottle");
		NamespacedKey AirBottleNS = new NamespacedKey("ec", "airbottle");
		AirBottleMeta.setItemModel(AirBottleNS);

		RainbowEssenceMeta.setDisplayName(ChatColor.WHITE +  "Rainbow Essence" + ChatColor.BOLD + ChatColor.ITALIC);
		NamespacedKey RainbowEssenceNS = new NamespacedKey("ec", "rainbowessence");
		RainbowEssenceMeta.setItemModel(RainbowEssenceNS);

		RefinedRainbowEssenceMeta.setDisplayName(ChatColor.WHITE +  "Refined Rainbow Essence" + ChatColor.BOLD + ChatColor.ITALIC);
		NamespacedKey RefinedRainbowEssenceNS = new NamespacedKey("ec", "refinedrainbowessence");
		RefinedRainbowEssenceMeta.setItemModel(RefinedRainbowEssenceNS);


		RainbowStarMeta.setDisplayName(ChatColor.RED + "★" +
				ChatColor.GOLD + " " +
				ChatColor.YELLOW + "R" +
				ChatColor.GREEN + "a" +
				ChatColor.AQUA + "i" +
				ChatColor.BLUE + "n" +
				ChatColor.LIGHT_PURPLE + "b" +
				ChatColor.RED + "o" +
				ChatColor.GOLD + "w" +
				ChatColor.YELLOW + " " +
				ChatColor.GREEN + "S" +
				ChatColor.AQUA + "t" +
				ChatColor.BLUE + "a" +
				ChatColor.LIGHT_PURPLE + "r" +
				ChatColor.RED + " " +
				ChatColor.GOLD + "★");

		NamespacedKey RainbowStarNS = new NamespacedKey("ec", "rainbowstar");
		RainbowStarMeta.setLore(Arrays.asList("Immense and colorful energy is",
				"contained in this star.",
				"Whoever that channels its",
				"energy into them, will be surrounded",
				"by a powerful rainbow energy and will",
				"feel faster and stronger than ever",
				"able to knock down enemies with just their touch"));
		RainbowStarMeta.setItemModel(RainbowStarNS);
		RainbowStarMeta.addEnchant(Enchantment.INFINITY, 1, true);
		RainbowStarMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
		((Damageable) RainbowStarMeta).setMaxDamage(6000);

		OneUpMushroomMeta.setDisplayName(ChatColor.GREEN +  "1-Up Mushroom" + ChatColor.BOLD);
		NamespacedKey OneUpMushroomNS = new NamespacedKey("ec", "oneupmushroom");
		OneUpMushroomMeta.setLore(Arrays.asList("A 1Up mushroom a day, ",
												"keeps the death away!"));
		OneUpMushroomMeta.setItemModel(OneUpMushroomNS);

		RedStarMeta.setDisplayName(ChatColor.RED +  "★ Red Star ★" + ChatColor.BOLD);
		NamespacedKey RedStarNS = new NamespacedKey("ec", "redstar");
		RedStarMeta.setLore(Arrays.asList(""));
		RedStarMeta.setItemModel(RedStarNS);
		RedStarMeta.addEnchant(Enchantment.INFINITY, 1, true);
		RedStarMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
		((Damageable) RedStarMeta).setMaxDamage(6000);


		CrimsonExtract.setItemMeta(CrimsonExtractMeta);
		RefinedCrimsonExtract.setItemMeta(RefinedCrimsonExtractMeta);
		RedStar.setItemMeta(RedStarMeta);
		OneUpMushroom.setItemMeta(OneUpMushroomMeta);
		RainbowStar.setItemMeta(RainbowStarMeta);
		RefinedRainbowEssence.setItemMeta(RefinedRainbowEssenceMeta);
		RainbowEssence.setItemMeta(RainbowEssenceMeta);
		FireFlower.setItemMeta(FireFlowerMeta);
		RockMushroom.setItemMeta(RockMushroomMeta);
		CloudFlower.setItemMeta(CloudFlowerMeta);
		CloudBucket.setItemMeta(CloudBucketMeta);
		AirBottle.setItemMeta(AirBottleMeta);


		Crimson_Extract = CrimsonExtract;
		RefinedCrimson_Extract = RefinedCrimsonExtract;
		Red_Star = RedStar;
		OneUp_Mushroom = OneUpMushroom;
		Rainbow_Star = RainbowStar;
		RefinedRainbow_Essence = RefinedRainbowEssence;
		Rainbow_Essence = RainbowEssence;
		Air_Bottle = AirBottle;
		Cloud_Bucket = CloudBucket;
		Fire_Flower = FireFlower;
		Rock_Mushroom = RockMushroom;
		Cloud_Flower = CloudFlower;

	}
}
