package flashpay.payment.to;

public class OrderItem {
	
	//�W��
	private String name;
	//����
	private double price;
	//���
	private String unit;
	//�ƶq
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
		return this.name +" "+ String.format("%f",this.price) +" X "+ this.quantity +" "+this.unit;
	}

		
}
