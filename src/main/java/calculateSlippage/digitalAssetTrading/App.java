package calculateSlippage.digitalAssetTrading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import calculateSlippage.digitalAssetTrading.dtos.OrderBook;
import calculateSlippage.digitalAssetTrading.dtos.OrderBookUpdate;
import calculateSlippage.digitalAssetTrading.dtos.TradeData;



public class App 
{

	public static OrderBook orderBook;
	public static List<OrderBookUpdate> orderBookUpdateList; 
	public static List<TradeData> tradeDataList;
	
	
    public static void main( String[] args )
    {
    	orderBook = new OrderBook();
		orderBookUpdateList =  Collections.synchronizedList(new ArrayList<>());
		tradeDataList =  Collections.synchronizedList(new ArrayList<>());
		
		new MyThread("getOrderBookAndTrades","subscribe.json");

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new CalculateSlippage(), 3000, 1000);
    }
}
