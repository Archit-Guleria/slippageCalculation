package calculateSlippage.digitalAssetTrading.dtos;

public class OrderBookUpdate {
	private String side;
	private Double price;
	private Double size;
	
	public void setChanges(String side, Double price, Double size) {
		this.side = side;
		this.price = price;
		this.size = size;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	
}
