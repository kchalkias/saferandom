package runnables;

import contest.ContestUtils;
import bitcoin.BitcoinUtils;

public class DoRaffle implements Runnable {
	public static int PERIOD = 1000*60; //1 minute
	
	public void run() {
		try {
			System.out.println("Starting DRAWS");
			ContestUtils.doDraws();
			System.out.println("Finished DRAWS");
		} catch (Exception e) {
			//DO NOTHING
		}
	}

}
