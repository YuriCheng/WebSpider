package ting.yori.test;

import java.io.File;
import java.io.FileOutputStream;

import ting.yori.http.HttpNutchUtil;

public class Test {
	public static void main(String[] args) {
		String questionUrl = "https://www.zhihu.com/question/53781002/answer/136876684";
		String answerHtml = HttpNutchUtil.downloadStaticHTML(questionUrl);
		File htmlFile = new File("D:\\htmlFile.txt");
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(htmlFile);
			fout.write(answerHtml.getBytes());
			fout.flush();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
