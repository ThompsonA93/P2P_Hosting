package at.kv.p2p.com.server;

import java.util.ArrayList;
import java.util.List;

import at.kv.p2p.com.P2PMessage;

public class P2PComServerCtrlCmdManager {
	
	private static P2PComServerCtrlCmdManager instance = null;
	
	private List<P2PComServerControlCommand> cmds = new ArrayList<>();
	
	private P2PComServerCtrlCmdManager(){
		
	}
	
	public static P2PComServerCtrlCmdManager getInstance(){
		if(instance == null){
			instance = new P2PComServerCtrlCmdManager();
		}
		return instance;
	}
	
	public boolean addControlCommand(P2PComServerControlCommand cmd){
		
		//check if cmd already exists?
		for(P2PComServerControlCommand c : cmds){
			if(c.getControlCmd().equals(cmd.getControlCmd())){
				return false;
			}
		}
		
		cmds.add(cmd);		
		return true;
	}
	
	public boolean removeControlCommad(P2PComServerControlCommand cmd){
		return cmds.remove(cmd);
	}
	
	public P2PMessage processCommands(P2PMessage msg){
		
		for(P2PComServerControlCommand c : cmds){
			if(c.getControlCmd().equals(msg.getControl())){
				P2PMessage responseMsg = new P2PMessage();
				c.processRequest(msg, responseMsg);
				return responseMsg;
			}
		}
		
		return null;
	}
	
	

}
