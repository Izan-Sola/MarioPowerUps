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
				case "givecloudflower":
					player.getInventory().addItem(ItemManager.Cloud_Flower);
					//player.sendMessage(""+ItemManager.Fire_Flower.getItemMeta());
					break;
				case "giveairbottle":
					player.getInventory().addItem(ItemManager.Air_Bottle);
					break;
				case "givecloudbucket":
					player.getInventory().addItem(ItemManager.Cloud_Bucket);
					break;
				case "giverefinedrainbowessence":
					player.getInventory().addItem(ItemManager.RefinedRainbow_Essence);
					break;
			}

		}
		return true;
	}
}
