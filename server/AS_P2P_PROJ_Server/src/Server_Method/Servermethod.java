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
	//<Resource IP>\n<Resource port>\n<id>\n<name>\n<size>\n<date>\n<type>
	String IPundport = "" ;
	String datainfo = "";
	String id = ""; 
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
		case "DetachResources":
			splitpayload(false);
			result =  detachDHT();
			break;
		case "ListResource":
			result =  listDHTRessouces();
			break;
		case "registerResource":
			splitpayload(true);
			result = registerResource();
			break;
		case "removeResource":
			splitpayload(false);
			result = removeResource();
			break;
		case "getResources":
			result = getResources();
			break;
		}
		return result;
	}
	
	public void splitpayload(boolean is_re){
		if (is_re) {
			String filename = new String(payload);
			String[] pay =  filename.split("\n");
			this.IPundport = pay[0]+"$"+pay[1]+"$";
			this.id  = pay[2];
			this.datainfo = pay[2]+"$"+pay[3]+"$"+pay[4]+"$"+pay[5]+"$"+pay[6]+"\n";
		}else{
			String filename = new String(payload);
			String[] pay =  filename.split("\n");
			this.IPundport = pay[0]+"$"+pay[1]+"$";
			this.id  = pay[2];
		}
	}
	
	public P2PMessage registerResource(){
		Set<String> setKey = GHTlist.keySet();
		if (setKey.isEmpty()) {
			List<String> list = new ArrayList<>();
			list.add(datainfo);
			GHTlist.put(IPundport, list);
		}else{
			Iterator<String> iterator = setKey.iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				List<String> flist = new ArrayList<>();
				flist = GHTlist.get(key);
				if (key.equals(IPundport)) {
					if (flist.contains(datainfo)) {
						return  notfind();
					}else{
						flist.add(datainfo);
						GHTlist.put(IPundport, flist);
						return  re();
					}
				}
			}
			List<String> list = new ArrayList<>();
			list.add(datainfo);
			GHTlist.put(IPundport, list);
		}
		return  re();
	}
	
	public P2PMessage removeResource(){
		Set<String> setKey = GHTlist.keySet();
		if (setKey.isEmpty()) {
			return notfind();
		}else{
			Iterator<String> iterator = setKey.iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				List<String> flist = new ArrayList<>();
				flist = GHTlist.get(key);
				if (key.equals(IPundport)) {
					for (int i = 0; i < flist.size(); i++) {
						String o = flist.get(i);
						System.out.println("o ="+o);
						String sid = (String) o.subSequence(0, 1);
						System.out.println("sid = "+sid);
						if (sid.equals(id)) {
							flist.remove(i);
							GHTlist.put(key, flist);
						}
					}
					if (GHTlist.get(key).isEmpty()) {
						GHTlist.remove(key);
					}
					return re();
				}
			}
		}
		return notfind();
	}
	
	public P2PMessage getResources(){
		Set<String> setKey = GHTlist.keySet();
		String filelist = "";
		if (setKey.isEmpty()) {
			return notfind();
		}else{
			Iterator<String> iterator = setKey.iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				List<String> flist = new ArrayList<>();
				flist = GHTlist.get(key);
				for (String string : flist) {
					filelist = filelist+ key+string;
				}
			}
		}
		return relist(filelist);
	}
	
	/**
	 * If OK, Server return this Message to Client
	 * 
	 * 
	 * @return result Response P2PMessage to Client
	 */
	public P2PMessage re(){
		printHM();
		P2PMessage response = new P2PMessage();
		response.setControl("Server response OK");
		response.setInformation(" ");
		response.setPayload("ServerPAYLOAD".getBytes());
		return response;
	}
	

	/**
	 * return filelist, Server return this Message to Client
	 * 
	 * 
	 * @return result Response P2PMessage to Client
	 */
	public P2PMessage relist(String i){
		printHM();
		P2PMessage response = new P2PMessage();
		response.setControl("Server response OK");
		response.setInformation(" ");
		response.setPayload(i.getBytes());
		return response;
	}

	/**
	 * If failed, Server return this Message to Client
	 * 
	 * 
	 * @return result Response P2PMessage to Client
	 */
	public P2PMessage notfind(){
		P2PMessage response = new P2PMessage();
		response.setControl("Server response failed");
		response.setInformation(" ");
		response.setPayload("ServerPAYLOAD".getBytes());
		return response;
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
		if (GHTlist.containsKey(IPundport)) {
			GHTlist.remove(IPundport);
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