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
				voteHandler.updateResetTime(current+(1000*60*60*24), true);
			}
			if(current>voteHandler.voteConf.getLong("monthly")) {
				voteHandler.mysql.clearMonthly();
				voteHandler.updateResetTime(current+(1000*60*60*24*30), false);
			}
		} catch(SQLException e) {
				e.printStackTrace();
			}
		}

	}
