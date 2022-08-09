package flashpay.payment.to;

public class OrderItem {
	
 
	private String name;

	private double price;

	private String unit;

	private int  quantity;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	} 
	
	public String getOrderItemStr() {
		return this.name +" "+ String.format("%.2f",this.price) +" X "+ String.valueOf(this.quantity) +" "+this.unit;
	}

		
}
