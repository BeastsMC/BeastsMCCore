package com.BeastsMC.core.components.vote;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.BeastsMC.core.BeastsMCCore;
import com.BeastsMC.core.util.UUIDFetcher;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;


public class VoteHandler implements Listener {

	private final BeastsMCCore plugin;
	protected VoteMySQLHandler mysql;
	public boolean enabled = false;
	private List<String[]> failed = new LinkedList<String[]>();
	protected YamlConfiguration voteConf;
	
	public VoteHandler(BeastsMCCore main) {
		this.plugin = main;
		//Setup MySQL
		try {
			this.mysql = new VoteMySQLHandler(
					plugin.getConfig().getString("mysql.database"),
					plugin.getConfig().getString("mysql.host"),
					plugin.getConfig().getString("mysql.port"),
					plugin.getConfig().getString("mysql.username"),
					plugin.getConfig().getString("mysql.password")
			);
		} catch (SQLException e) { 
			e.printStackTrace();
			return;
		}
		//Setup vote listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		//Load vote config
		plugin.saveResource("vote.yml", false);
		voteConf = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "vote.yml"));
		
		//Setup vote resetter
		new VoteResetter(this).runTaskTimerAsynchronously(plugin, 0, 60*20);

		plugin.getLogger().info("Vote component inialized!");
		enabled = true;
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onVotifierEvent(VotifierEvent event) {
		plugin.getLogger().info(event.getVote().toString());
		Vote vote = event.getVote();
		String username = vote.getUsername();
		if(username==null || username.equals(" ")) return;
		if(!Bukkit.getOfflinePlayer(username).hasPlayedBefore()) return;
		try {
			String uuid = new UUIDFetcher(Arrays.asList(username)).call().get(username).toString().replace("-", "");
			incrementVote(username, uuid);
			applyRewards(username, uuid);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void applyRewards(String username, String uuid) {
		try {
			int[] votes = mysql.getVotes(uuid);
			Player p = plugin.getServer().getPlayer(username);
			PermissionUser user = PermissionsEx.getUser(username);
			if(p!=null) p.sendMessage(String.format("%sThank you for voting!", ChatColor.AQUA));
			if(votes[0]==3) {
				user.addGroup("redstoner", null, 24*3600);
				plugin.getServer().broadcastMessage(
						String.format("%s%shas gotten redstone permissions for voting 3 times today! /vote",
								ChatColor.AQUA,
								username
						)
				);
				if(p!=null) p.sendMessage(
						String.format("%sYou have received %redstone permissions%s for voting!",
								ChatColor.AQUA,
								ChatColor.BOLD,
								ChatColor.RESET.toString() + ChatColor.AQUA.toString()
						)
				);
			} else if(votes[1]==5) {
				user.addGroup("worldeditor", null, 24*3600);
				plugin.getServer().broadcastMessage("&b" + username + " has gotten WorldEdit for voting 5 times today! /vote");
				if(p!=null) p.sendMessage(
						String.format("%sYou have received %WorldEdit%s for voting!",
								ChatColor.AQUA,
								ChatColor.BOLD,
								ChatColor.RESET.toString() + ChatColor.AQUA.toString()
						)
				);			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void incrementVote(String username, String uuid) {
		try {
			mysql.addVote(username, uuid);
		} catch (SQLException e) {
			e.printStackTrace();
			failed.add(new String[]{username, uuid});
		} catch(Exception e) {
			e.printStackTrace();
		}
		retryProcess();
	}
	
	private void retryProcess() {
		for(String[] vote : failed) {
			try {
				mysql.addVote(vote[0], vote[1]);
			} catch (SQLException e) {
				break;
			}
		}
	}
	
	protected void updateResetTime(long time, boolean daily) {
		if(daily) {
			voteConf.set("daily", time);
		} else {
			voteConf.set("monthly", time);
		}
		try {
			voteConf.save(plugin.getDataFolder() + "/vote.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
