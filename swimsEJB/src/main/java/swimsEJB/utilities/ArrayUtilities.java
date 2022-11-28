package swimsEJB.utilities;

import java.util.Arrays;

public class ArrayUtilities {
	// https://stackoverflow.com/questions/4138364/how-to-convert-list-to-map
	public static <T> T[] add2BeginningOfArray(T[] elements, T element) {
		T[] newArray = Arrays.copyOf(elements, elements.length + 1);
		newArray[0] = element;
		System.arraycopy(elements, 0, newArray, 1, elements.length);

		return newArray;
	}
}
