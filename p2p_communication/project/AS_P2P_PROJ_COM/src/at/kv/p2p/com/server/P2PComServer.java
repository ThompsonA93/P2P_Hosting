package at.kv.p2p.com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


import at.kv.p2p.com.P2PMessage;


public class P2PComServer implements Runnable{
	
	private int port;
	private boolean run = false;
	private Thread thread = null;
	private ServerSocket serverSocket = null;
		
	public P2PComServer(int port){
		this.port = port;
	}	
	
	
	/**
	 * Starts the server on the before declared port.
	 */
	public void start(){
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * Stop the server.
	 */
	public void stop(){
		
		if(thread != null && serverSocket != null){
			
			run = false;
			try {
				serverSocket.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * NEVER CALL THIS.
	 */
	@Override
	public void run() {
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {			
			System.err.println("Could not open server socket!");
			return;
		}
		run = true;
		while(run){
			
			try {
				
				Socket socket = serverSocket.accept();
				processConnection(socket);		
				
				
			} catch (IOException e) {
				
				//to avoid exception by closing the server socket
				if(!serverSocket.isClosed()){
					e.printStackTrace();
				}
			}
			
			
		}		
		
	}
	
	/**
	 * Interprets received data, check for handlers and sends response
	 * @param socket
	 * @throws IOException
	 */
	private void processConnection(Socket socket) throws IOException{
				
		InputStream is = socket.getInputStream();		
		OutputStream os = socket.getOutputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		//collect bytes
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
		
		//invoke command
		P2PMessage responseMsg = P2PComServerCtrlCmdManager.getInstance().processCommands(msg);
		//send response
		if(responseMsg != null){
			os.write(responseMsg.toBytes());
			os.flush();
		}
		
		//close everything
		os.close();
		is.close();
		socket.close();		
	}
	
	private String getEntrieValue(String entrie){
		
		int sepPos = entrie.indexOf(P2PMessage.ENTRIE_SEPERATOR);
		if(sepPos != -1){
			return entrie.substring(sepPos+1);
		}
		return "";
	}
	
}
