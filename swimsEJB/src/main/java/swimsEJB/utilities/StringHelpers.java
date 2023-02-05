package swimsEJB.utilities;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelpers {
	/**
	 * made possible by:
	 * https://dirask.com/posts/Java-remove-substring-from-string-between-indexes-DKooeD
	 * 
	 * @param text       original string.
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public static String removeSubstring(String text, int startIndex, int endIndex) {
		if (endIndex < startIndex) {
			startIndex = endIndex;
		}

		String a = text.substring(0, startIndex);
		String b = text.substring(endIndex);

		return a + b;
	}

	public static String stripAccents(String s) {
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return s;
	}

	public static String cammelCaseToKebabCase(String string) {
		final String regex = "(?=[A-Z][a-z])";
		final String subst = "-";

		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(string);

		// The substituted value will be contained in the result variable
		return matcher.replaceAll(subst).toLowerCase();
	}
	
	public static String cammelCaseToSnakeCase(String string) {
		final String regex = "(?=[A-Z][a-z])";
		final String subst = "_";

		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(string);

		// The substituted value will be contained in the result variable
		return matcher.replaceAll(subst).toLowerCase();
	}
}
