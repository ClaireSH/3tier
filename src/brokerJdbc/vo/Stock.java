package brokerJdbc.vo;

import java.io.Serializable;

public class Stock implements Serializable {
	private String symbol;
	private String price;
	public Stock(String symbol, String price) {
		super();
		this.symbol = symbol;
		this.price = price;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return symbol + " "+price;
	}
	
	
}
