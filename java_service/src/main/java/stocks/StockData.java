package stocks;

import java.sql.Timestamp;

public class StockData {
	private String forday;
	private String value;
	private Timestamp finished;
	private String seed;
	
	public StockData(String forday, String value, Timestamp finished, String seed) {
		super();
		this.forday = forday;
		this.value = value;
		this.seed = seed;
		this.finished = finished;
	}
	public String getForday() {
		return forday;
	}
	public void setForday(String forday) {
		this.forday = forday;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	
	public Timestamp getFinished() {
		return finished;
	}
	public void setFinished(Timestamp finished) {
		this.finished = finished;
	}
	@Override
	public String toString() {
		return "StockData [forday=" + forday + ", value=" + value + ", seed="
				+ seed + "]";
	}
}
