package flashpay.payment.operator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import flashpay.payment.exception.FlashPayException;

public class FlashPayUtil {

	public static final String APIVERSION = "1.0.0";



	public final static String httpPost(String url, String urlParameters, String encoding) {
		try {
			URL obj = new URL(url);
			HttpURLConnection connection = null;
			if (obj.getProtocol().toLowerCase().equals("https")) {
				connection = (HttpsURLConnection) obj.openConnection();
				trustAllHosts((HttpsURLConnection) connection);
			} else {
				connection = (HttpURLConnection) obj.openConnection();
			}
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64)  Chrome/99.0.2171.71 Safari/537.36");
			connection.setRequestProperty("Accept-Language", encoding);
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.write(urlParameters.getBytes(encoding));
			wr.flush();
			wr.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			return response.toString();
		} catch (Exception e) {
			throw new FlashPayException("httpPOST error : " + e.getMessage());
		}
	}

	private static void trustAllHosts(HttpsURLConnection connection) {
		X509TrustManager easyTrustManager = new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// Oh, I am easy!
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// Oh, I am easy!
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

		};

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { easyTrustManager };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");

			sc.init(null, trustAllCerts, new java.security.SecureRandom());

			connection.setSSLSocketFactory(sc.getSocketFactory());

		} catch (Exception e) {
			e.printStackTrace();
			throw new FlashPayException("SSL TLS ERROR : " + e.getMessage());
		}
	}
	
	
	

}
