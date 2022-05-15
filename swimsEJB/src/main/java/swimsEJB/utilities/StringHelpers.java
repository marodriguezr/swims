package swimsEJB.utilities;

import java.text.Normalizer;

public class StringHelpers {
	/**
	 * made possible by: https://dirask.com/posts/Java-remove-substring-from-string-between-indexes-DKooeD
	 * @param text original string.
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
	
	public static String stripAccents(String s) 
	{
	    s = Normalizer.normalize(s, Normalizer.Form.NFD);
	    s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	    return s;
	}
}
