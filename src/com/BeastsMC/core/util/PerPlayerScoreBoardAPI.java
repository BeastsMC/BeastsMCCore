package com.BeastsMC.core.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.BeastsMC.core.BeastsMCCore;

public class PerPlayerScoreBoardAPI {
	private HashMap<String, Scoreboard> boards;
	
	public PerPlayerScoreBoardAPI(BeastsMCCore main) {
		boards = new HashMap<String, Scoreboard>();
	}
	
	public Scoreboard createScoreBoard(String player) {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		boards.put(player, board);
		return board;
	}
	
	public Scoreboard getPlayerBoard(String username) {
		return boards.containsKey(username) ?  boards.get(username) : createScoreBoard(username);
	}
	
	public Objective addObjective(String player, String objName) {
		return getPlayerBoard(player).registerNewObjective(objName, "dummy");
	}
	
	public void addLine(String owner, String objName, String name, int value) {
		Scoreboard board = getPlayerBoard(owner);
		Score score = board.getObjective(objName).getScore(Bukkit.getOfflinePlayer(name));
		score.setScore(value);
	}
}
