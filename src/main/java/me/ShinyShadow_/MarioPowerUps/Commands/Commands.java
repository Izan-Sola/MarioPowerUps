package me.ShinyShadow_.MarioPowerUps.Commands;

import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
		
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
				case "giverainbowessence":
					player.getInventory().addItem(ItemManager.Rainbow_Essence);
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
				case "giveiceflower":
					player.getInventory().addItem(ItemManager.Ice_Flower);
					break;
				case "givecrimsonextract":
					player.getInventory().addItem(ItemManager.Crimson_Extract);
					break;
				case "giverefinedcrimsonextract":
					player.getInventory().addItem(ItemManager.RefinedCrimson_Extract);
					break;
				case "powerupshelp":
					player.sendMessage("Help: You can get a copy of a powerup or custom item typing, per example, /givefireflower, /givecloudbucket"+
							"");
			}

		}
		return true;
	}
}
