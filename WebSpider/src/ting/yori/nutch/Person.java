package ting.yori.nutch;

//用户类
public class Person {
	
	//用户名称
	public String name;
	//用户签名
	public String sign;
	//用户详细信息连接
	public String personalUrl;
	//用户头像路径
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
