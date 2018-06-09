package at.kv.p2p.seeder.logic;

import at.kv.p2p.com.server.P2PComServer;
import at.kv.p2p.com.server.P2PComServerCtrlCmdManager;

public class Server {
	
	private int port = 0;
	private P2PComServer server;
	
	
	public Server(int port){
		this.port = port;
		server = new P2PComServer(port);
		P2PComServerCtrlCmdManager.getInstance().addControlCommand(new ServerCmdDownloadResource());
	}
	
	public void start(){
		Logger.write("[Server] started on port ["+port+"]!");
		server.start();
	}
	
	public void stop(){
		Logger.write("[Server] stopped on port ["+port+"]!");
		server.stop();
	}
}
