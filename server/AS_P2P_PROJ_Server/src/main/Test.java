package main;

import java.util.ArrayList;
import java.util.List;

import Server_Method.Servermethod;
import at.kv.p2p.com.P2PMessage;
 import at.kv.p2p.com.client.P2PComClient;
 import at.kv.p2p.com.server.P2PComServer;
 import at.kv.p2p.com.server.P2PComServerControlCommand;
 import at.kv.p2p.com.server.P2PComServerCtrlCmdManager;
import at.kv.p2p.com.test.Main;

 

public class Test {
	
	public static void main(String[] args) {
		System.out.println("Hello World");
		System.out.println("Arguments:");
		System.out.println("-c ... starts as client");
		System.out.println("-s ... starts as server");
		
		Test p2p = new Test();
		
		if(args[0].equals("-s")){
			p2p.startAsServer();
		}else if(args[0].equals("-c")){
			p2p.startAsClient();
		}
		
	}
	
	public void startAsServer(){
		P2PComServer server = new P2PComServer(3000);
 		System.out.println("Starting server!");
 		server.start();
 		
 		P2PComServerCtrlCmdManager ctrlManager = P2PComServerCtrlCmdManager.getInstance();
 			ctrlManager.addControlCommand(new P2PComServerControlCommand("AddResource"){
 			@Override
 			public void processRequest(P2PMessage request, P2PMessage response) {
 				
				System.out.println("Called [AddResource]:");
 				System.out.println("Got payload -> " + new String(request.getPayload()));
 				P2PMessage servermassage = Servermethod.getInstance().setmessage(request);
 				response.setControl(servermassage.getControl());
 				response.setInformation(servermassage.getInformation());
 				response.setPayload(servermassage.getPayload());
 			}
 			
 		});
 		
 		ctrlManager.addControlCommand(new P2PComServerControlCommand("GetResource"){
 
 			@Override
 			public void processRequest(P2PMessage request, P2PMessage response) {
 				System.out.println("Called [GetResource]:");
 				System.out.println("Got payload -> " + new String(request.getPayload()));
 				P2PMessage servermassage = Servermethod.getInstance().setmessage(request);
 				response.setControl(servermassage.getControl());
 				response.setInformation(servermassage.getInformation());
 				response.setPayload(servermassage.getPayload());
 			}
 			
 		});
	}
	
	public void startAsClient(){
		System.out.println("Hello");
		P2PComClient client = new P2PComClient("localhost",3000);
		
		System.out.println("Sending AddResource");
		P2PMessage msg = new P2PMessage();
		msg.setControl("AddResource");
		msg.setInformation("127.0.0.1@3534");
		String pa = "mypicture.jpg\nmytext.tex";
		msg.setPayload(pa.getBytes());
		P2PMessage response = client.send(msg);
		
		if(response != null){
			System.out.println("Received:\n" + new String(response.toBytes()));
			
			
			System.out.println("Sending AddResource");
			msg = new P2PMessage();
			msg.setControl("AddResource");
			msg.setInformation("127.0.0.1@3534");
			msg.setPayload("mypictur.jpg".getBytes());
			response = client.send(msg);
			System.out.println("Received:\n" + new String(response.toBytes()));
		}else{
			System.out.println("Connection failed!");
		}
		
	}
	
	
}
