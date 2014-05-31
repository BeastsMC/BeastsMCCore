package com.BeastsMC.core.components.vote;

import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;

public class VoteResetter extends BukkitRunnable {
	private final VoteHandler voteHandler;

	public VoteResetter(VoteHandler voteHandler) {
		this.voteHandler = voteHandler;
	}

	@Override
	public void run() {
		long current = System.currentTimeMillis();
		try {
			if(current>voteHandler.voteConf.getLong("daily")) {
				voteHandler.mysql.clearDaily();
				voteHandler.updateResetTime(current+(24*3600*1000), true);
			}
			if(current>voteHandler.voteConf.getLong("monthly")) {
				voteHandler.mysql.clearMonthly();
				voteHandler.updateResetTime(current+(30*24*3600*1000), false);
			}
		} catch(SQLException e) {
				e.printStackTrace();
			}
		}

	}
