package contest;

import java.math.BigInteger;

import utils.Utils;

public class Participant implements Comparable<Participant>{
	private int id;
	private String identifier;
	private String seed;
	private String distance;
	private BigInteger numericDistance;
	private int order;
	public Participant(int id, String identifier, String seed, String distance) {
		super();
		this.id = id;
		this.identifier = identifier;
		this.seed = seed;
		this.distance = distance;
	}
	public Participant(int id, String identifier) {
		super();
		this.id = id;
		this.identifier = identifier;
		this.seed = Utils.SHA256(identifier);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public BigInteger getNumericDistance() {
		return numericDistance;
	}
	public void setNumericDistance(BigInteger numericDistance) {
		this.numericDistance = numericDistance;
	}
	public int compareTo(Participant arg0) {
		return this.numericDistance.compareTo(arg0.getNumericDistance());
	}
	@Override
	public String toString() {
		return "Participant [id=" + id + ", identifier=" + identifier
				+ ", seed=" + seed + ", distance=" + distance
				+ ", numericDistance=" + numericDistance + "]";
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

}
