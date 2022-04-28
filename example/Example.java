package flashpay.payment.example;

import java.time.LocalDate;
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
		//you can set log4j ex: flashpay = new FlashPay(" .. /log4j.properties");
		flashpay = new FlashPay("");
	}

	//新增訂單
	private static Order createrOrder()
	{
		var arrayitem = new ArrayList<OrderItem>();
		var item =new OrderItem();
		item.setName("123");
		item.setPrice(15.20);
		item.setQuantity(5);
		item.setUnit("個");		
		arrayitem.add(item);
		Order order =flashpay.createOrder("99999920", arrayitem, "test", "test","test", PaymentMethods.Credit, PaymentMethodsItem.Credit_all);
		return order;
	};
	
	
	//結帳頁面
	private static String checkout(Order order)
	{
		return flashpay.checkout(order);
	}
	
	
	//單筆交易查詢
	private static String queryOrder()
	{
	  return flashpay.queryOrder("269393");
	}
	
	private static String  queryMultiOrder()
	{
		return flashpay.queryMultiOrder(LocalDate.parse("2022-04-01"), LocalDate.parse("2022-04-20"));
	}
	
	private static String  cancelAuthorization()
	{
		return flashpay.doTrade("2679",441.00);
	}
	
	

}
