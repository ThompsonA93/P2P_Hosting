package at.kv.p2p.com.server;

import at.kv.p2p.com.P2PMessage;

public abstract class P2PComServerControlCommand {
	
	private String ctrl;
	
	public P2PComServerControlCommand(String ctrl){
		this.ctrl = ctrl;
	}
	
	public String getControlCmd(){
		return ctrl;
	}
			
	public abstract void processRequest(P2PMessage request, P2PMessage response);

}
