package ting.yori.nutch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ting.yori.http.HttpNutchUtil;
import ting.yori.util.RegExpUtil;

public class ZhiHuSpider {
	
	public static void main(String[] args) throws IOException {
		final String urlPath = "https://www.zhihu.com/explore/recommendations";
		String html = HttpNutchUtil.downloadStaticHTML(urlPath);
		List<ZhiHu> lists = getZhihuQuesResults(html);
		String finalResults = "";
		for(ZhiHu entity : lists){
			finalResults += entity.toString() +"\n";
		}
		File file = new File("D:\\myZhihuNutch.txt");
		if(file.exists()){
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file);
		out.write(finalResults.getBytes());
		out.flush();
		out.close();
		
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
				  String answerHtml = HttpNutchUtil.downloadStaticHTML(questionUrl);
				  //System.out.println(answerHtml);
				  //定义一个知乎描述信息的匹配器description
				  String quesPatternDesc = "/question/detail\">.+?>(.+?)</div";
				 
				  /*Pattern patternDesc = Pattern.compile(quesPatternDesc);
				  Matcher descMatcher = patternDesc.matcher(answerHtml);
				  if(descMatcher.find()){
					  zhihu.setDescription(descMatcher.group(1));
				  }*/
				  List<String> descMatchResults = RegExpUtil.getMatchResults(quesPatternDesc, answerHtml);
				  if(descMatchResults.size() > 0){
					  zhihu.setDescription(descMatchResults.get(0));
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
				  
				  //回答者个个人详细连接
				  String personalUrl = "author-link\".+?href=\"(.+?)\"";
				  Pattern personalUrlPattern = Pattern.compile(personalUrl);
				  Matcher personalUrlMatcher = personalUrlPattern.matcher(answerHtml);
				  
				  //回答者头像地址
				  String answerAvatarUrl = "<img src=\"(.+?)\"class=\"zm-list-avatar avatar";
				  Pattern ansAvatarPattern = Pattern.compile(answerAvatarUrl);
				  Matcher avatarMatcher = ansAvatarPattern.matcher(answerHtml);
				  
				  //点赞数
				  String answerAgreement = "class=\"count\">(.+?)<";
				  Pattern agreePattern = Pattern.compile(answerAgreement);
				  Matcher agreeMatcher = agreePattern.matcher(answerHtml);
				  
				  //答案匹配
				  String answerPatternStr = "<div class=\"zm-editable-content clearfix\">(.+?)</div>";
				  Pattern ansPattern = Pattern.compile(answerPatternStr);
				  Matcher ansMatcher = ansPattern.matcher(answerHtml);
				  
				  boolean ansFound = ansMatcher.find() && ansPersonMatcher.find() 
						  		  && agreeMatcher.find() && ansSignMatcher.find()
				  				  && avatarMatcher.find() && personalUrlMatcher.find();
				  while(ansFound){
					  Answer  eachAnswer = new Answer();
					  eachAnswer.setAnswerResult(ansMatcher.group(1));
					  eachAnswer.setCount(Integer.parseInt(agreeMatcher.group(1)));
					  Person eachPerson = new Person();
					  eachPerson.setName(ansPersonMatcher.group(1));
					  eachPerson.setSign(ansSignMatcher.group(1));
					  eachPerson.setAvatarUrl(avatarMatcher.group(1));
					  eachPerson.setPersonalUrl(personalUrlMatcher.group(1));
					  eachAnswer.setAnswerPerson(eachPerson);
					  answerList.add(eachAnswer);
					  ansFound = ansMatcher.find() && ansPersonMatcher.find() 
							  && agreeMatcher.find() && ansSignMatcher.find()
							  && avatarMatcher.find() && personalUrlMatcher.find();
				  }
				  zhihu.setAnswers(answerList);
			  }
			  results.add(zhihu);
			  isFound = quesMatcher.find() && urlMatcher.find();
		  }
		  return results;
		 }
}















