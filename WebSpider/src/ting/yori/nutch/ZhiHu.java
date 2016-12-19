package ting.yori.nutch;

import java.util.ArrayList;

public class ZhiHu {
	
	//问题
	public String question;
	//问题连接
	public String questionUrl;
	//问题回答列表
	public ArrayList<String> answers;
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestionUrl() {
		return questionUrl;
	}
	public void setQuestionUrl(String questionUrl) {
		this.questionUrl = questionUrl;
	}
	public ArrayList<String> getAnswers() {
		return answers;
	}
	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}
	@Override
	public String toString() {
		return "ZhuHu [question=" + question + ", questionUrl=" + questionUrl
				+ ", answers=" + answers + "]";
	}
	
	
}
