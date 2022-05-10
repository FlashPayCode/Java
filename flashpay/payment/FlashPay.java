package flashpay.payment;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import flashpay.payment.exception.FlashPayException;
import flashpay.payment.operator.FlashPayUtil;
import flashpay.payment.operator.PaymentMethods;
import flashpay.payment.operator.PaymentMethodsItem;
import flashpay.payment.to.Order;
import flashpay.payment.to.OrderItem;

public class FlashPay extends FlashPayBase  {

	public static final String Version = "0.0.0";

	public static final Logger log = LogManager.getLogger(FlashPay.class);

	public FlashPay(String log4jpath) {
		super();
		if (log4jpath != "" && log4jpath != null) {
			String propertiesFile = log4jpath + "/log4j.xml";

			if (log4jpath.substring(log4jpath.length() - 1) == "/")
				propertiesFile = propertiesFile + "log4j.properties";
			else
				propertiesFile = propertiesFile + "/log4j.properties";

			try {
				LoggerContext logContext = (LoggerContext) LogManager.getContext(false);
				File conFile = new File(propertiesFile);
				logContext.setConfigLocation(conFile.toURI());
				logContext.reconfigure();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * create Order �s�W�q��
	 *
	 * @param ArrayList<OrderItem> �ӫ~�C��
	 * @param clinetUrl            �n�^�ǥ�����G�ɦV���e��(��ܵ����O��)
	 * @param returnURL            ������G�T���^��(�^�ǥ���T��)
	 * @param phone                ���O���p���q��
	 * @param pay                  ����覡
	 * @param installPeriod        �����I�ڴ���
	 * @return �q��order
	 */
	public Order createOrder(String orderNo, ArrayList<OrderItem> orderItem, String clinetUrl, String returnUrl,
			String phone, PaymentMethods pay, PaymentMethodsItem installPeriod) {
		if (orderItem == null || orderItem.size() <= 0 || orderNo == null || "".equals(orderNo) || clinetUrl == null
				|| "".equals(clinetUrl) || returnUrl == null || "".equals(returnUrl) || pay == null
				|| installPeriod == null)
			throw new FlashPayException("Incomplete order information");
		Order order = new Order();
		order.setMer_id(FlashPayBase.MerchantID);
		order.setPay_typ(pay.getVule());

		if (pay.getVule() == 1)
			order.setInstall_period(installPeriod.getVule());
		else
			order.setInstall_period(0);
		order.setOrd_no(orderNo);
		order.setPhone(phone);
		order.setClient_url(clinetUrl);
		order.setReturn_url(returnUrl);
		order.setInstall_period(0);
		String orderItemStr = "";
		double amt = 0;
		for (OrderItem item : orderItem) {
			orderItemStr += item.getOrderItemStr() + ",";
			amt += (item.getPrice() * item.getQuantity());
		}
		BigDecimal bd = new BigDecimal(amt).setScale(2, RoundingMode.HALF_UP);
		order.setAmt(bd.doubleValue());
		order.setOrder_desc(orderItemStr);
		return order;
	}

	/**
	 * checkout �q��
	 * 
	 * @param Order �q��
	 * @return �q��Form
	 */
	public String checkout(Order order) {
		Gson odrerJson = new Gson();
		String dataJson = odrerJson.toJson(order).toString();
		var map = encodeFormatData(FlashPayBase.MerchantID, dataJson);
		return getCheckoutHtml(map, FlashPayBase.ServerUrl + "/trade.php");
	}

	/**
	 * checkoutFeedback �B�z�^�Ǹ�T
	 * 
	 * @param response �^�Ǹ��
	 * @return OrderFeedback
	 */
	public String checkoutFeedback(String response) {
		String result =decodeFormatData(response);
		log.info("queryOrder result : " + result);
		return result;		
	}

	/**
	 * queryOrder �d�߳浧�q���T
	 * 
	 * @param orderNO �q��s��
	 * @return json�r��
	 */
	public String queryOrder(String orderNO) {
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("mer_id", FlashPayBase.MerchantID);
		jsonObj.addProperty("ord_no", orderNO);
		jsonObj.addProperty("tx_type", 107);
		String json = new Gson().toJson(jsonObj);
		var map = encodeFormatData(FlashPayBase.MerchantID, json);
		Gson gson = new Gson();
		String jsonString = gson.toJson(map);
		String result = FlashPayUtil.httpPost(FlashPayBase.ServerUrl + "/querytrade.php?XDEBUG_SESSION_START=web",
				jsonString, "UTF8");
		String decodeResult = decodeFormatData(result);
		log.info("queryOrder result : " + decodeResult);
		return decodeResult;
	}

	/**
	 * queryOrder �d�ߦh���q���T
	 * 
	 * @param beginDate �_�l�ɶ�
	 * @param endDate   �����ɶ�
	 * @return json�r��
	 */
	public String queryMultiOrder(LocalDate beginDate, LocalDate endDate) {
		Duration duration = Duration.between(beginDate.atStartOfDay(),endDate.atStartOfDay());
		long days = duration.toDays();
		if(days>30)
			return "The number of days cannot be greater than 30 days";
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("tx_type", 106);
		jsonObj.addProperty("mer_id", FlashPayBase.MerchantID);
		jsonObj.addProperty("start_date", beginDate.toString());
		jsonObj.addProperty("end_date", endDate.toString());
		String json = new Gson().toJson(jsonObj);
		var map = encodeFormatData(FlashPayBase.MerchantID, json);
		Gson gson = new Gson();
		String jsonString = gson.toJson(map);
		String result = FlashPayUtil.httpPost(FlashPayBase.ServerUrl + "/querytrade.php?XDEBUG_SESSION_START=web",
				jsonString, "UTF8");
		String decodeResult = decodeFormatData(result);
		log.info("queryMultiOrder result : " + decodeResult);
		return decodeResult;
	}
	
	/**
	 *  doTrade �������v(�ثe�u���Ѩ������v)
	 *  @param orderNO �q��s��
	 *  @param orderPrice �q����B
	 */
	public String doTrade(String orderNO ,double orderPrice)
	{
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("tx_type", 8);
		jsonObj.addProperty("mer_id", FlashPayBase.MerchantID);
		jsonObj.addProperty("ord_no", orderNO);
		jsonObj.addProperty("amt", orderPrice);
		String json = new Gson().toJson(jsonObj);
		var map = encodeFormatData(FlashPayBase.MerchantID, json);
		Gson gson = new Gson();
		String jsonString = gson.toJson(map);
		String result = FlashPayUtil.httpPost(FlashPayBase.ServerUrl + "/querytrade.php?XDEBUG_SESSION_START=web",
				jsonString, "UTF8");
		String decodeResult = decodeFormatData(result);
		log.info("queryMultiOrder result : " + decodeResult);
		return decodeResult;
	}

	private String getCheckoutHtml(Map<String, Object> map, String seriveUrl) {
		StringBuilder builder = new StringBuilder();
		builder.append("<form id=\"FLASHForm\" action=\"" + seriveUrl + "\" method=\"post\">");
		for (Entry<String, Object> entry : map.entrySet()) {
			builder.append("<input type=\"hidden\" name=\"" + entry.getKey() + "\" value=\""
					+ String.valueOf(entry.getValue()) + "\">");
		}
		builder.append("<script language=\"JavaScript\">");
		builder.append("FLASHForm.submit()");
		builder.append("</script>");
		builder.append("</form>");
		return builder.toString();
	}

	private Map<String, Object> encodeFormatData(String MerchantID, String data) {
		String checkData = HashKey + data + HashIV;
		String checkKeys = Hash(checkData);
		String tradeData = AESencrypt(data);
		String checkInfo = Hash(tradeData).toUpperCase();
		Map<String, Object> map = new HashMap<>();
		map.put("ver", FlashPayUtil.APIVERSION);
		map.put("mid", MerchantID);
		map.put("dat", tradeData);
		map.put("key", checkKeys);
		map.put("chk", checkInfo);
		return map;
	}

	//ver�H��ݭn���P����API�ֹ�ϥη|�Ψ�
	private String decodeFormatData(String data) {
		if("".equals(data))
			throw new FlashPayException("feedback data is null");
		try {
			String dat = "", chk = "";//, ver = "";
			String[] result = data.split("&");
			for (String str : result) {
				if (str.indexOf("dat") >= 0)
					dat = str.split("=")[1];
				if (str.indexOf("chk") >= 0)
					chk = str.split("=")[1];
				//if (str.indexOf("ver") >= 0)
					//ver = str.split("=")[1];
			}
			if (!"".equals(dat) && !"".equals(chk)) {
				String dathash = Hash(dat);
				if (!dathash.equals(chk))
					throw new FlashPayException("chk does not match");
				return AESdecrypt(dat);
			} else
				throw new FlashPayException("chk or dat nor found");
		} catch (Exception e) {
			log.error(data);
			return data;
		}
	}

}
