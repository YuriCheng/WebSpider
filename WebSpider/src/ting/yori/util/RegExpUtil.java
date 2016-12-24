package ting.yori.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ������ʽ������
 *
 */
public class RegExpUtil {
	public static List<String> getMatchResults(String regexStr,String htmlStr){
		
		List<String> results = new ArrayList<String>();
		Pattern regexPattern = Pattern.compile(regexStr);
		Matcher regexMatcher = regexPattern.matcher(htmlStr);
		while(regexMatcher.find()){
			
			String matchResult = regexMatcher.group(1);
			results.add(matchResult);
		}
		return results;
	}
}
