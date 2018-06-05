package at.kv.peer;

import java.util.Scanner;

import at.kv.p2p.com.P2PMessage;
import at.kv.p2p.com.client.P2PComClient;
import at.kv.p2p.com.server.P2PComServer;
import at.kv.p2p.com.server.P2PComServerCtrlCmdManager;
import at.kv.peer.cmds.CmdCount;
import at.kv.peer.cmds.CmdJoin;
import at.kv.peer.cmds.CmdMessageLeft;
import at.kv.peer.cmds.CmdMessageRight;
import at.kv.peer.cmds.CmdSetNeighborLeft;
import at.kv.peer.cmds.CmdSetNeighborRight;


public class Node {
	
	private static final String P2P_CMD_JOIN = "join";
	private static final String P2P_CMD_SET_LEFT_NEIGHBOR = "setNeighborLeft";
	private static final String P2P_CMD_SET_RIGHT_NEIGHBOR = "setNeighborRight";
	private static final String P2P_CMD_COUNT = "count";
	private static final String P2P_CMD_MESSAGE_RIGHT = "messageRight";
	private static final String P2P_CMD_MESSAGE_LEFT = "messageLeft";
	private static final String P2P_CMD_MESSAGE_OK = "messageOK";
	private static final String P2P_CMD_MESSAGE_FAILED = "messageFAILED";
	
	private static final String CMD_CLOSE = "close";
	private static final String CMD_LEAVE = "leave";
	private static final String CMD_LEAVE_WITH_FAILURE = "leaveWithFailure";
	private static final String CMD_COUNT = "count";
	private static final String CMD_SEND = "send";
		
	private String entrieAddress = "";
	private int entriePort = 0;
	private int myPort = 0;
	private boolean isSuperNode = false;
	private P2PComServer server;
	private Peer neighborLeft = new Peer();
	private Peer neighborRight = new Peer();
	
	
	public Node(String entrieAddress, int entriePort, int myPort){
		
		this.entrieAddress = entrieAddress;
		this.entriePort = entriePort;
		this.myPort = myPort;
		
		server = new P2PComServer(myPort);
				
		if(entrieAddress.isEmpty() || entriePort == 0){
			neighborLeft.setAddress(MyIP.getCurrentAddress());
			neighborLeft.setPort(myPort);
			neighborRight.setAddress(MyIP.getCurrentAddress());
			neighborRight.setPort(myPort);
			isSuperNode = true;			
		}
		
		P2PComServerCtrlCmdManager cmdManager = P2PComServerCtrlCmdManager.getInstance();
		cmdManager.addControlCommand(new CmdJoin(neighborRight));
		cmdManager.addControlCommand(new CmdSetNeighborLeft(neighborLeft));
		cmdManager.addControlCommand(new CmdSetNeighborRight(neighborRight));
		cmdManager.addControlCommand(new CmdCount(new Peer(MyIP.getCurrentAddress(),myPort),neighborRight));
		cmdManager.addControlCommand(new CmdMessageRight(new Peer(MyIP.getCurrentAddress(),myPort),neighborRight));
		cmdManager.addControlCommand(new CmdMessageLeft(new Peer(MyIP.getCurrentAddress(),myPort),neighborLeft));
		
		server.start();
		
	}
	
	public boolean join(){
		
		if(isSuperNode){
			System.err.println("Could not join() because I am super node");
			return true;
		}
		
		Peer p = new Peer(MyIP.getCurrentAddress(),myPort);
		P2PComClient client = new P2PComClient(entrieAddress, entriePort);
		P2PMessage response = client.send(new P2PMessage(P2P_CMD_JOIN,"",P2PTool.getBytesFromPeer(p)));
		
		if(response == null){
			return false;
		}
		
		Peer newRightNeighbor = P2PTool.getPeerFromBytes(response.getPayload());
		if(newRightNeighbor == null){
			return false;
		}		
		System.out.println("new right neighbor---> " + newRightNeighbor.getID());
		
		// set new neighbors
		neighborRight.setAddress(newRightNeighbor.getAddress());
		neighborRight.setPort(newRightNeighbor.getPort());
		
		neighborLeft.setAddress(entrieAddress);
		neighborLeft.setPort(entriePort);
		
		// register on new right neighbor
		P2PComClient rightNeighborConnection = new P2PComClient(neighborRight.getAddress(),neighborRight.getPort());
		response = rightNeighborConnection.send(new P2PMessage(P2P_CMD_SET_LEFT_NEIGHBOR,"",P2PTool.getBytesFromPeer(p)));
		if(response == null){
			return false;
		}
		
		return true;
	}
	
	public String getNeighbors(){
		return "Right = " + neighborRight.getID() + "\n"+
				"Left = " + neighborLeft.getID();
	}
	
	public void leave(){
		//sending address of left neighbor to right neighbor
		P2PMessage msg = new P2PMessage();
		msg.setControl(P2P_CMD_SET_LEFT_NEIGHBOR);
		msg.setInformation("");
		msg.setPayload(P2PTool.getBytesFromPeer(neighborLeft));
		P2PComClient neighborRightConnection = new P2PComClient(neighborRight.getAddress(),neighborRight.getPort());
		neighborRightConnection.send(msg);
		
		//sending address of right neighbor to left neighbor
		msg.setControl(P2P_CMD_SET_RIGHT_NEIGHBOR);
		msg.setPayload(P2PTool.getBytesFromPeer(neighborRight));
		P2PComClient neighborLeftConnection = new P2PComClient(neighborLeft.getAddress(), neighborLeft.getPort());
		neighborLeftConnection.send(msg);
		
		
		
		server.stop();
	}
	
