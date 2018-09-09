package bitcoin;

import java.sql.Timestamp;

import utils.Utils;

public class BlockData {
	
	private int height;
	private String blockHash;
	private Timestamp blockDate;
	private String seed;
	
	public BlockData(int height, String blockHash, Timestamp blockDate) {
		this(blockHash, blockDate);
		this.height = height;
		
	}
	
	public BlockData(int height, String blockHash, Timestamp blockDate, String seed) {
		this(height, blockHash, blockDate);
		this.seed = seed;
	}
	
	public BlockData(String blockHash, Timestamp blockDate) {
		super();
		this.blockHash = blockHash;
		this.blockDate = blockDate;
		this.seed = Utils.SHA256(blockHash);
	}

	public int getHeight() {
		return height;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public Timestamp getBlockDate() {
		return blockDate;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getSeed() {
		return seed;
	}

	@Override
	public String toString() {
		return "BlockData [height=" + height + ", blockHash=" + blockHash
				+ ", blockDate=" + blockDate + ", seed=" + seed + "]";
	}
	
}
