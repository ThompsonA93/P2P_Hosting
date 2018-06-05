package at.kv.peer.cmds;

import at.kv.p2p.com.P2PMessage;
import at.kv.p2p.com.server.P2PComServerControlCommand;
import at.kv.peer.P2PTool;
import at.kv.peer.Peer;

public class CmdJoin extends P2PComServerControlCommand{

	private static final String CMD = "join";
	private static final String CMD_OK = "ok";
	
	private Peer rightNeighbor;
	
	public CmdJoin(Peer rightNeighbor) {
		super(CMD);		
		this.rightNeighbor = rightNeighbor;
		
	}

	@Override
	public void processRequest(P2PMessage request, P2PMessage response) {
		
		
		Peer newNeighbor = P2PTool.getPeerFromBytes(request.getPayload());
		

		response.setControl(CMD_OK);
		response.setInformation("");
		response.setPayload(P2PTool.getBytesFromPeer(rightNeighbor));
		
		rightNeighbor.setAddress(newNeighbor.getAddress());
		rightNeighbor.setPort(newNeighbor.getPort());
		
		
		System.out.println("New neighbor right:");
		System.out.println(newNeighbor.getID());
		
	}

}
