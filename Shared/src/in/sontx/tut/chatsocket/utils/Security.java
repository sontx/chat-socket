package in.sontx.tut.chatsocket.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public final class Security {
	private static final Pattern userNamePattern = Pattern.compile("^[a-z0-9_-]{3,15}$");
	private static final Pattern passwordPattern = Pattern.compile("^.{6,40}$");
	private static final Random rand = new Random(new Date().getTime());

	public static boolean checkValidUsername(String username) {
		return username != null && userNamePattern.matcher(username).matches();
	}

	public static boolean checkValidPassword(String password) {
		return password != null && passwordPattern.matcher(password).matches();
	}

	public static boolean checkValidDisplayName(String disp) {
		return disp != null && disp.trim().length() >= 3;
	}

	private static String bytesToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	private static String convertToSHA1(byte[] bytes) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			return bytesToHexString(md.digest(bytes));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getPasswordHash(String password) {
		if (password == null)
			return null;
		return convertToSHA1(password.getBytes(Charset.forName("UTF-8")));
	}

	public static int getRandomInteger() {
		return rand.nextInt(Integer.MAX_VALUE);
	}

	Security() {
	}
}
