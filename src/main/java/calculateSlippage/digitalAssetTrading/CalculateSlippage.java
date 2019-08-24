package calculateSlippage.digitalAssetTrading;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import calculateSlippage.digitalAssetTrading.dtos.OrderBook;
import calculateSlippage.digitalAssetTrading.dtos.OrderBookUpdate;
import calculateSlippage.digitalAssetTrading.dtos.TradeData;

class CalculateSlippage extends TimerTask {
	static int t=0;
		
		public void updateOrderBook() {
			 List<OrderBookUpdate> OBUpdates = new ArrayList<>();
		      
		       synchronized(App.orderBookUpdateList) 
		        { 
		    	   OBUpdates.addAll(App.orderBookUpdateList);
			       App.orderBookUpdateList.removeAll(OBUpdates);
		        }
		       
		       for(OrderBookUpdate update: OBUpdates) {
		    	   if(update.getSide().equals("sell")) {
		    		   App.orderBook.getAsks().put(update.getPrice(), update.getSize());
		    	   }
		    	   else if(update.getSide().equals("buy")) {
		        		   App.orderBook.getBids().put(update.getPrice(), update.getSize());
		        	   }
		       }
		}
	
		public double calculateExpectedPrice(double size, String side){
			
			OrderBook orderBook = App.orderBook;
			
			double totalPrice = 0;
	    	double totalSize = 0;
	    	
	    	if(size > 0 )
	    	{
				while(size > 0) {
					Map.Entry<Double,Double> me = null;
					if(side.equals("sell"))
					{
						me = orderBook.getAsks().firstEntry();
	    		    }else {
	    		    	me = orderBook.getBids().lastEntry();
	    		    }
					
		    		if(me.getValue()<size) {
		    			size = size - me.getValue();
		    			totalSize = totalSize + me.getValue();
		    			totalPrice = totalPrice + (me.getKey() * me.getValue());
		    		}
		    		else {
		    			totalSize = totalSize + size;
		    			totalPrice = totalPrice + (me.getKey() * size);
		    			size=0;
		    		}
		    	}
				return (totalPrice/totalSize);
	    	}
	    	else 
	    	{
	    		return 0;
	    	}
		}
		
		public void calculateSlippage(double expectedPrice,double executedPrice, String side) {
			if(expectedPrice>0) {
				double slippage = (Math.abs(expectedPrice-executedPrice) * 100) / expectedPrice;
				System.out.printf("Slippage for "+side+" is : %f\n",slippage);
				
			}
		}
		
	    public void run() {

	    	
	    	if(!(App.orderBook.getAsks().isEmpty())) {
	        	
		    	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		    	LocalDateTime now = LocalDateTime.now();  
		    	System.out.println("Time "+dtf.format(now)+" : \n"); 
		    	
		    	
		    	List<TradeData> tradeDataList = new ArrayList<>();
	        	
	        	 synchronized(App.tradeDataList) 
			        { 
	        		 tradeDataList.addAll(App.tradeDataList);
	        		 App.tradeDataList.removeAll(tradeDataList);
			        }
		    	
	    		if(tradeDataList.isEmpty()) {
	    			
	    			System.out.println("No trade data received");
	    			
	    		}
	    		else 
	    		{	
		        	double sumForSell = 0;
		        	double sumForBuy = 0;
		        	double totalSells = 0;
		        	double totalBuys = 0;
		        	for(TradeData data: tradeDataList) {
		        		if(data.getSide().equals("buy"))
		        		{
		        			totalBuys = totalBuys + data.getSize();
		        			sumForBuy = sumForBuy + (data.getPrice()* data.getSize());
		        		}
		        		else if(data.getSide().equals("sell"))
		        		{
		        			totalSells = totalSells + data.getSize();
		        			sumForSell = sumForSell + (data.getPrice()* data.getSize());
		        		}
		        	}
		    		

			    	double executedPriceForSell = sumForSell/totalSells;
			    	double executedPriceForBuy = sumForBuy/totalBuys;
			    	
			    	double expectedPriceForSell = calculateExpectedPrice(totalSells,"sell");
			    	double expectedPriceForBuy = calculateExpectedPrice(totalBuys,"buy");
	
			    	
			    	calculateSlippage(expectedPriceForSell,executedPriceForSell,"sell");
			    	calculateSlippage(expectedPriceForBuy,executedPriceForBuy,"buy");
			    	

		    		updateOrderBook();
		    	
	    	}
	    		System.out.println("\n\n"); 
	    }
    }
}
