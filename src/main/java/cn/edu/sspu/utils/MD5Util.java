package cn.edu.sspu.utils;

import java.security.MessageDigest;

public class MD5Util {
	public static String transferMD5(String strMD5) throws Exception {
	    MessageDigest messageDigest = null;
	    StringBuffer md5StrBuff = new StringBuffer();
	    try {
	        messageDigest = MessageDigest.getInstance("MD5");
	        messageDigest.reset();
	        messageDigest.update(strMD5.getBytes("UTF-8"));

	        byte[] byteArray = messageDigest.digest();
	        for (int i = 0; i < byteArray.length; i++)
	        {
	            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
	                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
	            else
	                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
	        }
	    } catch (Exception e) {
	        throw new RuntimeException();
	    }
	    return md5StrBuff.toString().toUpperCase();//字母大写
	}
}
