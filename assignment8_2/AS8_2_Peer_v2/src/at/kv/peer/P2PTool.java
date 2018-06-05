package at.kv.peer;

public class P2PTool {
	
	public static Peer getPeerFromBytes(byte[] bytes){
		try{
			
			String[] p = new String(bytes).split("\n");			
			return new Peer(p[1],Integer.parseInt(p[2]));			
			
		}catch(Exception e){
			System.err.println("Could not convert from bytes to Peer()");
		}
		return null;
	}
	
	public static byte[] getBytesFromPeer(Peer peer){
		return (peer.getAddress() + ":" + peer.getPort() + "\n"+
				peer.getAddress() + "\n"+
				peer.getPort()).getBytes();
	}

}
