package calculateSlippage.digitalAssetTrading.dtos;

public class TradeData {
	private double size;
	private double price;
	private String side;

	public void setOrder(double size, double price, String side) {
		this.size = size;
		this.price = price;
		this.side = side;
	}
	
	
	public double getSize() {
		return size;
	}


	public void setSize(double size) {
		this.size = size;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
}
