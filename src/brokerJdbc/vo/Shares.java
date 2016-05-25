package brokerJdbc.vo;

import java.io.Serializable;

public class Shares implements Serializable {
	private String ssn;
	private String symbol;
	private String quantity;
	public Shares(String ssn, String symbol, String quantity) {
		super();
		this.ssn = ssn;
		this.symbol = symbol;
		this.quantity = quantity;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return symbol + " " + quantity ;
	}
	
	
}
