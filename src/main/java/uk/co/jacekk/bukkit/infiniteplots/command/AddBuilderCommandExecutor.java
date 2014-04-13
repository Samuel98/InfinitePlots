package uk.co.jacekk.bukkit.infiniteplots.command;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.evilmidget38.UUIDFetcher;

import uk.co.jacekk.bukkit.baseplugin.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.command.CommandTabCompletion;
import uk.co.jacekk.bukkit.baseplugin.command.SubCommandHandler;
import uk.co.jacekk.bukkit.infiniteplots.InfinitePlots;
import uk.co.jacekk.bukkit.infiniteplots.Permission;
import uk.co.jacekk.bukkit.infiniteplots.generation.PlotsGenerator;
import uk.co.jacekk.bukkit.infiniteplots.plot.Plot;
import uk.co.jacekk.bukkit.infiniteplots.plot.PlotLocation;

public class AddBuilderCommandExecutor extends BaseCommandExecutor<InfinitePlots>{

	public AddBuilderCommandExecutor(InfinitePlots plugin){
		super(plugin);
	}
	
	@SubCommandHandler(parent = "iplot", name = "addbuilder")
	@CommandTabCompletion("<online_player>")
	public void plotAddbuilder(CommandSender sender, String label, String[] args){
		if (!Permission.PLOT_ADD_BUILDER.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return;
		}
		
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command can only be used in game");
			return;
		}
		
		if (args.length == 0){
			sender.sendMessage(ChatColor.RED + "Must supply a players name");
			return;
		}
		
		Player player = (Player)sender;
		
		if (!(player.getWorld().getGenerator() instanceof PlotsGenerator)){
			player.sendMessage(ChatColor.RED + "You must be in a plot world");
			return;
		}
		
		Plot plot = plugin.getPlotManager().getPlotAt(PlotLocation.fromWorldLocation(player.getLocation()));
		
		if (plot == null){
			player.sendMessage(ChatColor.RED + "There is no plot at this location");
			return;
		}
		
		if (!plot.getAdmin().getUniqueId().equals(player.getUniqueId())){
			player.sendMessage(ChatColor.RED + "You do not own this plot");
			return;
		}
		
		if (plot.getBuilders().contains(args[0])){
			player.sendMessage(ChatColor.RED + args[0] + " is already a builder on your plot");
			return;
		}
		
		OfflinePlayer addPlayer = plugin.getServer().getPlayerExact(args[0]);
		
		if (addPlayer == null){
			try{
				Map<String, UUID> ids = (new UUIDFetcher(Arrays.asList(args[0]))).call();
				
				if (!ids.containsKey(args[0])){
					player.sendMessage(ChatColor.RED + args[0] + " was not online and does not have a UUID stored");
					return;
				}
				
				addPlayer = plugin.getServer().getOfflinePlayer(ids.get(args[0]));
			}catch (Exception e){
				player.sendMessage(ChatColor.RED + "Error fetching UUID: " + e.getMessage());
				return;
			}
		}
		
		plot.addBuilder(addPlayer);
		
		player.sendMessage(ChatColor.GREEN + "Added " + args[0] + " as a builder to your plot");
	}
	
	@SubCommandHandler(parent = "iplot", name = "removebuilder")
	@CommandTabCompletion("<online_player>")
	public void plotRemovebuilder(CommandSender sender, String label, String[] args){
		if (!Permission.PLOT_REMOVE_BUILDER.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return;
		}
		
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command can only be used in game");
			return;
		}
		
		if (args.length == 0){
			sender.sendMessage(ChatColor.RED + "Must supply a players name");
			return;
		}
		
		Player player = (Player)sender;
		
		if (!(player.getWorld().getGenerator() instanceof PlotsGenerator)){
			player.sendMessage(ChatColor.RED + "You must be in a plot world");
			return;
		}
		
		Plot plot = plugin.getPlotManager().getPlotAt(PlotLocation.fromWorldLocation(player.getLocation()));
		
		if (plot == null){
			player.sendMessage(ChatColor.RED + "There is no plot at this location");
			return;
		}
		
		if (!plot.getAdmin().getUniqueId().equals(player.getUniqueId())){
			player.sendMessage(ChatColor.RED + "You do not own this plot");
			return;
		}
		
		if (args[0].equalsIgnoreCase("all")){
			plot.removeAllBuilders();
			player.sendMessage(ChatColor.GREEN + "Removed all builders from your plot");
			return;
		}
		
		if (!(plot.getBuilders().contains(args[0]))){
			player.sendMessage(ChatColor.RED + args[0] + " is not a builder on your plot");
			return;
		}
		
		OfflinePlayer removePlayer = plugin.getServer().getPlayerExact(args[0]);
		
		if (removePlayer == null){
			try{
				Map<String, UUID> ids = (new UUIDFetcher(Arrays.asList(args[0]))).call();
				
				if (!ids.containsKey(args[0])){
					player.sendMessage(ChatColor.RED + args[0] + " was not online and does not have a UUID stored");
					return;
				}
				
				removePlayer = plugin.getServer().getOfflinePlayer(ids.get(args[0]));
			}catch (Exception e){
				player.sendMessage(ChatColor.RED + "Error fetching UUID: " + e.getMessage());
				return;
			}
		}
		
		plot.removeBuilder(removePlayer);
		
		player.sendMessage(ChatColor.GREEN + "Removed " + args[0] + " from your plot");
	}
	
}
