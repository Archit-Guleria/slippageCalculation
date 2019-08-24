package calculateSlippage.digitalAssetTrading;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import calculateSlippage.digitalAssetTrading.dtos.OrderBookUpdate;
import calculateSlippage.digitalAssetTrading.dtos.TradeData;



public class WebSocketClientEndPoint extends WebSocketClient {
	String file;
	public WebSocketClientEndPoint(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public WebSocketClientEndPoint(String file,URI serverURI) {
		super(serverURI);
		this.file=file;

		
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		
		JSONParser jsonParser = new JSONParser();
		
		try {
		  FileReader reader = new FileReader("subscribeFile/"+this.file);
	      Object obj;
	      obj = jsonParser.parse(reader);
	      JSONObject json = (JSONObject) obj;
		  String subscribeMessage = json.toString();
		  send(subscribeMessage);
		   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		System.out.println("new websocket connection opened  ");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code + " additional info: " + reason);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(String message) {
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject) parser.parse(message);
			if(json.get("type").equals("snapshot"))
				{
				ArrayList<ArrayList<String>> bidsList =  (ArrayList<ArrayList<String>>) json.get("bids");
				ArrayList<ArrayList<String>> asksList =  (ArrayList<ArrayList<String>>) json.get("asks");
				for(ArrayList<String> al:bidsList)
				{
					App.orderBook.getBids().put(Double.valueOf(al.get(0)),Double.valueOf(al.get(1)));
				}
				
				for(ArrayList<String> al:asksList)
				{
					App.orderBook.getAsks().put(Double.valueOf(al.get(0)), Double.valueOf(al.get(1)));
				
				}
				}
			else if(json.get("type").equals("l2update")) {
				ArrayList<ArrayList<String>> changes = (ArrayList<ArrayList<String>>) json.get("changes") ;
				
				for(ArrayList<String> al : changes) {
					OrderBookUpdate orderBookUpdate = new OrderBookUpdate();
					orderBookUpdate.setChanges(al.get(0), Double.valueOf(al.get(1)), Double.valueOf(al.get(2)));
					
					 synchronized(App.orderBookUpdateList) 
				        { 
						 App.orderBookUpdateList.add(orderBookUpdate);
				        }
				}
			}
			
			else if(json.get("type").equals("match")) {
				TradeData tradeData = new TradeData();
				String size = (String) json.get("size");
				String price = (String) json.get("price");
				tradeData.setOrder(Double.valueOf(size),Double.valueOf(price),(String) json.get("side"));
				
				synchronized(App.tradeDataList) 
		        { 
					App.tradeDataList.add(tradeData);
		        }

			}
			
			
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		}

	@Override
	public void onMessage(ByteBuffer message) {
		System.out.println("received ByteBuffer");
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("an error occurred:" + ex);
	}
}


