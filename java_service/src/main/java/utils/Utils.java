package utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
	private static SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
	private static SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
	
	static {
		dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	public final static String SHA256(String input) {
		return org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
	}
	
	public final static Timestamp getTimeStamp(Date d) {
		try {
			return new Timestamp(dateFormatLocal.parse( dateFormatUTC.format(d) ).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
