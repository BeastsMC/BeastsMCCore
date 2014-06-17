package com.BeastsMC.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.BeastsMC.core.BeastsMCCore;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class FakeVote {
	private final BeastsMCCore plugin;
	public FakeVote(BeastsMCCore core) {
		this.plugin = core;
	}
	@CommandPermissions(value = { "beastsmccore.fakevote" })
	@Command(aliases = { "fakevote2"}, desc = "Fakes a vote")
	public void fakevote(final CommandContext args, CommandSender sender) {
		Vote vote = new Vote();
		vote.setUsername("TheBeast808");
		vote.setAddress("72.130.194.172");
		vote.setServiceName("Testing");
		vote.setTimeStamp(""+System.currentTimeMillis());
		VotifierEvent event = new VotifierEvent(vote);
		Bukkit.getServer().getPluginManager().callEvent(event);
		sender.sendMessage("Vote sent!");
	}

}
