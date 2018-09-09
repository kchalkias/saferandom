package utils.dbtools;

/**
 * @author Konstantinos Chalkias
 *
 */
public class DBDetails implements Comparable<DBDetails> {
	
	private static final int DEFAULT_PORT = 3306;

	public static final DBDetails SAFERANDOM = new DBDetails("safe_random", "java-user", "UHWR2bBQhQnFvziTFBfA", "144.76.234.83", DEFAULT_PORT);

	private String name; // database name
	private String username;
	private String password;
	private String ip;
	private int port;

	/**
	 * @param name
	 * @param username
	 * @param password
	 * @param port
	 */
	private DBDetails(String name, String username, String password, String ip, int port) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.ip = ip;
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DBDetails other = (DBDetails) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int compareTo(DBDetails other) {
		if (this.name.equals(other.name) && this.ip.equals(other.ip) && this.port == other.port) return 0;
		return 1;
	}

}
