package Server_Method;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import at.kv.p2p.com.P2PMessage;

public class Servermethod {

	private static Servermethod instance = null;
	P2PMessage message;
	static HashMap<String, List> GHTlist; // Adress:port as key, ressource list as value 
	String command;
	String information;
	byte[] payload;
	// Singleton all Information of Server will be stored here
	public static Servermethod getInstance(){
		if(instance == null){
			instance = new Servermethod();
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
	public Servermethod(){
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
		case "AddResource":
			result =  updateDHT();
			break;
		case "GetResource":
			result =  fetchFromDHT();
			break;
		case "DetachResource":
			result =  detachDHT();
			break;
		case "ListResource":
			result =  listDHTRessouces();
			break;
		}
		return result;
	}
	
	/**
	 * Return resource and IP@Port, which client wants. If it dose not have, return notfound message
	 * 
	 * @return result Response P2PMessage to Client
	 */
	public P2PMessage fetchFromDHT(){
		String filename = new String(payload);
		Set<String> setKey = GHTlist.keySet();
		Iterator<String> iterator = setKey.iterator();
		P2PMessage response = new P2PMessage();
		while(iterator.hasNext()){
			String key = iterator.next();
			System.out.println("adress = "+key);
			List<String> flist = new ArrayList<>();
			flist = GHTlist.get(key);
			for (String string : flist) {
				if (filename.equals(string)) {
					response.setControl("Server response OK");
					response.setInformation("GetResource is successful");
					String payl = string+"#"+key;
					response.setPayload(payl.getBytes());
					return response;
				}
			}
		}
		
		return notfind();
		
	}
	
	/**
	 * If not found resource, Server return this Message to Client
	 * 
	 * 
	 * @return result Response P2PMessage to Client
	 */
	public P2PMessage notfind(){
		P2PMessage response = new P2PMessage();
		response.setControl("Server response NO");
		response.setInformation("Resource not found");
		response.setPayload("ServerPAYLOAD".getBytes());
		return response;
	}
	/**
	 * Get resource list from Client, stores those list as value in a Hashmap, which key is "IP@Port".
	 * If same client second time calls this method, it will check, if DHT contains the second resource than do nothing,
	 * if not contains, adds it to DHT
	 * 
	 * 
	 * @return result Response P2PMessage to Client
	 */
	public P2PMessage updateDHT(){
		String filenames = new String(payload);
		List<String> list = new ArrayList<>();
		String[] filenamelist = filenames.split("\n");
		for (String string : filenamelist) {
			list.add(string);
		}
		
		Set<String> setKey = GHTlist.keySet();
		if (setKey.isEmpty()) {
			GHTlist.put(information, list);
		}
		Iterator<String> iterator = setKey.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			List<String> flist = new ArrayList<>();
			flist = GHTlist.get(key);
			if (key.equals(information)) {
				for (String string1 : filenamelist) {
					if (flist.contains(string1)) {
						break;
					}else{
						flist.add(string1);
					}
				}
			}else{
				GHTlist.put(information, list);
			}
		}
		printHM();
		P2PMessage response = new P2PMessage();
		response.setControl("Server response OK");
		response.setInformation("AddRessource is successful");
		response.setPayload("ServerPAYLOAD".getBytes());
		return response;
	}
	/**
	 *Logout the client, delete all flies, which belong to this client.
	 * 
	 * 
	 * @return result Response P2PMessage to Client
	 */
	public P2PMessage detachDHT(){
		if (GHTlist.containsKey(information)) {
			GHTlist.remove(information);
			printHM();
			P2PMessage response = new P2PMessage();
			response.setControl("Server response OK");
			response.setInformation("DetachResource is successful");
			response.setPayload("ServerPAYLOAD".getBytes());
			return response;
		}else{
			return notfind();
		}
	}
	
	/**
	 *Give client a list of all active Files with IP
	 * 
	 * 
	 * @return result Response P2PMessage to Client
	 */
	public P2PMessage listDHTRessouces(){
		String filelist = "";
		Set<String> setKey = GHTlist.keySet();
		Iterator<String> iterator = setKey.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			List<String> flist = new ArrayList<>();
			flist = GHTlist.get(key);
			for (String string : flist) {
				filelist = filelist+string+"#"+key+"\n";
			}
		}
		P2PMessage response = new P2PMessage();
		response.setControl("Server response OK");
		response.setInformation("ListResource is successful");
		response.setPayload(filelist.getBytes());
		
		return response;
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
	
	/**
	 * print HDT, to test
	 */
	public void printHM(){
		System.out.println("GHTlist is:");
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
	}
	
}