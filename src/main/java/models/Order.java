package models;

import java.util.List;

public class Order {
	
	private String date;
	private List<Couch> couches;
	private double price;
	private double tax;
	private double total;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public List<Couch> getCouches() {
		return couches;
	}
	public void setCouch(List<Couch> couches) {
		this.couches = couches;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
	@Override
	public String toString() {
		return ("{date: " + date + ", couches: " + couches + ", price: " + price + ", tax: " + tax + ", total: " + total+"}\n");
	}

}
