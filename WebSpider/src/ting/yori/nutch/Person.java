package ting.yori.nutch;

//�û���
public class Person {
	
	//�û�����
	public String name;
	//�û�ǩ��
	public String sign;
	//�û���ϸ��Ϣ����
	public String personalUrl;
	//�û�ͷ��·��
	public String avatarUrl;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getPersonalUrl() {
		return personalUrl;
	}
	public void setPersonalUrl(String personalUrl) {
		this.personalUrl = personalUrl;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", sign=" + sign + ", personalUrl="
				+ personalUrl + ", avatarUrl=" + avatarUrl + "]";
	}
	
}
