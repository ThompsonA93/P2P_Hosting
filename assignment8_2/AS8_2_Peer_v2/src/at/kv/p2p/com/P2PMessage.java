package at.kv.p2p.com;

public class P2PMessage {
	public static final String ENTRIE_SEPERATOR = ":";
	
	public static final String ENTRIE_INFORMATION = "Info";
	public static final String ENTRIE_CONTROL = "Ctrl";
	public static final String ENTRIE_PAYLOAD_LENGTH = "PayloadLength";
	
	private String ctrl = "";
	private String information = "";	
	private byte[] payload = new byte[0];
	
	public P2PMessage(){
		
	}
	
	public P2PMessage(String ctrl, String information, byte[] payload){
		this.ctrl = ctrl;
		this.information = information;
		this.payload = payload;
	}
	
	public void setPayload(byte[] payload){
		this.payload = payload;
	}
	
	public byte[] getPayload(){
		return payload;
	}
	
	public int getPayloadLength(){
		return payload.length;
	}
	
	public void setInformation(String information){
		this.information = information;
	}
	
	public String getInformation(){
		return information;
	}
	
	public void setControl(String control){
		ctrl = control;
	}
	
	public String getControl(){
		return ctrl;
	}
	
	public byte[] toBytes(){
		StringBuilder sb = new StringBuilder();
		sb.append(ENTRIE_CONTROL + ENTRIE_SEPERATOR + ctrl + "\n");
		sb.append(ENTRIE_INFORMATION + ENTRIE_SEPERATOR + information + "\n");
		sb.append(ENTRIE_PAYLOAD_LENGTH + ENTRIE_SEPERATOR + String.valueOf(payload.length)+"\n");
		byte[] byteArray = new byte[sb.length() + payload.length];
		System.arraycopy(sb.toString().getBytes(), 0, byteArray, 0, sb.toString().getBytes().length);
		System.arraycopy(payload, 0, byteArray,sb.toString().getBytes().length , payload.length);
		return byteArray;
	}
	

}
