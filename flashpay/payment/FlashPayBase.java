package flashpay.payment;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import flashpay.payment.exception.FlashPayException;


public class FlashPayBase {
	
	protected static String Mode="";
	protected static String ServerUrl="";
	protected static String HashKey;
	protected static String HashIV;
	protected static String MerchantID;
	protected static String CreditTradeUrl;
	protected static String TradeInFoUrl;
	protected static String TradeDetailUrl;
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public FlashPayBase() {
		try {			
			Properties props= new Properties();
			props.load(getClass().getResourceAsStream("config/flashpay.properties"));			
			Mode=props.getProperty("FlashPayMode");
			ServerUrl=props.getProperty("FlashPay."+Mode+".url");
			HashKey=props.getProperty("FlashPay."+Mode+".hashKey");
			HashIV=props.getProperty("FlashPay."+Mode+".hashIV");
			MerchantID=props.getProperty("FlashPay."+Mode+".merchantID");
		}catch (FileNotFoundException e ) {
            e.printStackTrace();
            throw new FlashPayException("Config not found ");
        } catch (IOException e) {
            e.printStackTrace();
            throw new FlashPayException("Config error ");
        }
		
	}
	
    /*
     *  bytes to hex
     */
	private static String bin2hex(byte[] bs) {
		StringBuffer sb = new StringBuffer("");
		int bit;
		for (byte element : bs) {
			bit = (element & 0x0f0) >> 4;
			sb.append(hexArray[bit]);
			bit = element & 0x0f;
			sb.append(hexArray[bit]);
		}
		return sb.toString();
	}
	
    /*
     * hex to bytes
     */
	private static byte[] hex2bin(String hex) {
		String digital = String.valueOf(hexArray);
		char[] hex2char = hex.toCharArray();
		byte[] bytes = new byte[hex.length() / 2];
		int temp;
		for (int i = 0; i < bytes.length; i++) {
			temp = digital.indexOf(hex2char[2 * i]) * 16;
			temp += digital.indexOf(hex2char[2 * i + 1]);
			bytes[i] = (byte) (temp & 0xff);
		}
		return bytes;
	}
    /*
     * add PKCS7Padding
     */
	private static byte[] addPKCS7Padding(byte[] data, int iBlockSize) {
		int iLength = data.length;
		byte cPadding = (byte) (iBlockSize - (iLength % iBlockSize));
		byte[] output = new byte[iLength + cPadding];
		System.arraycopy(data, 0, output, 0, iLength);
		for (int i = iLength; i < output.length; i++)
			output[i] = cPadding;
		return output;
	}
    /*
     * remove PKCS7Padding
     */
	private static byte[] removePKCS7Padding(byte[] data, int size) {
		byte pad = data[data.length - 1];
		byte[] output = new byte[data.length - pad];
		System.arraycopy(data, 0, output, 0, data.length - pad);
		return output;
	}
    /*
     * AES encrypt
     */
	public final static String AESencrypt(String data) {
		try {
			SecretKey key = new SecretKeySpec(HashKey.getBytes("UTF-8"), "AES");
			IvParameterSpec iv = new IvParameterSpec(HashIV.getBytes("UTF-8"));
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] encryptDataBytes = cipher.doFinal(addPKCS7Padding(data.getBytes("UTF-8"), 16));
			return bin2hex(encryptDataBytes);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FlashPayException("AES encrypt error : " + e.getMessage());
		}
	}
    /*
     * AES decrypt
     */
	public final static String AESdecrypt(String data) {
		try {
			SecretKey key = new SecretKeySpec(HashKey.getBytes("UTF-8"), "AES");
			IvParameterSpec iv = new IvParameterSpec(HashIV.getBytes("UTF-8"));
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] decryptData = cipher.doFinal(hex2bin(data.toUpperCase()));
			return new String(removePKCS7Padding(decryptData, 16), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new FlashPayException("AES decrypt error : " + e.getMessage());
		}
	}
	
    /*
     * Hash
     */
	public final static String Hash(String data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(data.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new FlashPayException("SHA-256 error error : " + e.getMessage());
		}
		return bin2hex(md.digest());
	}
	
	
}
