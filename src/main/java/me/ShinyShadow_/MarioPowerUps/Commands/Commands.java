package me.ShinyShadow_.MarioPowerUps.Commands;

import me.ShinyShadow_.MarioPowerUps.item.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
		
		if(sender instanceof Player) {	
			Player player = (Player) sender;
			Inventory inventory = player.getInventory();
			switch (command.getName()) {
				case "givefireflower":
					inventory.addItem(ItemManager.Fire_Flower);
					break;
				case "giverockmushroom":
					inventory.addItem(ItemManager.Rock_Mushroom);
					break;
				case "givecloudflower":
					inventory.addItem(ItemManager.Cloud_Flower);
					break;
				case "giveairbottle":
					inventory.addItem(ItemManager.Air_Bottle);
					break;
				case "givecloudbucket":
					inventory.addItem(ItemManager.Cloud_Bucket);
					break;
				case "giverainbowessence":
					inventory.addItem(ItemManager.Rainbow_Essence);
					break;
				case "giverefinedrainbowessence":
					inventory.addItem(ItemManager.RefinedRainbow_Essence);
					break;
				case "giverainbowstar":
					inventory.addItem(ItemManager.Rainbow_Star);
					break;
				case "give1upmushroom":
					inventory.addItem(ItemManager.OneUp_Mushroom);
					break;
				case "giveredstar":
					inventory.addItem(ItemManager.Red_Star);
					break;
				case "giveiceflower":
					player.getInventory().addItem(ItemManager.Ice_Flower);
					break;
				case "givecrimsonextract":
					inventory.addItem(ItemManager.Crimson_Extract);
					break;
				case "giverefinedcrimsonextract":
					inventory.addItem(ItemManager.RefinedCrimson_Extract);
					break;
				case "givebeambox":
					inventory.addItem(ItemManager.Beam_Box);
					break;
				case "powerupshelp":
					player.sendMessage("Help: You can get a copy of a powerup or custom item typing, per example, /givefireflower, /givecloudbucket"+
							"");
				case "givesupermushroom":
					inventory.addItem(ItemManager.Super_Mushroom);
			}

		}
		return true;
	}
}
