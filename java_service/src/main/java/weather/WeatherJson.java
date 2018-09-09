package weather;

public class WeatherJson {
	private Location location;
	private Current current;
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Current getCurrent() {
		return current;
	}

	public void setCurrent(Current current) {
		this.current = current;
	}

	public WeatherJson() {
		super();
	}
	
	

	@Override
	public String toString() {
		return "WeatherJson [location=" + location + ", current=" + current
				+ "]";
	}



	public class Current {
		private long last_updated_epoch;
		private String last_updated;
		private long temp_c;
		private long temp_f;
		private int is_day;
		private long wind_mph;
		private long wind_kph;
		private long wind_degree;
		private String wind_dir;
		private long pressure_mb;
		private long pressure_in;
		private long precip_mm;
		private long precip_in;
		private long humidity;
		private long cloud;
		private long feelslike_c;
		private long feelslike_f;
		private Condition condition;

		
		@Override
		public String toString() {
			return "Current [last_updated_epoch=" + last_updated_epoch
					+ ", last_updated=" + last_updated + ", temp_c=" + temp_c
					+ ", temp_f=" + temp_f + ", is_day=" + is_day
					+ ", wind_mph=" + wind_mph + ", wind_kph=" + wind_kph
					+ ", wind_degree=" + wind_degree + ", wind_dir=" + wind_dir
					+ ", pressure_mb=" + pressure_mb + ", pressure_in="
					+ pressure_in + ", precip_mm=" + precip_mm + ", precip_in="
					+ precip_in + ", humidity=" + humidity + ", cloud=" + cloud
					+ ", feelslike_c=" + feelslike_c + ", feelslike_f="
					+ feelslike_f + "]";
		}

		public Current() {
			super();
		}

		public long getLast_updated_epoch() {
			return last_updated_epoch;
		}

		public String getLast_updated() {
			return last_updated;
		}

		public long getTemp_c() {
			return temp_c;
		}

		public long getTemp_f() {
			return temp_f;
		}

		public int getIs_day() {
			return is_day;
		}

		public long getWind_mph() {
			return wind_mph;
		}

		public long getWind_kph() {
			return wind_kph;
		}

		public long getWind_degree() {
			return wind_degree;
		}

		public String getWind_dir() {
			return wind_dir;
		}

		public long getPressure_mb() {
			return pressure_mb;
		}

		public long getPressure_in() {
			return pressure_in;
		}

		public long getPrecip_mm() {
			return precip_mm;
		}

		public long getPrecip_in() {
			return precip_in;
		}

		public long getHumidity() {
			return humidity;
		}

		public long getCloud() {
			return cloud;
		}

		public long getFeelslike_c() {
			return feelslike_c;
		}

		public long getFeelslike_f() {
			return feelslike_f;
		}

		public Condition getCondition() {
			return condition;
		}
		
		

	}
	
	public static class Condition {
		private String text;
		private String icon;
		private int code;
		public Condition() {
			super();
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		
		
	}

	public static class Location {
		private String name;
		private String region;
		private String country;
		private double lat;
		private double lon;
		private String tz_id;
		private long localtime_epoch;
		private String localtime;

		
		
		@Override
		public String toString() {
			return "Location [name=" + name + ", region=" + region
					+ ", country=" + country + ", lat=" + lat + ", lon=" + lon
					+ ", tz_id=" + tz_id + ", localtime_epoch="
					+ localtime_epoch + ", localtime=" + localtime + "]";
		}

		public Location() {
			super();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLon() {
			return lon;
		}

		public void setLon(double lon) {
			this.lon = lon;
		}

		public String getTz_id() {
			return tz_id;
		}

		public void setTz_id(String tz_id) {
			this.tz_id = tz_id;
		}

		public long getLocaltime_epoch() {
			return localtime_epoch;
		}

		public void setLocaltime_epoch(long localtime_epoch) {
			this.localtime_epoch = localtime_epoch;
		}

		public String getLocaltime() {
			return localtime;
		}

		public void setLocaltime(String localtime) {
			this.localtime = localtime;
		}
	}
}
