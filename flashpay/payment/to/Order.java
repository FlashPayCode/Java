package flashpay.payment.to;

import java.time.LocalDateTime;

import flashpay.payment.operator.FlashPayUtil;
import flashpay.payment.operator.PaymentMethods;
import flashpay.payment.operator.PaymentMethodsItem;

public class Order {
	

	private String  ver =FlashPayUtil.APIVERSION; 

	private String  tx_type="101";

	private String  mer_id;

	private String  ord_no;

	private PaymentMethods pay_type;

	private double  amt=0;

	private String  cur="NTD";

	private String order_desc;

	private PaymentMethodsItem  install_period;

	private String  phone;

	private String  use_redeem="0";

	private String  return_url;

	private String  client_url;

	private LocalDateTime ord_time;

	private  String sto_id;
	
	
	
	public String getVer() {
		return ver;
	}

	public String getTx_type() {
		return tx_type;
	}

	public String getMer_id() {
		return mer_id;
	}
	public String getOrd_no() {
		return ord_no;
	}
	public void setOrd_no(String ord_no) {
		this.ord_no = ord_no;
	}
	public double getAmt() {
		return amt;
	}
	public void setAmt(double amt) {
		this.amt = amt;
	}
	public String getCur() {
		return cur;
	}
	
	public String getOrder_desc() {
		return order_desc;
	}

	public void setOrder_desc(String order_desc) {
		this.order_desc = order_desc;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getReturn_url() {
		return return_url;
	}
	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}
	public String getClient_url() {
		return client_url;
	}
	public void setClient_url(String client_url) {
		this.client_url = client_url;
	}



	public void setCur(String cur) {
		this.cur = cur;
	}



	public LocalDateTime getOrd_time() {
		return ord_time;
	}

	public void setOrd_time(LocalDateTime ord_time) {
		this.ord_time = ord_time;
	}

	public String getSto_id() {
		return sto_id;
	}

	public void setSto_id(String sto_id) {
		this.sto_id = sto_id;
	}

	public PaymentMethods getPay_type() {
		return pay_type;
	}

	public void setPay_type(PaymentMethods pay_type) {
		this.pay_type = pay_type;
	}

	public PaymentMethodsItem getInstall_period() {
		return install_period;
	}

	public void setInstall_period(PaymentMethodsItem install_period) {
		this.install_period = install_period;
	}

	public String getUse_redeem() {
		return use_redeem;
	}

	public void setUse_redeem(String use_redeem) {
		this.use_redeem = use_redeem;
	}



	
	
	
	
}
