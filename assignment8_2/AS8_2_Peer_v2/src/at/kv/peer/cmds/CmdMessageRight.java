package at.kv.peer.cmds;

import at.kv.p2p.com.P2PMessage;
import at.kv.p2p.com.client.P2PComClient;
import at.kv.p2p.com.server.P2PComServerControlCommand;
import at.kv.peer.Peer;

public class CmdMessageRight extends P2PComServerControlCommand{

	private static final String CMD = "messageRight";
	private static final String CMD_OK = "messageOK";
	private static final String CMD_FAILED = "messageFAILED";
	
	private Peer me;
	private Peer rightNeighbor;
	
	public CmdMessageRight(Peer me, Peer rightNeighbor) {
		super(CMD);	
		this.me = me;
		this.rightNeighbor = rightNeighbor;
		
	}

	@Override
	public void processRequest(P2PMessage request, P2PMessage response) {
		
		
		if(request.getInformation().equals(me.getID())){
			System.out.println("Received message = " + new String(request.getPayload()));
			response.setControl(CMD_OK);
			return;
		}
		
		
		P2PComClient com = new P2PComClient(rightNeighbor.getAddress(),rightNeighbor.getPort());
		P2PMessage newMsg = new P2PMessage();
		newMsg.setControl(CMD);
		newMsg.setInformation(request.getInformation());
		newMsg.setPayload(request.getPayload());
		P2PMessage r = com.send(newMsg);
		if(r == null){
			response.setControl(CMD_FAILED);
			response.setInformation(me.getID());
			
		}else if(r.getControl().equals(CMD_FAILED)){
			response.setControl(CMD_FAILED);
			response.setInformation(r.getInformation());
			response.setPayload(new byte[0]);
		}else{
			response.setControl(CMD_OK);
			response.setInformation("");
			response.setPayload(new byte[0]);
		}		
		
	}

}