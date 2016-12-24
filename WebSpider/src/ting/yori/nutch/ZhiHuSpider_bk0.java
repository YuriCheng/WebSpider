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
 *��һ����ʼ�汾 
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
	
	 //����httpsʱ����������У�黷��
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
	 //��ȡ֪����������
	public static List<ZhiHu> getZhihuQuesResults(String html) {
		  
		  List<ZhiHu> results = new ArrayList<ZhiHu>();
		  boolean isFound = false;
		  //����һ��֪�����������ƥ����
		  String quesPatternStr = "question_link.+?>(.+?)<";
		  Pattern patternQues = Pattern.compile(quesPatternStr);
		  
		  //����һ��֪�������������ӵ�ַUrl
		  //String urlPatternStr = "question_link.+?href=\"(.+?)\"";
		  //���ĵ�ǰ ��������ʽ��ʹ������ƥ��<a class="question_link" href="/question/53689098/answer/136195455"
		  //��������ֱ��/answer֮ǰ��OK�ˣ�����ƥ���ܻ�ȡ���еĻش�
		  String urlPatternStr = "question_link.+?href=\"(.*?)/answer/";
		  Pattern patternQuesUrl = Pattern.compile(urlPatternStr);
		  
		  // ����һ��matcher������ƥ��
		  // ����ƥ����
		  Matcher quesMatcher = patternQues.matcher(html);
		  // �����ַƥ����
		  Matcher urlMatcher = patternQuesUrl.matcher(html);
		  
		  //System.out.println(targetStr);
		  //�ж��Ƿ��ҵ�
		  isFound = quesMatcher.find() && urlMatcher.find();
		  // ����ҵ���
		  while (isFound) {
		   // ����ZhiHu����
			  ZhiHu zhihu = new ZhiHu();
			  zhihu.setQuestion(quesMatcher.group(1));
			  //�����Url 
			  String questionUrl = "https://www.zhihu.com";
			  if(urlMatcher.group(1) != "" && urlMatcher.group(1) != null){
				  questionUrl += urlMatcher.group(1); 
				  zhihu.setQuestionUrl(questionUrl);
				  //������һ�����ݵ���ȡ��������𰸵���ȡ
				  String answerHtml = downloadStaticHTML(questionUrl);
				 
				  //System.out.println(answerHtml);
				  //����һ��֪��������Ϣ��ƥ����description
				  String quesPatternDesc = "/question/detail\">.+?>(.+?)</div";
				  Pattern patternDesc = Pattern.compile(quesPatternDesc);
				  Matcher descMatcher = patternDesc.matcher(answerHtml);
				  if(descMatcher.find()){
					  zhihu.setDescription(descMatcher.group(1));
				  }
				  List<Topic> topics = new ArrayList<Topic>();
				  //���廰���ǩ����
				  String topicsName = "zm-item-tag\".+?>(.+?)</a";
				  Pattern patternTopicsName = Pattern.compile(topicsName);
				  Matcher topicsNameMatcher = patternTopicsName.matcher(answerHtml);
				  //���廰���ǩ����
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
				  //���б�
				  List<Answer> answerList = new ArrayList<Answer>();
				  //�ش����ǳ�ƥ��
				  String answerPersonName = "<a class=\"author-link\".+?>(.+?)</a>";
				  Pattern ansPersonPattern = Pattern.compile(answerPersonName,Pattern.DOTALL);
				  Matcher ansPersonMatcher = ansPersonPattern.matcher(answerHtml);
				  //�ش���ǩ��
				  String answerPersonSign= "author-link.+?title=\"(.+?)\" class";
				  Pattern ansPersonSign = Pattern.compile(answerPersonSign);
				  Matcher ansSignMatcher = ansPersonSign.matcher(answerHtml);
				  //��ƥ��
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
