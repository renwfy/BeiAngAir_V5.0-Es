package com.beiang.airdog.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	public static boolean isMobileNumber(String mobiles) {
		if("".equals(mobiles) || null == mobiles){
			return false;
		}

		Pattern p = Pattern.compile("^\\d{11}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();

	}

	public static boolean isVerificationCode(String mobiles) {

		Pattern p = Pattern.compile("^\\d{6}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();

	}
	
	public static boolean isPasswordRight(String mobiles) {

		Pattern p = Pattern.compile("^[A-Za-z0-9]{6,20}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();

	}
	
	
}