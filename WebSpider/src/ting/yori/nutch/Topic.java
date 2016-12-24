package ting.yori.nutch;

public class Topic {
	
	public String topicName;
	public String topicHref;
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getTopicHref() {
		return topicHref;
	}
	public void setTopicHref(String topicHref) {
		this.topicHref = topicHref;
	}
	@Override
	public String toString() {
		return "Topic [topicName=" + topicName + ", topicHref=" + topicHref
				+ "]";
	}
	
}
