package at.kv.peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MyIP {

	private static final String serviceAddress = "http://checkip.amazonaws.com";	
	
	
	public static String getCurrentAddress() {
		/*URL whatismyip;
		try {
			whatismyip = new URL(serviceAddress);
			BufferedReader in = new BufferedReader(new InputStreamReader(
	                whatismyip.openStream()));
			String address = in.readLine();
			
			in.close();
			return address; 
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;*/
		//just for debug on local machine
		return "localhost";
	}
	
}
