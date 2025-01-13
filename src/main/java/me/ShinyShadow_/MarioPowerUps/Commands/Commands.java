package me.ShinyShadow_.MarioPowerUps.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import me.ShinyShadow_.MarioPowerUps.item.ItemManager;



public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
		// TODO Auto-generated method stub
		
		if(sender instanceof Player) {	
			Player player = (Player) sender;
			player.getInventory().addItem(ItemManager.Fire_Flower);
			ItemMeta meta = ItemManager.Fire_Flower.getItemMeta();
			player.sendMessage(""+ItemManager.Fire_Flower.getItemMeta());
		}
		return true;
	}

}
