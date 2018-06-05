package at.kv.p2p.com.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import at.kv.p2p.com.P2PMessage;

public class P2PComServerMethod {

	private static P2PComServerMethod instance = null;
	P2PMessage message;
	static HashMap<String, List> GHTlist; // Adress:port as key, ressource list as value 
	String command;
	String information;
	byte[] payload;
	// Singleton all Information of Server will be stored here
	public static P2PComServerMethod getInstance(){
		if(instance == null){
			instance = new P2PComServerMethod();
		}
		return instance;
	}
	public P2PMessage setmessage(P2PMessage message){
		this.message = message;
		this.command = message.getControl();
		this.information = message.getInformation();
		this.payload = message.getPayload();
		return dealRequest();
	}
	public P2PComServerMethod(){
		GHTlist = new HashMap<>();
	}
	/**
	 * check Command, and determine which method should be called
	 * 
	 * @return result Response P2PMessage to Client
	 */
	public P2PMessage dealRequest(){
		P2PMessage result = new P2PMessage();
		switch (command) {
		case "AddRessource":
			result =  updateDHT();
			break;
		case "GetRessource":
			result =  fetchFromDHT();
			break;
		case "detachRessource":
			result =  updateDHT();
			break;
		case "listRessources":
			result =  listDHTRessouces();
			break;
		}
		return result;
	}
	public P2PMessage fetchFromDHT(){
		return null;
	}
	/**
	 * Get Ressource list from Client, store those list as value in a Hashmap, which key is "IP@Port".
	 * 
	 * @return result Response P2PMessage to Client
	 */
	public P2PMessage updateDHT(){
		String filenames = new String(payload);
		List<String> list = new ArrayList<>();
		String[] filenamelist = filenames.split("\n");
		System.out.println("filename:");
		for (String string : filenamelist) {
			list.add(string);
		}
		GHTlist.put(information, list);
		Set<String> setKey = GHTlist.keySet();
		Iterator<String> iterator = setKey.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			System.out.println("adress = "+key);
			List<String> flist = new ArrayList<>();
			flist = GHTlist.get(key);
			for (String string : flist) {
				System.out.println(string);
			}
		}
		P2PMessage response = new P2PMessage();
		response.setControl("Server response");
		response.setInformation("AddRessource is successful");
		response.setPayload("Server-PAYLOAD".getBytes());
		return response;
	}
	
	public P2PMessage detachDHT(){
		
		
		
		return null;
	}
	public P2PMessage listDHTRessouces(){
		return null;
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}
	
}
