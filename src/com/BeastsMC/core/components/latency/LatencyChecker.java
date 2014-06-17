package com.BeastsMC.core.components.latency;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LatencyChecker {
	public static int getPing(Player p) {
		try {
			Method handle = p.getClass().getDeclaredMethod("getHandle");
			Object ep = handle.invoke(p);
			Field pingField = ep.getClass().getField("ping");
			int ping = pingField.getInt(ep);
			return ping;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static int getAverageLatency() {
		Player[] players = Bukkit.getOnlinePlayers();
		int total = 0;
		int errors = 0;
		for(Player p : players) {
			int ping = getPing(p);
			if(ping>=0) {
				total+= ping;
			} else {
				errors++;
			}
		}
		return total/(players.length-errors);
	}
}
