package ting.yori.nutch;
import java.util.List;

public class ZhiHu {
	
	//问题
	public String question;
	//问题连接
	public String questionUrl;
	//提问者
	public Person questioner;
	//问题的描述
	public String description;
	//问题回答内容列表---->这里可以进一步的扩充成为对象的集合
	public List<Answer> answers;
	//话题所属标签
	public List<Topic> topics;
	
	
	
	public List<Topic> getTopics() {
		return topics;
	}
	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Person getQuestioner() {
		return questioner;
	}
	public void setQuestioner(Person questioner) {
		this.questioner = questioner;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
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
	@Override
	public String toString() {
		return "ZhiHu [question=" + question + ", questionUrl=" + questionUrl
				+ ", questioner=" + questioner + ", description=" + description
				+ ", answers=" + answers + ", topics=" + topics + "]";
	}
	
	
}
