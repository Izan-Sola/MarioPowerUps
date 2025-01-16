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
	public static void init() {

		createItems();
		//getCommand("givefirebow").setExecutor(new fireBowCommand());
	}
	private static void createItems() {

		ItemStack FireFlower = new ItemStack(Material.TORCHFLOWER, 1);
		ItemMeta FireFlowerMeta = FireFlower.getItemMeta();

		ItemStack RockMushroom = new ItemStack(Material.APPLE, 1);
		ItemMeta RockMushroomMeta = RockMushroom.getItemMeta();


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
		//NamespacedKey RockMushroomNS = new NamespacedKey("ec", "rockmushroom");
		RockMushroomMeta.setLore(Arrays.asList("This mushroom, despite being ",
											 "as hard as a rock, it's edible ",
											 "(although it requires a strong bite) ",
											 "and when consumed provides a great ",
											 "strength and power such that you could ",
											 "crumble walls with your body"));
		//RockMushroomMeta.setItemModel(RockMushroomNS);


		FireFlower.setItemMeta(FireFlowerMeta);
		RockMushroom.setItemMeta(RockMushroomMeta);
		Fire_Flower = FireFlower;
		Rock_Mushroom = RockMushroom;
	}
}
