package ting.yori.nutch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class Nutch {
	
	public static void main(String[] args) {
		final String urlPath = "https://www.zhihu.com/explore/recommendations";
		String html = downloadStaticHTML(urlPath);
		//System.out.println(html);
		String postPatternStr = "";
		String quesPatternStr = "";
		postPatternStr ="post-link.+?>(.+?)<";
		quesPatternStr ="question_link.+?>(.+?)<"; 
		List<String> postList = regexString(html, postPatternStr);
		for(String s : postList){
			System.out.println(s);
		}
		List<String> quesList = regexString(html, quesPatternStr);
		for(String s : quesList){
			System.out.println(s);
		}
		
	}
	
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		 
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
	
	// ��ȡ��ҳ��htmlҳ��
	public static String downloadStaticHTML(String path){
		URL readUrl;
		HttpURLConnection conn;
		HttpsURLConnection https;
		String result = "";
		BufferedReader br = null;
		/*FileOutputStream output = null;
		File file = null;*/
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
			/*file = new File("D://webspider.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			output = new FileOutputStream(file);
			output.write(result.getBytes());
			output.flush();
			output.close();*/
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
		return result;
		//System.out.println(result);
	}
	/*
	 * targetStr Ҫ������Ŀ���ַ���
	 * patternStr Ҫƥ�����ݵ��ַ���
	 */
	public static List<String> regexString(String targetStr, String patternStr) {
		  // ����һ����ʽģ�壬����ʹ��������ʽ����������Ҫץ������
		  // �൱�����������ƥ��ĵط��ͻ����ȥ
		  //pattern�൱��һ��ƥ������ƥ��������ƥ������
		  List<String> results = new ArrayList<String>();
		  boolean isFound = false;
		  Pattern pattern = Pattern.compile(patternStr);
		  // ����һ��matcher������ƥ��
		  Matcher matcher = pattern.matcher(targetStr);
		  //System.out.println(targetStr);
		  //�ж��Ƿ��ҵ�
		  isFound = matcher.find();
		  // ����ҵ���
		  while (isFound) {
		   // ��ӡ�����
			  results.add(matcher.group(1));
			  isFound = matcher.find();
		  }
		  return results;
		 }

	
	
	
	
}
