package ting.yori.nutch;

//�ش���
public class Answer {
	
	//�ش���
	private Person answerPerson;
	//�ش���
	private String answerResult;
	
	//������
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