	public void leaveWithFault(){
		server.stop();
	}
	
	public void count(){
		System.out.println("Sending count message to ["+neighborRight.getID()+"]");
		Peer me = new Peer(MyIP.getCurrentAddress(),myPort);
		
		P2PMessage msg = new P2PMessage();
		msg.setControl(P2P_CMD_COUNT);
		msg.setInformation(me.getID());
		msg.setPayload((me.getID()+ " ").getBytes());
		P2PComClient rightNeighborConnection = new P2PComClient(neighborRight.getAddress(),neighborRight.getPort());
		P2PMessage response = rightNeighborConnection.send(msg);
		
		if(response == null ){
			System.err.println("Count failed right neighbor not reachable!");
		}
				
	}
	
	
	
	public void sendTo(String peerID, String data){
		
		/*
		 * Message:
		 * Control = message
		 * Information =	<destination id>		 *					
		 * Payload = <Message>
		 */
		
		String resultRight = sendMessageRight(peerID,data);		
		
		if(resultRight.equals(P2P_CMD_MESSAGE_OK)){
			System.out.println("Successfully sent message!");
			return;
		}
		
		String resultLeft = sendMessageLeft(peerID,data);
		if(resultLeft.equals(P2P_CMD_MESSAGE_OK)){
			System.out.println("Successfully send message");
			return;
		}
		
		System.out.println("Failure detected by:");
		System.out.println(resultRight);
		System.out.println(resultLeft);
		
		// if nothing worked connect the two peers which detected the failure
		P2PComClient rightConnection = new P2PComClient(resultRight.split(":")[0],Integer.parseInt(resultRight.split(":")[1]));
		P2PMessage msg = new P2PMessage();
		msg.setControl(P2P_CMD_SET_RIGHT_NEIGHBOR);
		msg.setPayload(P2PTool.getBytesFromPeer(new Peer(resultLeft.split(":")[0],Integer.parseInt(resultLeft.split(":")[1]))));
		rightConnection.send(msg);
		
		P2PComClient leftConnection = new P2PComClient(resultLeft.split(":")[0],Integer.parseInt(resultLeft.split(":")[1]));
		
		msg.setControl(P2P_CMD_SET_LEFT_NEIGHBOR);
		msg.setPayload(P2PTool.getBytesFromPeer(new Peer(resultRight.split(":")[0],Integer.parseInt(resultRight.split(":")[1]))));
		leftConnection.send(msg);
		
		//try again right way
		sendMessageRight(peerID,data);		
	}
	
	private String sendMessageRight(String peerID, String data){
		P2PMessage msg = new P2PMessage();
		msg.setControl(P2P_CMD_MESSAGE_RIGHT);
		msg.setInformation(peerID);
		msg.setPayload(data.getBytes());
		
		P2PComClient connection = new P2PComClient(neighborRight.getAddress(),neighborRight.getPort());
		P2PMessage response = connection.send(msg);
		
		
		// if detected by this node
		if(response == null){
			return MyIP.getCurrentAddress()+":"+myPort;
		}else if(response.getControl().equals(P2P_CMD_MESSAGE_FAILED)){
			return response.getInformation();
		}
		
		return P2P_CMD_MESSAGE_OK;
		
	}
	
	private String sendMessageLeft(String peerID, String data){
		P2PMessage msg = new P2PMessage();
		msg.setControl(P2P_CMD_MESSAGE_LEFT);
		msg.setInformation(peerID);
		msg.setPayload(data.getBytes());
		
		P2PComClient connection = new P2PComClient(neighborLeft.getAddress(),neighborLeft.getPort());
		P2PMessage response = connection.send(msg);
		
		
		// if detected by this node
		if(response == null){
			return MyIP.getCurrentAddress()+":"+myPort;
		}else if(response.getControl().equals(P2P_CMD_MESSAGE_FAILED)){
			return response.getInformation();
		}
		
		return P2P_CMD_MESSAGE_OK;
		
	}

	public static void main(String[] args) {
		
		if(args.length < 3){
			System.out.println("Arguments are: (entrieNodeAddress) (entrieNodePort) (ownport)");
			System.out.println("Use args: 0 0 <ownport> to start this node as super node");
			System.exit(1);
		}
		
		Node peer = new Node(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		
		if(!peer.join()){
			System.out.println("Could not join p2p network!");
			System.exit(1);
		}
		
		System.out.println("Neighbors:");
		System.out.println(peer.getNeighbors());
		
		System.out.println("Commands:");
			
		System.out.println(CMD_CLOSE);
		System.out.println(CMD_LEAVE);
		System.out.println(CMD_LEAVE_WITH_FAILURE);
		System.out.println(CMD_COUNT);
		
		Scanner scn = new Scanner(System.in);
		
		boolean run = true;
		
		while(run){
		
			System.out.println("Ready!");
			String in = scn.nextLine();
		
			if(in.equals(CMD_CLOSE)){
				run = false;
				peer.leave();
			}else if(in.equals(CMD_LEAVE)){
				run = false;
				peer.leave();
			}else if(in.equals(CMD_LEAVE_WITH_FAILURE)){
				run = false;
				peer.leaveWithFault();;
			}else if(in.equals(CMD_COUNT)){
				System.out.println("Sending count message to peers!");
				peer.count();
			}else if(in.startsWith(CMD_SEND)){
				String[] parts = in.split(" ");
				String id = parts[1];
				String msg = parts[2];
				peer.sendTo(id, msg);
			}
		
		}
		scn.close();
		System.out.println("Closed!");
		System.exit(0);
		

	}

}
