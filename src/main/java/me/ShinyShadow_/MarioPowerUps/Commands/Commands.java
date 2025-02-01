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
					break;
				case "giverockmushroom":
					player.getInventory().addItem(ItemManager.Rock_Mushroom);
					break;
				case "givecloudflower":
					player.getInventory().addItem(ItemManager.Cloud_Flower);
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
				case "giverainbowstar":
					player.getInventory().addItem(ItemManager.Rainbow_Star);
					break;
				case "give1upmushroom":
					player.getInventory().addItem(ItemManager.OneUp_Mushroom);
					break;
				case "giveredstar":
					player.getInventory().addItem(ItemManager.Red_Star);
					break;
			}

		}
		return true;
	}
}
