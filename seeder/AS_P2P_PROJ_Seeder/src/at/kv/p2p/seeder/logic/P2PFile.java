package at.kv.p2p.seeder.logic;

public class P2PFile {
	

	private int id;
	private String name = "";
	private String path = "";
	private int size = 0;
	private String date = "";
	private String type = "";
	
	public P2PFile(String name, String path, int size, String date, String type){
		this.id = P2PSharingManager.getFreeID();
		this.name = name;
		this.path = path;
		this.size = size;
		this.date = date;
		this.type = type;
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
	
	public String getPath(){
		return path;
	}
	
	public void setPath(String path){
		this.path = path;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
