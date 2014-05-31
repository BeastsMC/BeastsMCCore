package com.BeastsMC.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

public class CommandCorrector implements Listener {
	
	private BeastsMCCore plugin;
	
	private Set<String> commands;
	
	private Map<String, String> commandPermissions = new HashMap<String, String>();
	private Map<String, String> aliases = new HashMap<String, String>();
	
	public CommandCorrector(BeastsMCCore main) {
		plugin = main;
		for(Plugin plug : plugin.getServer().getPluginManager().getPlugins()) {
			Map<String, Map<String, Object>> commandMap = plug.getDescription().getCommands();
			for(String command : commandMap.keySet()) {
				commands.add(command);
				commandPermissions.put(command, commandMap.get(command).toString());
				Object aliasList = commandMap.get(command);
				if(aliasList instanceof List) {
					List<String> commandAliases = (List<String>)aliasList;
					for(String alias : commandAliases) {
						aliases.put(alias, command);
					}
				} else {
					aliases.put(aliasList.toString(), command);
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void correctCommand(PlayerCommandPreprocessEvent e) {
		String command = e.getMessage().split(" ")[0].replaceFirst("/", "");
	}

}
