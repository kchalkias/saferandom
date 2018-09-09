package utils.dbtools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DBTime {
	/**
	 * Get the milliseconds since 01/01/1970 
	 * @param timestamp
	 * @return it returns the time for the given timestamp or -1 if timestamp is null or not in the correct form 
	 */
	public static long getTimeFromTimestamp(String timestamp){
		long time = -1;
		if(null == timestamp){
			return time;
		}
		try{
			System.out.println(timestamp);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date d = sdf.parse(timestamp);
			time = d.getTime();
		}catch(Exception e){}
		return time;
	}
}
