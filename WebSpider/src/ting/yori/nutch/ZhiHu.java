package ting.yori.nutch;
import java.util.List;

public class ZhiHu {
	
	//����
	public String question;
	//��������
	public String questionUrl;
	//������
	public Person questioner;
	//���������
	public String description;
	//����ش������б�---->������Խ�һ���������Ϊ����ļ���
	public List<Answer> answers;
	//����������ǩ
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
