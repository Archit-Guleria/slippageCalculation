package calculateSlippage.digitalAssetTrading;

import java.net.URI;
import java.net.URISyntaxException;



public class MyThread implements Runnable {
	Thread t;
	String fileName;
	
	MyThread(String name, String fileName){
	t= new Thread(this,name);
	System.out.println("Added my thread "+name);
	this.fileName = fileName;
	t.start();
	}
	
	public void run() {
		
		 try {
			 
			 WebSocketClientEndPoint client = new WebSocketClientEndPoint(fileName,new URI("wss://ws-feed.pro.coinbase.com"));
			 client.connect();
			 
		 } catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}
