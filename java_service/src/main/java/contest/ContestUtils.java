package contest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import stocks.StockData;
import utils.dbtools.DBDetails;
import utils.dbtools.DBPool;
import bitcoin.BlockData;

public class ContestUtils {

	
	public static void doDraws() {
		List<Contest> contests = getNewContests();
		for (Contest c : contests) {
			c.setValues();
		}
	}
	
	/*
	 * Store winner, wintoken, details
	 */
	public static void updateParticipants(Contest contest) {
		if (contest == null || contest.getParticipants() == null || contest.getParticipants().isEmpty()) return;
		Connection c = null;
		java.sql.PreparedStatement pstmt = null;
		
		DBPool DB = DBPool.getInstance(DBDetails.SAFERANDOM);		
		try {
			c = DB.getConnection();
			if (c != null) {
				pstmt = c.prepareStatement("UPDATE participation SET seed =?, distance=?, myorder=? WHERE id = ?;");
				
				for (Participant p: contest.getParticipants()) {
					pstmt.setString(1, p.getSeed());
					pstmt.setString(2, p.getNumericDistance().toString());
					pstmt.setInt(3, p.getOrder());
					pstmt.setInt(4, p.getId());
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBPool.closeEverything(pstmt, c);
		}
	}
	
	
	/*
	 * Store winner, wintoken, details
	 */
	public static void storeWinner(Contest contest) {
		if (contest == null || contest.getParticipants() == null || contest.getParticipants().isEmpty()) return;
		Connection c = null;
		java.sql.PreparedStatement pstmt = null;
		
		DBPool DB = DBPool.getInstance(DBDetails.SAFERANDOM);		
		try {
			c = DB.getConnection();
			if (c != null) {
				pstmt = c.prepareStatement("UPDATE contest SET wintoken =?, winner=?, details=?, state=2 WHERE id = ?;");
				pstmt.setString(1, contest.getSeed());
				pstmt.setInt(2, contest.getWinner());
				pstmt.setString(3, contest.getDetails());
				pstmt.setInt(4, contest.getId());
				pstmt.addBatch();
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBPool.closeEverything(pstmt, c);
		}
	}
	
	public static void main(String args[]) {
		System.out.println(getWinningBitcoin(new Timestamp(System.currentTimeMillis() - 2000000000)));
	}
	
	public static StockData getWinningStock(Timestamp t) {
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		DBPool DB = DBPool.getInstance(DBDetails.SAFERANDOM);
		try {
			c = DB.getConnection();
			if (c != null) {
				pstmt = c.prepareStatement("SELECT * FROM snp WHERE finished > ? AND TIMEDIFF(finished,?) <= (SELECT MIN(TIMEDIFF(finished,?)) from snp where finished > ?)");
				pstmt.setTimestamp(1, t);
				pstmt.setTimestamp(2, t);
				pstmt.setTimestamp(3, t);
				pstmt.setTimestamp(4, t);
				pstmt.addBatch();
				rs = pstmt.executeQuery();
				if (rs.next()) {
					return new StockData(rs.getString("forday"), rs.getString("value"), rs.getTimestamp("finished"), rs.getString("seed"));
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBPool.closeEverything(rs, pstmt, c);
		}
		return null;
	}
	
	public static BlockData getWinningBitcoin(Timestamp t) {
		Connection c = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		DBPool DB = DBPool.getInstance(DBDetails.SAFERANDOM);
		try {
			c = DB.getConnection();
			if (c != null) {
				pstmt = c.prepareStatement("SELECT * FROM bitcoin WHERE found > ? AND TIMEDIFF(found,?) <= (SELECT MIN(TIMEDIFF(found,?)) from bitcoin where found > ?)");
				pstmt.setTimestamp(1, t);
				pstmt.setTimestamp(2, t);
				pstmt.setTimestamp(3, t);
				pstmt.setTimestamp(4, t);
				pstmt.addBatch();
				rs = pstmt.executeQuery();
				if (rs.next()) {
					return new BlockData(rs.getInt("height"), rs.getString("hash"), rs.getTimestamp("found"), rs.getString("seed"));
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBPool.closeEverything(rs, pstmt, c);
		}
		return null;
	}
	
	/*
	 * Get new Contests that require a draw
	 */
	private static List<Contest> getNewContests() {
		Connection c = null;
		ResultSet rs = null;
		Statement stmt = null;

		DBPool DB = DBPool.getInstance(DBDetails.SAFERANDOM);
		List<Contest> contests = new ArrayList<Contest>();
		try {
			c = DB.getConnection();
			if (c != null) {
				stmt = c.createStatement();
				rs = stmt.executeQuery("SELECT * FROM contest WHERE state=0 AND endtime<NOW();");
				while (rs.next()) {
					contests.add(new Contest(rs.getInt("id"), rs.getInt("type"), rs.getInt("state"), rs.getTimestamp("endtime")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBPool.closeEverything(rs, stmt, c);
		}
		for (Contest contest: contests) {
			contest.setParticipants(getParticipants(contest));
		}
		
		return contests;
	}
	
	/*
	 * Get new Contests that require a draw
	 */
	private static List<Participant> getParticipants(Contest contest) {
		Connection c = null;
		ResultSet rs = null;
		Statement stmt = null;

		DBPool DB = DBPool.getInstance(DBDetails.SAFERANDOM);
		List<Participant> participants = new ArrayList<Participant>();
		try {
			c = DB.getConnection();
			if (c != null) {
				stmt = c.createStatement();
				rs = stmt.executeQuery("SELECT * FROM participation WHERE contest_id = " + contest.getId());
				while (rs.next()) {
					participants.add(new Participant(rs.getInt("id"),rs.getString("identifier")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBPool.closeEverything(rs, stmt, c);
		}
		return participants;
	}

}
