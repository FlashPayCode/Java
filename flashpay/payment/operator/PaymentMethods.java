package flashpay.payment.operator;

public enum PaymentMethods {
	
	Credit(1,"Credit"),
	Union(2,"Union");
	
	private int vule;
	
    private String payment;
	
	
    PaymentMethods(int vule, String payment) {
		this.vule=vule;
		this.payment=payment;
	}


	public int getVule() {
		return vule;
	}


	public void setVule(int vule) {
		this.vule = vule;
	}


	public String getPayment() {
		return payment;
	}


	public void setPayment(String payment) {
		this.payment = payment;
	}


}
