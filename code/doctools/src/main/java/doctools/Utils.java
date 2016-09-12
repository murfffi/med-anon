package doctools;

public class Utils {

	public static String summary(String text) {
		if (text == null) {
			return "null";
		}
		return text.substring(0, Math.min(10, text.length()));
	}

}
