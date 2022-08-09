package flashpay.payment.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlashPayException extends Error{
	private static final long serialVersionUID = 1L;
	
	public static final Logger log = LogManager.getLogger(FlashPayException.class);
	
	private String  exceptionMessage;
	
	
	public FlashPayException(String msg)
	{ 
		this.exceptionMessage = msg;
		log.error(exceptionMessage);
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
		
	}
	
	
}
