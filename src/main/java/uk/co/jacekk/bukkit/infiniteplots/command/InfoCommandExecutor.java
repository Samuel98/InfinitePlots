package uk.co.jacekk.bukkit.infiniteplots.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.jacekk.bukkit.baseplugin.command.BaseCommandExecutor;
import uk.co.jacekk.bukkit.baseplugin.command.SubCommandHandler;
import uk.co.jacekk.bukkit.baseplugin.util.ListUtils;
import uk.co.jacekk.bukkit.infiniteplots.InfinitePlots;
import uk.co.jacekk.bukkit.infiniteplots.Permission;
import uk.co.jacekk.bukkit.infiniteplots.flag.PlotFlag;
import uk.co.jacekk.bukkit.infiniteplots.generation.PlotsGenerator;
import uk.co.jacekk.bukkit.infiniteplots.plot.Plot;
import uk.co.jacekk.bukkit.infiniteplots.plot.PlotLocation;

public class InfoCommandExecutor extends BaseCommandExecutor<InfinitePlots> {
	
	public InfoCommandExecutor(InfinitePlots plugin){
		super(plugin);
	}
	
	@SubCommandHandler(parent = "iplot", name = "info")
	public void plotInfo(CommandSender sender, String label, String[] args){
		if (!Permission.PLOT_INFO.has(sender)){
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
		
		player.sendMessage(ChatColor.YELLOW + "Plot Information");
		
		player.sendMessage(ChatColor.BLUE + "Owner: " + ChatColor.RESET + plot.getAdmin().getName());
		player.sendMessage(ChatColor.BLUE + "Name: " + ChatColor.RESET + plot.getName());
		player.sendMessage(ChatColor.BLUE + "Location: " + ChatColor.RESET + plot.getLocation().getX() + ", " + plot.getLocation().getZ());
		
		if (!plot.getBuilders().isEmpty()){
			player.sendMessage(ChatColor.LIGHT_PURPLE + "Builders: " + ChatColor.RESET + ListUtils.implode(", ", plot.getBuilderNames()));
		}
		
		StringBuilder flags = new StringBuilder();
		
		for (PlotFlag flag : PlotFlag.values()){
			flags.append((plot.isFlagEnabled(flag) ? ChatColor.GREEN : ChatColor.RED) + flag.getName() + ChatColor.RESET + " ");
		}
		
		player.sendMessage("Flags: " + flags.toString());
		
		player.sendMessage("Protection: " + (plot.isBuildProtected() ? ChatColor.GREEN : ChatColor.RED) + "build " + (plot.isBuildProtected() ? ChatColor.GREEN : ChatColor.RED) + "enter");
	}
	
}
