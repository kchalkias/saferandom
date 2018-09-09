package projectMain;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.bitcoinj.store.BlockStoreException;

import runnables.DoRaffle;
import runnables.UpdateNewBlocks;
import runnables.UpdateNewStocks;
import utils.StaticVars;

public class Main {
	public static void main (String args[]) throws BlockStoreException, InterruptedException, ExecutionException, IOException {
		StaticVars.EXECUTOR.scheduleWithFixedDelay(new UpdateNewBlocks(), 0, UpdateNewBlocks.PERIOD, TimeUnit.MILLISECONDS);
		StaticVars.EXECUTOR.scheduleAtFixedRate(new UpdateNewStocks(), 0, UpdateNewStocks.PERIOD, TimeUnit.MILLISECONDS);
		StaticVars.EXECUTOR.scheduleAtFixedRate(new DoRaffle(), 0, DoRaffle.PERIOD, TimeUnit.MILLISECONDS);

	}
}
