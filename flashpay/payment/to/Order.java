package flashpay.payment.to;

import flashpay.payment.operator.FlashPayUtil;

public class Order {
	
	//api����
	private String  ver =FlashPayUtil.APIVERSION; 
	//flashpay����N�X
	private String  tx_type="101";
	//���ӥN��
	private String  mer_id;
	//�q�渹�X
	private String  ord_no;
	//������O
	private int pay_typ=1;
	//�`���B
	private double  amt;
	//���O
	private String  cur="NTD";
	//�ӫ~�M��
	private String order_desc;
	//����
	private int  install_period;
	//�s���q��
	private String  phone;
	//�w�]��
	private String  use_redeem="0";
	//������\��^��URL(�Ω�q�檬�A��s) 
	private String  return_url;
	//������\�᭶������URL(�Ω���O�̪�^�ӫ�) 
	private String  client_url;
	
	
	public String getVer() {
		return ver;
	}

	public String getTx_type() {
		return tx_type;
	}

	public String getMer_id() {
		return mer_id;
	}
	public void setMer_id(String mer_id) {
		this.mer_id = mer_id;
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

	public int getInstall_period() {
		return install_period;
	}
	public void setInstall_period(int install_period) {
		this.install_period = install_period;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUse_redeem() {
		return use_redeem;
	}
	public void setUse_redeem(String use_redeem) {
		this.use_redeem = use_redeem;
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

	public int getPay_typ() {
		return pay_typ;
	}

	public void setPay_typ(int pay_typ) {
		this.pay_typ = pay_typ;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public void setTx_type(String tx_type) {
		this.tx_type = tx_type;
	}

	public void setCur(String cur) {
		this.cur = cur;
	}
	
	
}
