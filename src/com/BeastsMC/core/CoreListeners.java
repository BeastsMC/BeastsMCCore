package com.BeastsMC.core;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.BeastsMC.core.tpablock.CommandBookTeleport;

public class CoreListeners implements Listener {

	private final BeastsMCCore plugin;
	public CoreListeners(BeastsMCCore main) {
		plugin = main;
	}
	
	@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
	public void commandPreprocess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String command = e.getMessage();
		if(command.equalsIgnoreCase("call") && p.hasPermission("commandbook.call") && command.length() > 0) {
			e.setCancelled(true);
			String target = command.split(" ")[0];
			CommandBookTeleport comp = (CommandBookTeleport) plugin.getComponent("tpablock");
			comp.requestTeleport((CommandSender)e.getPlayer(), target);
		}
	}
}
