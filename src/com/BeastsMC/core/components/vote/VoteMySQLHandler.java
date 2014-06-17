package com.BeastsMC.core.components.vote;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class VoteMySQLHandler {
	private final String url;
	private final String user;
	private final String pass;
	private Connection dbConn;
	//Statements
	private static final String clearDailyVotes = "UPDATE votecount SET daily=0";
	private static final String clearMonthlyVotes = "UPDATE votecount SET monthly=0";
	private static final String addVote = "INSERT INTO votecount VALUES (?, ?, 0, 0) ON DUPLICATE KEY UPDATE daily=daily+1, monthly=monthly+1";
	private static final String getVoteCount = "SELECT daily, monthly FROM votecount WHERE uuid=?";

	public VoteMySQLHandler(String db, String host, String port, String username, String password) throws SQLException {
		url = String.format("jdbc:mysql://%s:%s/%s", host, port, db);
		user = username;
		pass = password;
		dbConn = DriverManager.getConnection(url, user, pass);
		dbConn.prepareStatement("CREATE TABLE IF NOT EXISTS votecount (username VARCHAR(16) not NULL, uuid VARCHAR(36) not NULL, daily TINYINT(1) not NULL, monthly SMALLINT(2) not NULL, PRIMARY KEY(uuid))").execute();
	}
	
	private Connection getConnection() {
		try {
			if(dbConn.isClosed()) {
				dbConn = DriverManager.getConnection(url, user, pass);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbConn;
	}
	
	public int[] getVotes(String uuid) throws SQLException {
		PreparedStatement stmt = getConnection().prepareStatement(getVoteCount);
		stmt.setString(1, uuid);
		ResultSet rs = stmt.executeQuery();
		if(rs.first()) {
			int[] votes = new int[]{ rs.getInt("daily"), rs.getInt("daily") };
			rs.close();
			stmt.close();
			return votes;
		}
		return new int[]{0, 0};
	}
	
	public void clearDaily() throws SQLException {
		PreparedStatement stmt = getConnection().prepareStatement(clearDailyVotes);
		stmt.execute();
		stmt.close();
	}
	
	public void clearMonthly() throws SQLException {
		PreparedStatement stmt = getConnection().prepareStatement(clearMonthlyVotes);
		stmt.execute();
		stmt.close();
	}
	public void addVote(String username, String uuid) throws SQLException {
		PreparedStatement stmt = getConnection().prepareStatement(addVote);
		stmt.setString(1, username);
		stmt.setString(2, uuid);
		stmt.execute();
		stmt.close();
	}
}
