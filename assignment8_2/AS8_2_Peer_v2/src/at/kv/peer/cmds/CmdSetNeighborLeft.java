package at.kv.peer.cmds;

import at.kv.p2p.com.P2PMessage;
import at.kv.p2p.com.server.P2PComServerControlCommand;
import at.kv.peer.P2PTool;
import at.kv.peer.Peer;

public class CmdSetNeighborLeft extends P2PComServerControlCommand{

	private static final String CMD = "setNeighborLeft";
	private Peer neighborLeft;
	
	public CmdSetNeighborLeft(Peer neighborLeft) {
		super(CMD);
		this.neighborLeft = neighborLeft;
	}

	@Override
	public void processRequest(P2PMessage request, P2PMessage response) {
		
		Peer newNeighbor = P2PTool.getPeerFromBytes(request.getPayload());
		neighborLeft.setAddress(newNeighbor.getAddress());
		neighborLeft.setPort(newNeighbor.getPort());
		System.out.println("Got new neighbor left = " + newNeighbor.getID());
		
	}

	
	
}
