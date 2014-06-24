package com.chalet.lskpi.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class ChaletMD5Utils {

	
	private static byte[] add(byte[]... b) {
		int len = 0;
		for (int i = 0; i < b.length; i ++) {
			len += b[i].length;
		}
		byte[] bb = new byte[len];
		len = 0;
		for (int i = 0; i < b.length; i ++) {
			byte[] bi = b[i];
			System.arraycopy(bi, 0, bb, len, bi.length);
			len += bi.length;
		}
		return bb;
	}
	
	public final static String MD5(String s){
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}   
	}
	
	public static String KL(String inStr) {
		  // String s = new String(inStr);
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 'c');   
		}
		String s = new String(a);
		return s;
	}
	// 加密后解密 
	public static String JM(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 'c');
		}
		String k = new String(a);
		return k;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException{
		  String s = new String("123456");//maomao13799801//cooper123
		  System.out.println("原始：" + s);   
		  System.out.println("MD5后：" + MD5(s));
		  System.out.println("MD5后再加密：" + KL(MD5(s)));   
		  System.out.println("解密为MD5后的：" + JM(KL(MD5(s))));
		  
		  //CCA
		  String password = "P@ssw1rd";
		  System.out.println("{SSHA}" + new BASE64Encoder().encode(add(MessageDigest.getInstance("SHA1").digest(add(password.getBytes(), new byte[8])), new byte[8])));
	}
}
