package at.kv.p2p.com.test;

import java.util.ArrayList;
import java.util.List;

import at.kv.p2p.com.P2PMessage;
import at.kv.p2p.com.client.P2PComClient;
import at.kv.p2p.com.server.P2PComServer;
import at.kv.p2p.com.server.P2PComServerControlCommand;
import at.kv.p2p.com.server.P2PComServerCtrlCmdManager;
import at.kv.p2p.com.server.P2PComServerMethod;

public class Main {
	
	public Main(){	
		
	}
	
	public void startAsServer(){
		P2PComServer server = new P2PComServer(3000);
		System.out.println("Starting server!");
		server.start();
		
		P2PComServerCtrlCmdManager ctrlManager = P2PComServerCtrlCmdManager.getInstance();
		
		ctrlManager.addControlCommand(new P2PComServerControlCommand("AddRessource"){

			@Override
			public void processRequest(P2PMessage request, P2PMessage response) {
				System.out.println("Called [AddRessource]:");
				System.out.println("Got payload -> " + new String(request.getPayload()));
				P2PMessage servermassage = P2PComServerMethod.getInstance().setmessage(request);
				response.setControl(servermassage.getControl());
				response.setInformation(servermassage.getInformation());
				response.setPayload(servermassage.getPayload());
			}
			
		});
		
		ctrlManager.addControlCommand(new P2PComServerControlCommand("CTRL2"){

			@Override
			public void processRequest(P2PMessage request, P2PMessage response) {
				System.out.println("Called [CTRL2]:");
				System.out.println("Got payload -> " + new String(request.getPayload()));
				
				response.setControl("Server-CTRL2");
				response.setInformation("Server-INFO");
				response.setPayload("Server-PAYLOAD".getBytes());
			}
			
		});
		
		
	}
	
	public void startAsClient(){
		P2PComClient client = new P2PComClient("localhost",3000);
		
		System.out.println("Sending AddRessource");
		P2PMessage msg = new P2PMessage();
		msg.setControl("AddRessource");
		msg.setInformation("127.0.0.1@3534");
		String pa = "mypicture.jpg\nmytext.tex";
		msg.setPayload(pa.getBytes());
		P2PMessage response = client.send(msg);
		System.out.println("Received:\n" + new String(response.toBytes()));
				
		System.out.println("Sending CTRL2");
		msg = new P2PMessage();
		msg.setControl("CTRL2");
		msg.setPayload("Client-PAYLOAD".getBytes());
		response = client.send(msg);
		System.out.println("Received:\n" + new String(response.toBytes()));
		
		
	}
	

	public static void main(String[] args) {
		
		System.out.println("Arguments:");
		System.out.println("-c ... starts as client");
		System.out.println("-s ... starts as server");
		
		Main p2p = new Main();
		
		if(args[0].equals("-s")){
			p2p.startAsServer();
		}else if(args[0].equals("-c")){
			p2p.startAsClient();
		}
		
	}

}
