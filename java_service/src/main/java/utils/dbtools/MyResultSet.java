package utils.dbtools;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 
 * @author Konstantinos Chalkias
 * this is a wrapper of ResultSet
 * while ResultSet returns 0 for some methods even if in DB the value is null, this class never returns
 * primitive value, but always uses wrappers for return values
 */
public class MyResultSet {
	
	private ResultSet rs;

	public MyResultSet(ResultSet rs) {
		super();
		this.rs = rs;
	}
	

	
	public boolean next() throws SQLException{
		return rs.next();
	}

	public void close() throws SQLException{
		rs.close();
	}
	
	public String getString(String columnLabel) throws SQLException{
		return rs.getString(columnLabel);
	}
	
	public Boolean getBoolean(String columnLabel) throws SQLException{
		if (rs.getObject(columnLabel) == null){
			return null;
		}
		return rs.getBoolean(columnLabel);
	}
	
	public Long getLong(String columnLabel) throws SQLException{
		if (rs.getObject(columnLabel) == null){
			return null;
		}
		return rs.getLong(columnLabel);
	}
	
	public Integer getInteger(String columnLabel) throws SQLException{
		if (rs.getObject(columnLabel) == null){
			return null;
		}
		return rs.getInt(columnLabel);
	}
	
	public Double getDouble(String columnLabel) throws SQLException{
		if (rs.getObject(columnLabel) == null){
			return null;
		}
		return rs.getDouble(columnLabel);
	}
	
	public java.sql.Date getDate(String columnLabel) throws SQLException{
		return rs.getDate(columnLabel);
	}
	
    public java.sql.Time getTime(String columnLabel) throws SQLException{
    	return rs.getTime(columnLabel);
    }

    public java.sql.Timestamp getTimestamp(String columnLabel) throws SQLException{
    	return rs.getTimestamp(columnLabel);
    }
    
    public boolean previous() throws SQLException{
    	return rs.previous();
    }
}

