package models;

public class Couch {
	
	private String brand;
	private String couchType;
	private String couchColor;
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public String getCouchType() {
		return couchType;
	}
	public void setCouchType(String couchType) {
		this.couchType = couchType;
	}
	
	public String getCouchColor() {
		return couchColor;
	}
	public void setCouchColor(String couchColor) {
		this.couchColor = couchColor;
	}
	
	@Override
	public String toString() {
		return ("\t\tbrand: " + brand + "\n\t\tcouchType: " + couchType + "\n\t\tcouchColor: " + couchColor + "\n");
	}

}
