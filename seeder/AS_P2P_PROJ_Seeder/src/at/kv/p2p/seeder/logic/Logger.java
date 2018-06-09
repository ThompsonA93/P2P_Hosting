package at.kv.p2p.seeder.logic;

import java.util.ArrayList;
import java.util.List;

public class Logger {

	private static List<LoggerListener> listeners = new ArrayList<>();
	
	public static boolean addListener(LoggerListener listener){
		return listeners.add(listener);
	}
	
	public static boolean removeListener(LoggerListener listener){
		return listeners.remove(listener);
	}
	
	public static void write(String log){
		System.out.println("LOG: " + log);
		for(LoggerListener l : listeners){
			l.newEntire(log);
		}
	}
	
}
