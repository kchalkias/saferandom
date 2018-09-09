package stocks;

public class Stock {
	
	private String symbol;
	private String price;
	private String day;
	public Stock(String symbol, String price, String day) {
		super();
		this.symbol = symbol;
		this.price = price;
		this.day = day;
	}
	public String getSymbol() {
		return symbol;
	}
	public String getPrice() {
		return price;
	}
	public String getDay() {
		return day;
	}
	@Override
	public String toString() {
		return "Stock [symbol=" + symbol + ", price=" + price + ", day=" + day
				+ "]";
	}
}
