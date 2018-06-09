package at.kv.p2p.seeder.logic;

import java.util.HashMap;

public class ServerFactory {

	private static HashMap<Integer,Server> createdServers = new HashMap<>();
	
	private ServerFactory(){
		
	}
	
	public static Server getServer(int port){
		
		if(createdServers.containsKey(port)){
			return createdServers.get(port);
		}
		
		Server server = new Server(port);
		
		createdServers.put(port, server);
		return server;
		
	}
	
}
