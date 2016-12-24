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

/**
 *第一个初始版本 
 *
 */
public class ZhiHuSpider_bk0 {
	
	public static void main(String[] args) throws IOException {
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
		String finalResults = "";
		for(ZhiHu entity : lists){
			finalResults += entity.toString() +"\n";
		}
		File file = new File("D:\\myZhihuNutch.txt");
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file);
		out.write(finalResults.getBytes());
		out.flush();
		out.close();
		
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
	 //获取知乎问题内容
	public static List<ZhiHu> getZhihuQuesResults(String html) {
		  
		  List<ZhiHu> results = new ArrayList<ZhiHu>();
		  boolean isFound = false;
		  //定义一个知乎提问问题的匹配器
		  String quesPatternStr = "question_link.+?>(.+?)<";
		  Pattern patternQues = Pattern.compile(quesPatternStr);
		  
		  //定义一个知乎提问问题连接地址Url
		  //String urlPatternStr = "question_link.+?href=\"(.+?)\"";
		  //更改当前 的正则表达式，使用它来匹配<a class="question_link" href="/question/53689098/answer/136195455"
		  //不过这里直到/answer之前就OK了，这样匹配能获取所有的回答
		  String urlPatternStr = "question_link.+?href=\"(.*?)/answer/";
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
			  //问题的Url 
			  String questionUrl = "https://www.zhihu.com";
			  if(urlMatcher.group(1) != "" && urlMatcher.group(1) != null){
				  questionUrl += urlMatcher.group(1); 
				  zhihu.setQuestionUrl(questionUrl);
				  //进入下一步内容的爬取，即问题答案的爬取
				  String answerHtml = downloadStaticHTML(questionUrl);
				 
				  //System.out.println(answerHtml);
				  //定义一个知乎描述信息的匹配器description
				  String quesPatternDesc = "/question/detail\">.+?>(.+?)</div";
				  Pattern patternDesc = Pattern.compile(quesPatternDesc);
				  Matcher descMatcher = patternDesc.matcher(answerHtml);
				  if(descMatcher.find()){
					  zhihu.setDescription(descMatcher.group(1));
				  }
				  List<Topic> topics = new ArrayList<Topic>();
				  //定义话题标签名称
				  String topicsName = "zm-item-tag\".+?>(.+?)</a";
				  Pattern patternTopicsName = Pattern.compile(topicsName);
				  Matcher topicsNameMatcher = patternTopicsName.matcher(answerHtml);
				  //定义话题标签连接
				  String topicsHref = "zm-item-tag\"href=\"(.+?)\"";
				  Pattern patternTopicsHref = Pattern.compile(topicsHref);
				  Matcher topicsHrefMatcher = patternTopicsHref.matcher(answerHtml);
				  System.out.println(topicsHrefMatcher.find());
				  boolean topicsFound = topicsNameMatcher.find() && topicsHrefMatcher.find();
				  while(topicsFound){
					  Topic topic = new Topic();
					  topic.setTopicName(topicsNameMatcher.group(1));
					  topic.setTopicHref(topicsHrefMatcher.group(1));
					  topics.add(topic);
					  topicsFound = topicsNameMatcher.find() && topicsHrefMatcher.find();
				  }
				  zhihu.setTopics(topics);
				  //答案列表
				  List<Answer> answerList = new ArrayList<Answer>();
				  //回答者昵称匹配
				  String answerPersonName = "<a class=\"author-link\".+?>(.+?)</a>";
				  Pattern ansPersonPattern = Pattern.compile(answerPersonName,Pattern.DOTALL);
				  Matcher ansPersonMatcher = ansPersonPattern.matcher(answerHtml);
				  //回答者签名
				  String answerPersonSign= "author-link.+?title=\"(.+?)\" class";
				  Pattern ansPersonSign = Pattern.compile(answerPersonSign);
				  Matcher ansSignMatcher = ansPersonSign.matcher(answerHtml);
				  //答案匹配
				  String answerPatternStr = "<div class=\"zm-editable-content clearfix\">(.+?)</div>";
				  Pattern ansPattern = Pattern.compile(answerPatternStr);
				  Matcher ansMatcher = ansPattern.matcher(answerHtml);
				  
				  boolean ansFound = ansMatcher.find() && ansPersonMatcher.find() && ansSignMatcher.find();
				  while(ansFound){
					  Answer  eachAnswer = new Answer();
					  eachAnswer.setAnswerResult(ansMatcher.group(1));
					  Person eachPerson = new Person();
					  eachPerson.setName(ansPersonMatcher.group(1));
					  eachPerson.setSign(ansSignMatcher.group(1));
					  eachAnswer.setAnswerPerson(eachPerson);
					  answerList.add(eachAnswer);
					  ansFound = ansMatcher.find() && ansPersonMatcher.find() && ansSignMatcher.find();
				  }
				  zhihu.setAnswers(answerList);
			  }
			  results.add(zhihu);
			  isFound = quesMatcher.find() && urlMatcher.find();
		  }
		  return results;
		 }
}
