package utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class StaticVars {
	
	public static ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(1);

}
