package at.kv.peer;

public class Peer {

	private String address = "";
	private int port = 0;
		
	public Peer(String address, int port){
		this.address = address;		
		this.port = port;
	}
	
	public Peer(){
		
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public String getAddress(){
		return address;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public int getPort(){
		return port;
	}
	
	public String getID(){
		return address + ":" + port;
	}
	
	public String toString(){
		return 	"ID = " + address + ":" + port +"\n"+
				"Address = " + address + "\n"+
				"Port = " + port ;
	}
	
	
}
