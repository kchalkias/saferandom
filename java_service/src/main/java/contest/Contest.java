package contest;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import stocks.StockData;
import bitcoin.BlockData;

public class Contest {
	private int id;
	private int type;
	private int state;
	private Timestamp timestamp;
	private List<Participant> participants;
	private String seed;
	private String details;
	private int winner;
	
	public Contest(int id, int type, int state, Timestamp timestamp) {
		super();
		this.id = id;
		this.type = type;
		this.state = state;
		this.timestamp = timestamp;
	}
	
	public void setValues() {
		try {
		if (type == 1) {
			BlockData blockdata = ContestUtils.getWinningBitcoin(timestamp);
			if (blockdata == null) return;
			this.seed = blockdata.getSeed();
			this.details = "{\"height\":" + blockdata.getHeight() + ",\"block\":\"" + blockdata.getBlockHash() + "\",\"winning-token\":\"" + blockdata.getSeed() +"\"}";
		} else if (type == 2) {
			StockData stockdata = ContestUtils.getWinningStock(timestamp);
			if (stockdata == null) return;
			
			this.seed = stockdata.getSeed();
			this.details = "{\"day\":" + stockdata.getForday() + ",\"value\":\"" + stockdata.getValue() + "\",\"winning-token\":\"" + stockdata.getSeed() +"\"}";
		}
		setPoints();
		ContestUtils.storeWinner(this);
		ContestUtils.updateParticipants(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPoints() {
		if (participants == null || participants.isEmpty()) return;
		BigInteger numericSeed = new BigInteger(seed, 16);
		for (Participant p: participants) {
			BigInteger absDistance = numericSeed.subtract(new BigInteger(p.getSeed(), 16)).abs();
			p.setNumericDistance(absDistance);
		}
		Participant p = Collections.min(participants);
		Collections.sort(participants);
		for (int i =0; i<participants.size(); i++) {
			participants.get(i).setOrder(i+1);
		}
		this.winner = p.getId();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	@Override
	public String toString() {
		return "Contest [id=" + id + ", type=" + type + ", state=" + state
				+ ", timestamp=" + timestamp + ", participants=" + participants
				+ ", seed=" + seed + ", details=" + details + ", winner="
				+ winner + "]";
	}
	
	
}
