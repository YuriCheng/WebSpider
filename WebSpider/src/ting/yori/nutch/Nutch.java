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
	
	// 爬取网页的html页面
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
			//开启连接
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
	 * targetStr 要解析的目标字符串
	 * patternStr 要匹配内容的字符串
	 */
	public static List<String> regexString(String targetStr, String patternStr) {
		  // 定义一个样式模板，此中使用正则表达式，括号中是要抓的内容
		  // 相当于埋好了陷阱匹配的地方就会掉下去
		  //pattern相当于一个匹配器，匹配器包含匹配内容
		  List<String> results = new ArrayList<String>();
		  boolean isFound = false;
		  Pattern pattern = Pattern.compile(patternStr);
		  // 定义一个matcher用来做匹配
		  Matcher matcher = pattern.matcher(targetStr);
		  //System.out.println(targetStr);
		  //判断是否找到
		  isFound = matcher.find();
		  // 如果找到了
		  while (isFound) {
		   // 打印出结果
			  results.add(matcher.group(1));
			  isFound = matcher.find();
		  }
		  return results;
		 }

	
	
	
	
}
