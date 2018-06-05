package at.kv.p2p.seeder.logic;

public class P2PFile {
	
	private String ip;
	private int port;
	private int id;
	private String name = "";
	private int size = 0;
	private String date = "";
	
	
	public P2PFile(String ip, int port, int size){
		this.ip = ip;
		this.port = port;
		this.size = size;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getSize() {
		return size;
	}


	public void setSize(int size) {
		this.size = size;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}
	
	

}
