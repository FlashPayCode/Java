package flashpay.payment.operator;

public enum PaymentMethodsItem {
	
	Credit_all(0,"Credit_all"),
    Credit_3(3,"Credit_3"),
    Credit_6(6,"Credit_6"), 
    Credit_12(12,"Credit_12"),
    Credit_18(18,"Credit_18"),
    Credit_24(24,"Credit_24"),
	Union_all(0,"Union_all");
   // other(-1,"other");

	private int vule;
	
    private String paymentItem;
	
	

	PaymentMethodsItem(int vule, String paymentItem) {
		this.vule=vule;
		this.paymentItem=paymentItem;
	}


	public int getVule() {
		return vule;
	}

	public void setVule(int vule) {
		this.vule = vule;
	}


	public String getPaymentItem() {
		return paymentItem;
	}


	public void setPaymentItem(String paymentItem) {
		this.paymentItem = paymentItem;
	}








}
