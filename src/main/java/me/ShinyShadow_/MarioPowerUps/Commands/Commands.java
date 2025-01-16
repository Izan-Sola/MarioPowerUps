package me.ShinyShadow_.MarioPowerUps.Commands;

import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
		// TODO Auto-generated method stub
		
		if(sender instanceof Player) {	
			Player player = (Player) sender;
			player.sendMessage(command.getName());
			switch (command.getName()) {
				case "givefireflower":
					player.getInventory().addItem(ItemManager.Fire_Flower);
					//player.sendMessage(""+ItemManager.Fire_Flower.getItemMeta());
					break;
				case "giverockmushroom":
					player.getInventory().addItem(ItemManager.Rock_Mushroom);
					break;
			}

		}
		return true;
	}
}
