package com.BeastsMC.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.BeastsMC.core.BeastsMCCore;
import com.BeastsMC.core.components.latency.LatencyChecker;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;

public class Latency {
	private final BeastsMCCore plugin;
	public Latency(BeastsMCCore main) {
		this.plugin = main;
	}
	
	@Command(aliases = {"latency"}, desc = "Returns latency info")
	public void vote(final CommandContext args, CommandSender sender) {
		if(args.argsLength()==0) {
			int avg = LatencyChecker.getAverageLatency();
			sender.sendMessage(ChatColor.GREEN + "Server has an average latency of " + avg);
		} else {
			String username = args.getString(0);
			Player p = Bukkit.getServer().getPlayer(username);
			if(p==null) {
				sender.sendMessage(ChatColor.RED + username + " is not online");
			} else {
				int latency = LatencyChecker.getPing(p);
				sender.sendMessage(ChatColor.GREEN + username + " has a latency of " + latency );
			}
		}
	}
}
