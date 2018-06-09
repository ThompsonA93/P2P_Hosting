package at.kv.p2p.seeder.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import at.kv.p2p.com.P2PMessage;
import at.kv.p2p.com.server.P2PComServerControlCommand;


public class ServerCmdDownloadResource extends P2PComServerControlCommand{

	private static final String CMD = "downloadResource";
	private static final String CMD_OK = "ok";
	private static final String CMD_FAILED = "failed";
	
	public ServerCmdDownloadResource() {
		super(CMD);
	}

	@Override
	public void processRequest(P2PMessage request, P2PMessage response) {
		
		Logger.write("[ServerCmdDownloadResource] called!");
		Logger.write("[ServerCmdDownloadResource] try to parse ID");
		int id = Integer.parseInt(new String(request.getPayload()));
		Logger.write("[ServerCmdDownloadResource] id = ["+id+"]");
		
		
		P2PFile f = P2PSharingManager.getFileByID(id);
		if(f == null){
			Logger.write("[ServerCmdDownloadResource] id is no valid file");
			response.setControl(CMD_FAILED);
			return;
		}
		Logger.write("[ServerCmdDownloadResource] id is a valid file ["+f.getName()+"]");
		
		try {
			response.setPayload(Base64.getEncoder().encode(Files.readAllBytes(new File(f.getPath()).toPath())));	
			response.setControl(CMD_OK);
		} catch (IOException e) {
			response.setControl(CMD_FAILED);
		}
		
		
	}

}
