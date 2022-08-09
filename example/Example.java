package flashpay.payment.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import flashpay.payment.FlashPay;
import flashpay.payment.operator.PaymentMethods;
import flashpay.payment.operator.PaymentMethodsItem;
import flashpay.payment.to.Order;
import flashpay.payment.to.OrderItem;



public class Example {
	
	public static  FlashPay flashpay;
	
	public static void main(String[] args) {
		init();
	}
	
	private static void init(){
		// you can set log4j ex: flashpay = new FlashPay(" .. /log4j.properties");
		flashpay = new FlashPay("");
	}

	//新增訂單
	private  String createrOrder()
	{
		FlashPay f =new FlashPay("");
		var arrayitem = new ArrayList<OrderItem>();
		var item =new OrderItem();
		item.setName("123");
		item.setPrice(15.20);
		item.setQuantity(5);
		item.setUnit("unit");		
		arrayitem.add(item);
		String ordListStr="";
		for(OrderItem oitem : arrayitem)
			ordListStr+=oitem.getOrderItemStr();
		var od = new Order();
	    od.setOrd_no("99999920");
	    od.setOrd_time(LocalDateTime.now());
	    od.setOrder_desc(ordListStr);
		od.setAmt(76.00);
		od.setSto_id("My store");
		od.setPay_type(PaymentMethods.Credit);
		od.setInstall_period( PaymentMethodsItem.Credit_all);
	    od.setPhone("0912345678");
		od.setClient_url("test");
		od.setReturn_url("test");
		return f.createOrder(od);
	};
	
	
	//結帳
	private  String checkout(String orderJsonStr)
	{
		return flashpay.checkOut(orderJsonStr);
	}
	
	//單筆交易查詢
	private  String queryOrder()
	{
	  return flashpay.queryOrder("269393");
	}
	//多筆交易查詢
	private  String  queryMultiOrder()
	{
		return flashpay.queryMultiOrder(LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-20"));
	}
	//取消請款
	private  String  cancelAuthorization()
	{
		return flashpay.doTrade("2679",441.00);
	}
	
	//交易資訊回傳請參考API規格書資料解密請使用encodeData()函式進行解密
	private String tradeReturn()
	{
		return flashpay.decodeData("交易回傳資訊");
	}
}
