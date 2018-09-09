package runnables;

import stocks.StockUtils;

public class UpdateNewStocks implements Runnable {
	public static int PERIOD = 1000*60*60*24; //1 day
	
	public void run() {
		try {
			StockUtils.storeNewStocks();
			System.out.println("Finished STOCKS");
		} catch (Exception e) {
			//DO NOTHING
		}
		
	}

}
