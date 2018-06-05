package at.kv.peer.cmds;

import at.kv.p2p.com.P2PMessage;
import at.kv.p2p.com.server.P2PComServerControlCommand;
import at.kv.peer.P2PTool;
import at.kv.peer.Peer;

public class CmdSetNeighborRight extends P2PComServerControlCommand{

	private static final String CMD = "setNeighborRight";
	private Peer neighborRight;
	
	public CmdSetNeighborRight(Peer neighborRight) {
		super(CMD);
		this.neighborRight = neighborRight;
	}

	@Override
	public void processRequest(P2PMessage request, P2PMessage response) {
		
		Peer newNeighbor = P2PTool.getPeerFromBytes(request.getPayload());
		neighborRight.setAddress(newNeighbor.getAddress());
		neighborRight.setPort(newNeighbor.getPort());
		System.out.println("Got new neighbor right = " + newNeighbor.getID());
		
	}

	
	
}