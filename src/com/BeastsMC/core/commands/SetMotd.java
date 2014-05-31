package com.BeastsMC.core.commands;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class SetMotd {
	@CommandPermissions(value = { "beastsmccore.setmotd" })
	@Command(aliases = { "setmotd", "test"}, desc = "Sets the MOTD for the server.", usage = "[motd] - message to use", min = 1)
	public static void setmotd(final CommandContext args, CommandSender sender) {
		String motd = args.getJoinedStrings(0);
		motd = motd.replaceAll("&", String.valueOf(ChatColor.COLOR_CHAR));
		try {
			Server craftServer = Bukkit.getServer();
			Field consoleField = craftServer.getClass().getDeclaredField("console");
			consoleField.setAccessible(true);
			Object dedicatedServer = consoleField.get(craftServer);
			Method setMotd = dedicatedServer.getClass().getSuperclass().getDeclaredMethod("setMotd", String.class);
			setMotd.invoke(dedicatedServer, motd);
			sender.sendMessage(ChatColor.AQUA + "Set MOTD to: " + motd);
		} catch(Exception ignored) {
			sender.sendMessage(ChatColor.RED + "Could not set MOTD! Reflection failed");
		}
	}

}
