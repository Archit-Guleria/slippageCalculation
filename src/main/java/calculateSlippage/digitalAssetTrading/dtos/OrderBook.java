package calculateSlippage.digitalAssetTrading.dtos;

import java.util.TreeMap;

public class OrderBook {
	private TreeMap<Double, Double> bids;
	private TreeMap<Double, Double> asks;
	
	public OrderBook(){
		bids = new TreeMap<Double,Double>();
		asks = new TreeMap<Double,Double>();
	}


	public TreeMap<Double, Double> getBids() {
		return bids;
	}



	public void setBids(TreeMap<Double, Double> bids) {
		this.bids = bids;
	}



	public TreeMap<Double, Double> getAsks() {
		return asks;
	}



	public void setAsks(TreeMap<Double, Double> asks) {
		this.asks = asks;
	}

	
}
