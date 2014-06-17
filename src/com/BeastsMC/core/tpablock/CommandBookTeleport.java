package com.BeastsMC.core.tpablock;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import com.BeastsMC.core.BeastsMCCore;
import com.sk89q.commandbook.CommandBook;
import com.sk89q.commandbook.locations.TeleportComponent;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;

public class CommandBookTeleport implements Listener {
	private CommandBook cmdbook;
	private Map<String, Set<String>> tpablocks = new HashMap<String, Set<String>>();
	private TeleportComponent teleComp;
	private final BeastsMCCore plugin;
	
	public CommandBookTeleport(BeastsMCCore main) {
		plugin = main;
		cmdbook = (CommandBook) plugin.getServer().getPluginManager().getPlugin("CommandBook");
		cmdbook.isEnabled();
		teleComp = (TeleportComponent) cmdbook.getComponentManager().getComponent(TeleportComponent.class);
	}
	private CommandBook getCommandBook() {
		if(!cmdbook.isEnabled() || !cmdbook.isInitialized()) {
			cmdbook = (CommandBook) plugin.getServer().getPluginManager().getPlugin("CommandBook");
		}
		return cmdbook;
	}
	
	public void blockPlayer(String blocker, String blocked) {
		Set<String> blockList= tpablocks.get(blocker);
		if(blockList!=null) {
			blockList.add(blocked);
		} else {
			blockList = new HashSet<String>();
			blockList.add(blocked);
			tpablocks.put(blocker, blockList);
		}
	}
	public void requestTeleport(CommandSender sender, String target) {
		if(tpablocks.get(target).contains(sender.getName())) {
			return;
		}
		try {
			java.lang.reflect.Method teleReq = teleComp.getClass().getMethod("requestTeleport", CommandContext.class, CommandSender.class);
			CommandContext context = new CommandContext(new String[] {target});
			teleReq.invoke(teleComp, context, sender);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (CommandException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
;
	}
}
