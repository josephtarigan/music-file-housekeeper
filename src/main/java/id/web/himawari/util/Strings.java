package id.web.himawari.util;

/**
 * 
 * @author joseph.tarigan@gmail.com
 *
 */
public class Strings {

	public static boolean isNullOrEmpty (String string) {
		if (string == null) {
			return true;
		} else {
			return string.isEmpty();
		}
	}
	
	public static String stripIllegalCharacters (String input) {
		if (!Strings.isNullOrEmpty(input)) {
			return input.replaceAll("[/?%*:;|\"<>\\\\.]", "").trim();
		} else {
			return "";
		}
	}
}