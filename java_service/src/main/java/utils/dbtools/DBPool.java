package utils.dbtools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBPool implements Comparable<DBPool> {
	private DBDetails dbDetails;
	
	private static final int MAX_CONN_POOL_SIZE = 3;

	private static HashSet<DBPool> allDBPools = new HashSet<DBPool>();
	private static int numOfDBPools = 0;

	// the actual coonectionPooler
	private HikariDataSource pool;

	private static Object syncObject_ = new Object();

	private DBPool(DBDetails dbDetails) {
		this.dbDetails = dbDetails;
		HikariConfig config = new HikariConfig();
		config.setInitializationFailFast(true);
		/*
		 * From: https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration This sets the number of prepared statements that the MySQL driver will
		 * cache per connection. The default is a conservative 25. We recommend setting this to between 250-500.
		 */
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		/*
		 * From: https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration This is the maximum length of a prepared SQL statement that the driver
		 * will cache. The MySQL default is 256. In our experience, especially with ORM frameworks like Hibernate, this default is well below the threshold of
		 * generated statement lengths. Our recommended setting is 2048.
		 */
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		/*
		 * Neither of the above parameters have any effect if the cache is in fact disabled, as it is by default. You must set this parameter to true.
		 */
		config.addDataSourceProperty("cachePrepStmts", "true");
		/*
		 * Newer versions of MySQL support server-side prepared statements, this can provide a substantial performance boost. Set this property to true.
		 */
		config.addDataSourceProperty("useServerPrepStmts", "true");

		config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		config.addDataSourceProperty("serverName", dbDetails.getIP());
		config.addDataSourceProperty("port", dbDetails.getPort());
		config.addDataSourceProperty("databaseName", dbDetails.getName());
		config.addDataSourceProperty("user", dbDetails.getUsername());
		config.addDataSourceProperty("password", dbDetails.getPassword());
		config.addDataSourceProperty("characterEncoding","utf8");
		config.addDataSourceProperty("useUnicode","true");
		config.setMaximumPoolSize(MAX_CONN_POOL_SIZE);

		pool = new HikariDataSource(config);
		numOfDBPools++;
	}

	public static DBPool getInstance(DBDetails dbDetails) {

		/* in a non-thread-safe version of a Singleton */
		/* the following line could be executed, and the */
		/* thread could be immediately swapped out */
		synchronized (syncObject_) {
			for (DBPool dbPool : allDBPools) {
				if (dbPool.dbDetails.equals(dbDetails)) {
					return dbPool;
				}
			}
			DBPool temp = new DBPool(dbDetails);
			if (allDBPools.add(temp)) {
				return temp;
			}
			return null;
		}
	}

	public Connection getConnection() {
		try {
			// Main.print(this.getClass().getName(), ++connections);
			return pool.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void shutdown() {
		allDBPools.remove(pool);
		pool.shutdown();
	}
	
	public static void closeEverything(MyResultSet rs, Statement stmt, Connection con) {

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeEverything(ResultSet rs, Statement stmt, Connection con) {

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeEverything(Statement stmt, Connection con) {

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeConnection(Connection con) {

		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public HikariDataSource getPool() {
		return pool;
	}

	public static void print(String s) {
		System.out.println(s);
	}

	public DBDetails getDbDetails() {
		return dbDetails;
	}

	public String getIp() {
		return dbDetails.getIP();
	}

	public void setPool(HikariDataSource pool) {
		this.pool = pool;
	}
	
	public int compareTo(DBPool other) {
		return this.dbDetails.compareTo(other.dbDetails);
	}

	public static int getNumOfDBPools() {
		return numOfDBPools;
	}

}
