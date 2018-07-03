package logic;

import at.kv.p2p.com.P2PMessage;
import at.kv.p2p.com.client.P2PComClient;
import leecher.Leecher;

/**
 * Class to setup connections.
 */
public class Connector {
	private P2PComClient connection;
	private P2PMessage request, response;

	/** Fetches Seeder-Data from Server.
	 * @param ip
	 * @param port
	 * @return String[] -- [0]: <Resource1 IP>$<Resource1 port>$<id1>$<name1>$<size1>$<date1>$<type>
	 * 					   [1]: <Resource2 IP>$ ... 
	 */
	public String[] getResourcesFromServer(String ip, int port) {
		Leecher.logger.write("### Creating Connection to root-Server.");
		connection = new P2PComClient(ip, port);
		request = new P2PMessage("getResources", "", new byte[0]);
		response = connection.send(request);
		
		if(response == null) {
			Leecher.logger.write("# Buildup has Failed");
			return null;
		}else {
			Leecher.logger.write("# Buildup was successfull.");
			String data = new String(response.getPayload());
			
			String[] splitPayload = data.split("\n");
			return splitPayload;
		}
	}

	/** Fetches ressource by ID and translates it into File - creates dir if n/a
	 * @param resourceToFetch: Predetermined Data from ServerTable
	 * FIXME: Find an alternative way to incur int to byte[]
	 */
	public void getResourceFromSeeder(SeederData resourceToFetch) {
		Leecher.logger.write("### Creating Connection to seeder.");
		
		connection = new P2PComClient(resourceToFetch.getIP(), resourceToFetch.getPort());
		request = new P2PMessage("downloadResource", "", (resourceToFetch.getID()+"").getBytes());
		response = connection.send(request);

		Leecher.logger.write("# Successfully exchanged data.");
		FileConverter fc = new FileConverter();
		fc.convertToFile(fc.decodeB64(response.getPayload()), resourceToFetch.getName() , resourceToFetch.getType());
	}
}