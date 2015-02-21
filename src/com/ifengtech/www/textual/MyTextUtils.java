package com.ifengtech.www.textual;

public class MyTextUtils {
	
	public static void main(String[] args) {
		
		String para = "Î²µ¥´ÙÏú\nÏ²Çì\n¹ÒºìµÆÁý";
		String title = generateTitle(para);
		System.out.println(title);
	}

	public static String generateTitle(String para) {
		if(isEmpty(para))
            return null;

        int le;
        le = (le = para.indexOf('\n')) != -1 ? le : ((le = para.indexOf('\r')) != -1 ? le : -1);

        if(le != -1)
            return para.substring(0, le);
        else
            return null;
	}
	
	public static boolean isEmpty(CharSequence str) {
		if(str == null || str.length() == 0) {
			return false;
		}
		
		int start = 0;
		while(start < str.length()) {
			if(!isWhiteSpace(str.charAt(start)))
				break;
		}
		
		CharSequence sub = str.subSequence(start, str.length());
		return sub == null;
	}
	
	public static boolean isWhiteSpace(char ch) {
		if(ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r')
			return true;
		else 
			return false;
	}
}
