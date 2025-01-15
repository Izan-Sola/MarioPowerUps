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
	public static void init() {

		createItems();
		//getCommand("givefirebow").setExecutor(new fireBowCommand());
	}
	private static void createItems() {

		ItemStack item = new ItemStack(Material.TORCHFLOWER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED +  "Fire Flower" + ChatColor.BOLD);
		//meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		//meta.setCustomModelData(2);
		NamespacedKey a = new NamespacedKey("ec", "fireflower");
		meta.setLore(Arrays.asList("This innocent looking flower ",
									"contains a powerful blazing ",
									"heat that enables you to set ",
									"enemies ablaze by launching ",
									"fire balls."));
		meta.setItemModel(a);
		Damageable Ditem = (Damageable) meta;
		((Damageable) meta).setMaxDamage(20);

		item.setItemMeta(meta);
		Fire_Flower = item;
	}
}
