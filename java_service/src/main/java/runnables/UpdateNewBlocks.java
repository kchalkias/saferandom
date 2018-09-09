package runnables;

import bitcoin.BitcoinUtils;


public class UpdateNewBlocks implements Runnable {
	public static int PERIOD = 1000*60; //1 minute
	
	public void run() {
		try {
			BitcoinUtils.storeNewBlocks();
			System.out.println("Finished BLOCKS");
		} catch (Exception e) {
			//DO NOTHING
		}
		
	}

}
