package at.kv.p2p.seeder.logic;

import java.util.ArrayList;
import java.util.List;

import at.kv.p2p.com.P2PMessage;
import at.kv.p2p.com.client.P2PComClient;
import at.kv.p2p.seeder.SeederProperties;

public class P2PSharingManager {

	private static final String CMD_REGISTER_RESOURCE = "registerResource";
	private static final String CMD_REMOVE_RESOURCE = "removeResource";
	private static final String CMD_FAILED = "failed";
	
	private static int idCounter = 1;
	
	private static List<P2PFile> files = new ArrayList<>();	
	
	public static int getFreeID(){
		int id = idCounter;
		idCounter++;
		return id;
	}
	
	public static boolean addFile(P2PFile file){
		Logger.write("[P2PSharingManager] try to add file ["+ file.getName() + "] with id [" + file.getId()+"]");
		if(allreadyExists(file)){
			Logger.write("[P2PSharingManager] file ["+file.getName()+"] already exists!");
			return false;
		}
		
		if(!registerResource(file)){
			Logger.write("[P2PSharingManager] could not register file [" + file.getName() + "]");
			return false;
		}
		
		files.add(file);
		Logger.write("[P2PSharingManager] file ["+file.getName()+"] successfully registered and added!");
		return true;
		
	}
	
	public static P2PFile getFileByID(int id){
		
		int index = getIndexOf(id);
		if(index != -1){
			return files.get(index);
		}
		return null;
	}
	
	public static boolean removeFile(int id){
		Logger.write("[P2PSharingManager] try to remove file with id [" + id +"]");
		int index = getIndexOf(id);
		
		if(index != -1){		
			
			if(!removeResource(id)){
				Logger.write("[P2PSharingManager] could not remove file with id [" + id +"] from central server!");
				return false;
			}
			
			files.remove(index);	
			Logger.write("[P2PSharingManager] successfull removed file with id [" + id +"]");
			return true;
		}		
		
		Logger.write("[P2PSharingManager] no file with id [" + id +"]");
		return false;
		
	}
	
	public static void removeAllFiles(){
		Logger.write("[P2PSharingManager] removing all files!");
		for(P2PFile f : files){
			removeFile(f.getId());
		}
	}
	
	private static int getIndexOf(int id){
		
		int index = 0;
		
		for(P2PFile f : files){
			if(f.getId() == id){
				return index;
			}
			index++;
		}
		
		return -1;
	}
	
	private static boolean allreadyExists(P2PFile file){
				
		for(P2PFile f : files){			
			if(f.getPath().equals(file.getPath())){
				return true;
			}			
		}
		
		return false;
		
	}
	
	private static boolean registerResource(P2PFile file){
		P2PComClient centralServer = new P2PComClient(SeederProperties.CENTRAL_SERVER_ADDRESS,SeederProperties.CENTRAL_SERVER_PORT);
		P2PMessage request = new P2PMessage();
		request.setControl(CMD_REGISTER_RESOURCE);
		String payload = "";
		payload += SeederProperties.OWN_SERVER_ADDRESS + "\n";
		payload += String.valueOf(SeederProperties.OWN_SERVER_PORT) + "\n";
		payload += String.valueOf(file.getId()) + "\n";
		payload += file.getName() + "\n";
		payload += String.valueOf(file.getSize()) + "\n";
		payload += file.getDate() + "\n";
		payload += file.getType();
		request.setPayload(payload.getBytes());
		P2PMessage response = centralServer.send(request);
		
		if(response == null){
			return false;
		}
		
		if(response.getControl().equals(CMD_FAILED)){
			return false;
		}
		
		return true;		
	}
	
	private static boolean removeResource(int id){
		P2PComClient centralServer = new P2PComClient(SeederProperties.CENTRAL_SERVER_ADDRESS,SeederProperties.CENTRAL_SERVER_PORT);
		P2PMessage request = new P2PMessage();
		request.setControl(CMD_REMOVE_RESOURCE);
		String payload = "";
		payload += SeederProperties.OWN_SERVER_ADDRESS + "\n";
		payload += String.valueOf(SeederProperties.OWN_SERVER_PORT) + "\n";
		payload += String.valueOf(id);
		request.setPayload(payload.getBytes());
		
		P2PMessage response = centralServer.send(request);
		
		if(response == null){
			return false;
		}
		
		if(response.getControl().equals(CMD_FAILED)){
			return false;
		}
		
		return true;		
	}
	
}
