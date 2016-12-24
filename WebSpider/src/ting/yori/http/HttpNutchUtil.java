package ting.yori.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class HttpNutchUtil {
	 //����httpsʱ����������У�黷��
		final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
	        public boolean verify(String hostname, SSLSession session) {
	            return true;
	        }
	    };
	    /**
	     *  ʹ��http������ȡ��ҳ��htmlҳ��
	     *  ����URLConnection
	     * @param path
	     * @return
	     */
		public static String downloadStaticHTML(String path){
			URL readUrl;
			HttpURLConnection conn;
			HttpsURLConnection https;
			String result = "";
			BufferedReader br = null;
			try {
				
				readUrl = new URL(path);
			    https = (HttpsURLConnection)readUrl.openConnection();
				if (readUrl.getProtocol().toLowerCase().equals("https")) {
	                https.setHostnameVerifier(DO_NOT_VERIFY);
	                conn = (HttpURLConnection) https;
	            } else {
	                conn = (HttpURLConnection)readUrl.openConnection();
	            }
				//��������
				conn = (HttpURLConnection) readUrl.openConnection();
				
				conn.connect();
				 br = new BufferedReader(
								new InputStreamReader(conn.getInputStream(),"utf-8"));
				String line = "";
				while((line = br.readLine()) != null){
					result += line;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			} finally{
				try {
					if(br != null){
						br.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			result = result.replace("\\n","");
			return result;
		}
}
