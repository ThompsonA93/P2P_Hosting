package logic;

import at.kv.p2p.com.P2PMessage;
import at.kv.p2p.com.client.P2PComClient;
import exec.Leecher;

/**
 * Implements P2PComClient to buildup connection to some server.
 */
public class Client {
	/**
	 * Standardized Connection Builder
	 */
	private P2PComClient connection;
	
	/**
	 * Commands as specified in VerSys_P2PProjekt-master/p2p_communication/commands.ods
	 */
	private P2PMessage request, response;
	
	public void initiate(String ip, int port, String ressource) {
		if(Leecher.DEBUG) {
			System.err.println("Attempting to connect on " + ip + ":" + port + "\n"
					+ "Attempting to fetch: '" + ressource + "'");
		}
		
		connection = new P2PComClient(ip, port);
		String[] seeders = fetchSeeder();
		
	}

	/**
	 * Fetch all possible Seeder from main server
	 * @param P2PComClient-setup required
	 */
	private String[] fetchSeeder() {
		if(Leecher.DEBUG) {
			System.err.println("Fetching Seeders from Main Server");
		}
		
		request = new P2PMessage("getResources", "", new byte[0]);
		response = connection.send(request);
		
		if(response == null) {
			System.err.println("Error downloading Seeders.");
			return null;
		}else {
			String[] seeders = response.getPayload().toString().split("\n");
			return seeders;
		}
	}
	
	
}