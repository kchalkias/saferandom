package bitcoin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Peer;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import utils.Utils;
import utils.dbtools.DBDetails;
import utils.dbtools.DBPool;


public class BitcoinUtils {
	
	/*
	 * Get Header Block from block chain
	 */
	public static Block getHeader() throws BlockStoreException {
		WalletAppKit kit = new WalletAppKit(MainNetParams.get(), new java.io.File("."), "saferandom");
        kit.startAndWait();
        BlockChain chain = kit.chain();
        BlockStore bs = chain.getBlockStore();
        return bs.getChainHead().getHeader();
	}
	/*
	 * Get New Blocks not being in our DB
	 * *Requires more attention for degenerated blocks
	 */
	public static List<BlockData> getNewBlocks() throws BlockStoreException, InterruptedException, ExecutionException {
        BlockData lastStored = getLastBlock();
		//System.out.println("HASH_OLD" + lastStored.getBlockHash());
		
		WalletAppKit kit = new WalletAppKit(MainNetParams.get(), new java.io.File("."), "saferandom");
        kit.startAndWait();
        BlockChain chain = kit.chain();
        BlockStore bs = chain.getBlockStore();

		Block header = bs.getChainHead().getHeader();
		//System.out.println("HASH_NEW" + header.getHashAsString());
		List<BlockData> newBlocks = new ArrayList<BlockData>();
		Peer peer = kit.peerGroup().getDownloadPeer();
		while (!header.getHashAsString().equals(lastStored.getBlockHash())) {
			newBlocks.add(new BlockData(header.getHashAsString(), Utils.getTimeStamp(header.getTime())));
			header = peer.getBlock(header.getPrevBlockHash()).get();
			System.out.println("Found " + newBlocks.size());
		}
		kit.stop();
		if (newBlocks.isEmpty()) return null;
		
		//we should add counter in reverse order
		Collections.reverse(newBlocks);
		int counter = lastStored.getHeight();
		for (BlockData bd : newBlocks) {
			bd.setHeight(++counter);
		}
		return newBlocks;
	}
	
	
	public static void storeNewBlocks() throws BlockStoreException, InterruptedException, ExecutionException {
		storeBlocks(getNewBlocks());
	}
	
	/*
	 * Store new blocks to our DB
	 */
	public static void storeBlocks(List<BlockData> newBlocks) {
		if (newBlocks == null) return;
		Connection c = null;
		java.sql.PreparedStatement pstmt = null;
		
		DBPool DB = DBPool.getInstance(DBDetails.SAFERANDOM);		
		try {
			c = DB.getConnection();
			if (c != null) {
				pstmt = c.prepareStatement("INSERT IGNORE INTO bitcoin (height,hash,seed,found) VALUES (?,?,?,?);");
				for (BlockData b : newBlocks) {
					pstmt.setInt(1, b.getHeight());
					pstmt.setString(2, b.getBlockHash());
					pstmt.setString(3, b.getSeed());
					pstmt.setTimestamp(4, b.getBlockDate());
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBPool.closeEverything(pstmt, c);
		}
	}
	
	/*
	 * Get last block from our DB
	 */
	public static BlockData getLastBlock(){
		Connection c = null;
		ResultSet rs = null;
		Statement stmt = null;
		
		DBPool DB = DBPool.getInstance(DBDetails.SAFERANDOM);		
		BlockData lastStored = null;
		try {
			c = DB.getConnection();
			if (c != null) {
				stmt = c.createStatement();
				rs = stmt.executeQuery("SELECT * FROM bitcoin WHERE height = (SELECT MAX(height) FROM bitcoin);");
				if (rs.next()) {
					lastStored = new BlockData(rs.getInt("height"), rs.getString("hash"), rs.getTimestamp("found"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBPool.closeEverything(rs, stmt, c);
		}
		return lastStored;
	}

}
