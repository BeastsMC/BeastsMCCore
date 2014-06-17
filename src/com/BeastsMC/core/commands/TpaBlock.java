package com.BeastsMC.core.commands;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.BeastsMC.core.BeastsMCCore;
import com.BeastsMC.core.tpablock.CommandBookTeleport;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class TpaBlock {
	
	private final BeastsMCCore plugin;
	public TpaBlock(BeastsMCCore core) {
		this.plugin = core;
	}
	
	@CommandPermissions(value = { "beastsmccore.tpablock" })
	@Command(desc = "Blocks a player from tpa/calling you.", usage = "[player] - player to block", min = 1, max = 1,  aliases = { "tpablock", "callblock" })
	public void tpaBlockPlayer(CommandContext args, CommandSender sender) {
		String target = args.getString(0);
		((CommandBookTeleport)plugin.getComponent("tpablock")).blockPlayer(sender.getName(), target);
		sender.sendMessage(ChatColor.AQUA + "Blocked " + target + " from requesting a teleport!");
	}

}
