package at.kv.p2p.com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import at.kv.p2p.com.P2PMessage;

public class P2PComClient {

	private String serverAddress = "";
	private int serverPort = 0;
	
	public P2PComClient(String serverAddress, int serverPort){
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}
	
	/**
	 * Sends a message to the server. 
	 * @param msg response of the server
	 * @return
	 */
	public P2PMessage send(P2PMessage msg){
		
		Socket socket = null;
		
		try{
			socket = new Socket(serverAddress, serverPort);
		}catch (IOException e){
			System.err.println("Could not connect to server [" + serverAddress + ":" + serverPort + "]!");
			return null;
		}
		
		try {
			return sendAndReceive(socket,msg);
		} catch (IOException e) {
			System.err.println("Handshake failed!");
		}
		
		return null;
		
	}
	
	private P2PMessage sendAndReceive(Socket socket, P2PMessage requestMessage) throws IOException{
		
		InputStream is = socket.getInputStream();		
		OutputStream os = socket.getOutputStream();
		
		os.write(requestMessage.toBytes());
		os.flush();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		String line = "";
		P2PMessage msg = new P2PMessage();				
		int payloadLength = -1;
		while((line = br.readLine()) != null){
			if(line.startsWith(P2PMessage.ENTRIE_CONTROL)){
				msg.setControl(getEntrieValue(line));
			}else if(line.startsWith(P2PMessage.ENTRIE_INFORMATION)){
				msg.setInformation(getEntrieValue(line));
			}else if(line.startsWith(P2PMessage.ENTRIE_PAYLOAD_LENGTH)){
				payloadLength = Integer.parseInt(getEntrieValue(line));
				break;
			}
		}
		
		
		if(payloadLength != -1){
			char[] payloadBuffer = new char[payloadLength];
			
			br.read(payloadBuffer,0, payloadBuffer.length);
			byte[] payloadBufferByte = new byte[payloadLength];
			for(int i = 0; i < payloadLength; i++){
				payloadBufferByte[i] = (byte)payloadBuffer[i];
			}
			msg.setPayload(payloadBufferByte);
		}		
		
		os.close();
		is.close();
		socket.close();		
		return msg;
	}
	
	private String getEntrieValue(String entrie){
		
		int sepPos = entrie.indexOf(P2PMessage.ENTRIE_SEPERATOR);
		if(sepPos != -1){
			return entrie.substring(sepPos+1);
		}
		return "";
	}
	
	
	
	
}
