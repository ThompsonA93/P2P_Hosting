package at.kv.peer.cmds;

import at.kv.p2p.com.P2PMessage;
import at.kv.p2p.com.client.P2PComClient;
import at.kv.p2p.com.server.P2PComServerControlCommand;
import at.kv.peer.Peer;

public class CmdCount extends P2PComServerControlCommand{

	private static final String CMD = "count";
	
	private Peer me;
	private Peer rightNeighbor;
	
	public CmdCount(Peer me, Peer rightNeighbor) {
		super(CMD);		
		this.me = me;
		this.rightNeighbor = rightNeighbor;
		
	}

	@Override
	public void processRequest(P2PMessage request, P2PMessage response) {
		
		
		if(request.getInformation().equals(me.getID())){
			System.out.println("Count received:");			
			String[] ids = new String(request.getPayload()).split(" ");
			for(String id : ids){
				System.out.println(id);
			}
		}else{
			System.out.println("Count received and forward it!");
			/*String payload = request.getPayload().toString();
			payload += me.getID()+" ";
			response.setControl(CMD);
			response.setInformation(request.getInformation());
			response.setPayload(payload.getBytes());
			*/
			P2PComClient rightNeighborConnection = new P2PComClient(rightNeighbor.getAddress(), rightNeighbor.getPort());
			P2PMessage msg = new P2PMessage();
			msg.setControl(CMD);
			msg.setInformation(request.getInformation());
			String payload = new String(request.getPayload());
			payload += me.getID()+" ";
			msg.setPayload(payload.getBytes());
			rightNeighborConnection.send(msg);
			
		}
		
	}

}