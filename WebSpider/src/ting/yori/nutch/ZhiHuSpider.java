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

public class ZhiHuSpider {
	
	public static void main(String[] args) {
		final String urlPath = "https://www.zhihu.com/explore/recommendations";
		String html = downloadStaticHTML(urlPath);
		//System.out.println(html);
		/*String postPatternStr = "";
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
		}*/
		List<ZhiHu> lists = getZhihuQuesResults(html);
		for(ZhiHu entity : lists){
			System.out.println(entity);
		}
		
	}
	
	 //访问https时，进行跳过校验环节
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
	 //获取知乎问题内容
	public static List<ZhiHu> getZhihuQuesResults(String html) {
		  
		  List<ZhiHu> results = new ArrayList<ZhiHu>();
		  boolean isFound = false;
		  //定义一个知乎提问问题的匹配器
		  String quesPatternStr = "question_link.+?>(.+?)<";
		  Pattern patternQues = Pattern.compile(quesPatternStr);
		  
		  //定义一个知乎提问问题连接地址Url
		  String urlPatternStr = "question_link.+?href=\"(.+?)\"";
		  Pattern patternQuesUrl = Pattern.compile(urlPatternStr);
		  
		  // 定义一个matcher用来做匹配
		  // 问题匹配器
		  Matcher quesMatcher = patternQues.matcher(html);
		  // 问题地址匹配器
		  Matcher urlMatcher = patternQuesUrl.matcher(html);
		  
		  //System.out.println(targetStr);
		  //判断是否找到
		  isFound = quesMatcher.find() && urlMatcher.find();
		  // 如果找到了
		  while (isFound) {
		   // 创建ZhiHu对象
			  ZhiHu zhihu = new ZhiHu();
			  zhihu.setQuestion(quesMatcher.group(1));
			  zhihu.setQuestionUrl("https://www.zhihu.com" + urlMatcher.group(1));
			  results.add(zhihu);
			  isFound = quesMatcher.find() && urlMatcher.find();
		  }
		  return results;
		 }

	
	
	
	
}
