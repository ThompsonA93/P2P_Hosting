package logic;

/**
 * Received from Mainserver and Mainserver only
 * 
 */
public class SeederData {
	private String ip = "";
	private int port = -1;
	private int id = -1;
	private String name = "";
	private int size = -1;
	private String type = "";

	public SeederData(String s) {
		String[] dataSet = s.split("$");
		this.ip = dataSet[0];
		this.port = Integer.parseInt(dataSet[1]);
		this.id = Integer.parseInt(dataSet[2]);
		this.name = dataSet[3];
		this.size = Integer.parseInt(dataSet[4]);
		this.type = dataSet[6];
	}
	
	public SeederData(String ip, int port, int id, String name, int size, String type) {
		this.ip = ip;
		this.port = port;
		this.id = id;
		this.name = name;
		this.size = size;
		this.type = type;
	}	
}
