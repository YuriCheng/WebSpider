package ting.yori.nutch;

//回答类
public class Answer {
	
	//回答者
	private Person answerPerson;
	//回答结果
	private String answerResult;
	
	//点赞数
	private int count;
	
	public Person getAnswerPerson() {
		return answerPerson;
	}
	public void setAnswerPerson(Person answerPerson) {
		this.answerPerson = answerPerson;
	}
	public String getAnswerResult() {
		return answerResult;
	}
	public void setAnswerResult(String answerResult) {
		this.answerResult = answerResult;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "Answer [answerPerson=" + answerPerson + ", answerResult="
				+ answerResult + ", count=" + count + "]";
	}
	
}
