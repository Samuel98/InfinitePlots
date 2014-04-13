package uk.co.jacekk.bukkit.infiniteplots.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.baseplugin.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.command.CommandTabCompletion;
import uk.co.jacekk.bukkit.baseplugin.command.SubCommandHandler;
import uk.co.jacekk.bukkit.infiniteplots.InfinitePlots;
import uk.co.jacekk.bukkit.infiniteplots.Permission;
import uk.co.jacekk.bukkit.infiniteplots.flag.PlotFlag;
import uk.co.jacekk.bukkit.infiniteplots.generation.PlotsGenerator;
import uk.co.jacekk.bukkit.infiniteplots.plot.Plot;
import uk.co.jacekk.bukkit.infiniteplots.plot.PlotLocation;

public class FlagCommandExecutor extends BaseCommandExecutor<InfinitePlots> {
	
	public FlagCommandExecutor(InfinitePlots plugin){
		super(plugin);
	}
	
	public List<String> getFlagList(CommandSender sender, String[] args){
		ArrayList<String> flags = new ArrayList<String>();
		
		for (PlotFlag flag : PlotFlag.values()){
			flags.add(flag.getName());
		}
		
		return flags;
	}
	
	@SubCommandHandler(parent = "iplot", name = "flag")
	@CommandTabCompletion({"[getFlagList]", "true|false"})
	public void plotFlag(CommandSender sender, String label, String[] args){
		if (!Permission.PLOT_FLAG.has(sender)){
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return;
		}
		
		if (!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "This command can only be used in game");
			return;
		}
		
		Player player = (Player) sender;
		
		if (!(player.getWorld().getGenerator() instanceof PlotsGenerator)){
			player.sendMessage(ChatColor.RED + "You must be in a plot world");
			return;
		}
		
		Plot plot = plugin.getPlotManager().getPlotAt(PlotLocation.fromWorldLocation(player.getLocation()));
		
		if (plot == null){
			player.sendMessage(ChatColor.RED + "There is no plot at this location");
			return;
		}
		
		if (!Permission.PLOT_FLAG_OTHER.has(sender) && !plot.getAdmin().getUniqueId().equals(player.getUniqueId())){
			player.sendMessage(ChatColor.RED + "You do not own this plot");
			return;
		}
		
		if (args.length == 0){
			StringBuilder flags = new StringBuilder();
			
			for (PlotFlag flag : PlotFlag.values()){
				flags.append((plot.isFlagEnabled(flag) ? ChatColor.GREEN : ChatColor.RED) + flag.getName() + ChatColor.RESET + " ");
			}
			
			player.sendMessage("Valid flags: " + flags.toString());
			return;
		}
		
		PlotFlag flag = PlotFlag.getFromName(args[0]);
		
		if (flag == null){
			player.sendMessage(ChatColor.RED + args[0] + " is not a valid plot flag");
			return;
		}
		
		if (args.length <= 1){
			player.sendMessage(ChatColor.RED + "You must provide a value to set the flag");
			return;
		}
		
		plot.setFlag(flag, (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("allow") || args[1].equalsIgnoreCase("yes")));
	}
	
}
