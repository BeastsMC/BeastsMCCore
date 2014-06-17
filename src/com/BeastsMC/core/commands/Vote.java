package com.BeastsMC.core.commands;

import java.sql.SQLException;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.BeastsMC.core.BeastsMCCore;
import com.BeastsMC.core.components.vote.VoteHandler;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class Vote {
	private final static String remainingVotesMessage = "%sYou need %s%d%s votes for Redstone\n" +
														"%sYou need %s%d%s votes for WorldEdit";
	private final BeastsMCCore plugin;
	public Vote(BeastsMCCore core) {
		this.plugin = core;
	}
	@Command(aliases = {"vote", "votelinks"}, desc = "Returns vote info")
	public void vote(final CommandContext args, CommandSender sender) {
		VoteHandler vh = (VoteHandler)plugin.getComponent("votehandler");
		if(sender instanceof Player ) {
			sender.sendMessage(formatMessage((Player)sender, vh));
		}
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "Vote links:");
		for(String link : vh.getVoteLinks()) {
			sender.sendMessage(ChatColor.AQUA + link);
		}
	}

	private String formatMessage(Player player, VoteHandler vh) {
		try {
			int[] votes = vh.mysql.getVotes(player.getUniqueId().toString());
			int redstoneVotes = (3-votes[0])>=0 ? 3-votes[0] : 0;
			int worldeditVotes = (5-votes[0])>=0 ? 5-votes[0] : 0;
			return String.format(remainingVotesMessage,
						  ChatColor.LIGHT_PURPLE, ChatColor.AQUA, redstoneVotes, ChatColor.LIGHT_PURPLE,
						  ChatColor.LIGHT_PURPLE, ChatColor.AQUA, worldeditVotes, ChatColor.LIGHT_PURPLE);
		} catch (SQLException e) {
			return "";
		}
	}
}
