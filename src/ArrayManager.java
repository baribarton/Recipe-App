import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class manages all array operations
 * 
 * @author Jabari Barton
 *
 */
public class ArrayManager {

	/**
	 * Empty, no arg Constructor
	 */
	public ArrayManager() {
	}

	/**
	 * Removes specific elements from arrays and places the correct elements
	 * into the correct arrays
	 * 
	 * @param amounts
	 *            the array to place the amounts of ingredients into
	 * @param ingredients
	 *            the array to put the ingredient list into
	 */
	public void manageArrays(ArrayList<String> amounts, ArrayList<String> ingredients) {

		// Removes elements with no valuable content
		ingredients.removeAll(Arrays.asList(" ", null));
		ingredients.removeAll(Arrays.asList("", null));

		// adds the amounts of the ingredients to the amounts list and removes
		// the amounts from the ingredients list
		for (int i = 0; i < ingredients.size() - 1; i++) {

			String s = ingredients.get(i);

			// this block ensures that the amounts are placed into the correct
			// array.
			// the number "6" is arbitrary. few ingredients should have a length
			// shorter than 6
			if (s.length() < 6) {
				if ((String.valueOf((s.charAt(0))).matches("\\d+$"))
						&& (String.valueOf((s.charAt(s.length() - 1))).matches("\\d+$"))) {
					amounts.add(ingredients.get(i));
					ingredients.remove(i);
					i--;
				}
			}
		}

		// last 3 lines of the file are garbage
		for (int i = 1; i <= 3; i++) {
			ingredients.remove(ingredients.size() - 1);
		}
	}

	/**
	 * Outputs arrays to file and to the screen
	 * 
	 * @param amounts
	 *            the array to place the amounts of ingredients into
	 * @param ingredients
	 *            the array to put the ingredient list into
	 */
	public void outputArrays(ArrayList<String> amounts, ArrayList<String> ingredients) {

		// overwrites the FinalRecipe.txt file
		PrintWriter finalWriter = null;
		try {
			finalWriter = new PrintWriter("FinalRecipe.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// prints to screen and to file the contents of each array list
		for (int i = 0; i < ingredients.size(); i++) {

			//don't print the amount if it is 0
			if (!amounts.get(i).equals("0")) {
				if (i < amounts.size()) {
					System.out.print(amounts.get(i) + " ");
					finalWriter.print(amounts.get(i) + " ");
				}
			}

			System.out.println(ingredients.get(i));
			finalWriter.println(ingredients.get(i));
		}
		
		System.out.println("\n\n");
		finalWriter.println("\n\n");
		finalWriter.close();
	}
}
