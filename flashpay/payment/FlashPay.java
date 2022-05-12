package flashpay.payment;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import flashpay.payment.operator.PaymentMethodsItem;
import flashpay.payment.to.Order;


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
	 * create Order 新增訂單
	 *
	 * @param order_desc           商品列表
	 * @param clinetUrl            要回傳交易結果導向的畫面(顯示給消費者)
	 * @param amt                  總金額
	 * @param returnURL            交易結果訊息回傳(回傳交易訊息)
	 * @param phone                消費者聯絡電話
	 * @param pay                  交易方式
	 * @param installPeriod        分期付款期數
	 * @param order_time           訂單時間
	 * @param sto_id               商店名稱
	 * @return JsonString
	 */
	public String createOrder(Order order) {
		if ( order.getOrd_no() == null 
				|| order == null
				|| "".equals(order.getOrd_no()) 
				|| order.getClient_url() == null
				|| "".equals(order.getClient_url()) 
				|| order.getReturn_url() == null 
				|| "".equals(order.getReturn_url()) 
				|| order.getOrd_time() == null
				|| order.getInstall_period() ==null
				|| order.getPay_type()==null)
			throw new FlashPayException("Incomplete order information");
		//銀聯卡暫時不提供分期
		if(order.getPay_type().getVule()==2)
			order.setInstall_period(PaymentMethodsItem.Union_all);
		if(order.getPay_type().getVule()== 1 && "Union_all".equals(order.getInstall_period().getPaymentItem()))
			throw new FlashPayException("Install_period can't use Union_all");
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("pay_type", order.getPay_type().getVule());
		jsonObj.addProperty("mer_id", FlashPayBase.MerchantID);
		jsonObj.addProperty("order_desc",order.getOrder_desc());
		jsonObj.addProperty("ord_no",order.getOrd_no());
		jsonObj.addProperty("client_url",order.getClient_url());
		jsonObj.addProperty("return_url",order.getReturn_url());
		//暫時只提供台幣交易
		jsonObj.addProperty("cur","NTD");
		jsonObj.addProperty("amt",order.getAmt());
		jsonObj.addProperty("sto_id",order.getSto_id());
		jsonObj.addProperty("phone",order.getPhone());
		jsonObj.addProperty("use_redeem",order.getUse_redeem());
		jsonObj.addProperty("ver", order.getVer());
		jsonObj.addProperty("tx_type",order.getTx_type() );
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String orderTime=order.getOrd_time().format(format);
		jsonObj.addProperty("ord_time", orderTime);
		String json = new Gson().toJson(jsonObj);
		return json;
	}

	/**
	 * checkout 訂單
	 * 
	 * @param Order 訂單
	 * @return 訂單Form
	 */
	public String checkout(String dataJson) {
		var map = encodeFormatData(FlashPayBase.MerchantID, dataJson);
		return getCheckoutHtml(map, FlashPayBase.ServerUrl + "/trade.php");
	}

	/**
	 * checkoutFeedback 處理回傳資訊
	 * 
	 * @param response 回傳資料
	 * @return OrderFeedback
	 */
	public String checkoutFeedback(String response) {
		String result =decodeFormatData(response);
		log.info("queryOrder result : " + result);
		return result;		
	}

	/**
	 * queryOrder 查詢單筆訂單資訊
	 * 
	 * @param orderNO 訂單編號
	 * @return json字串
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
	 * queryOrder 查詢多筆訂單資訊
	 * 
	 * @param beginDate 起始時間
	 * @param endDate   結束時間
	 * @return json字串
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
	 *  doTrade 取消授權(目前只提供取消授權)
	 *  @param orderNO 訂單編號
	 *  @param orderPrice 訂單金額
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

	//ver以後需要不同版本API核對使用會用到
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
