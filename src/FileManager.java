import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

/**
 * This class manages all file and webpage interactions: reading and writing
 * 
 * @author Jabari Barton
 *
 */
public class FileManager {

	String title, url, heading;

	// determines whether or not to print inner text or HTML
	boolean innerText;

	/**
	 * constructor
	 */
	public FileManager(String url, String heading, boolean innerText) {
		this.title = "";
		this.url = url;
		this.heading = heading;
		this.innerText = innerText;
	}

	/**
	 * writes text from a webpage to a file. Calls redWebpage()
	 */
	public void write() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("Recipe.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String noSpaces = readWebpage(writer);

		// write to a file to be read from later
		writer.println(noSpaces);
		writer.close();

		if (innerText)
			removeBlankLines();
	}

	/**
	 * Removes blank lines from file and writes the results to a new file
	 */
	private void removeBlankLines() {
		BufferedReader in = null;
		PrintWriter writer = null;

		// will hold each line of the file in one element
		ArrayList<String> lines = new ArrayList<String>();

		try {
			in = new BufferedReader(new FileReader("Recipe.txt"));
			writer = new PrintWriter("FinalRecipe.txt", "UTF-8");

			// current line of file
			String currentLine = "";

			// add each line to an array list
			while ((currentLine = in.readLine()) != null) {
				lines.add(currentLine);
			}

			in.close();

			// remove empty elements
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).trim().equals("")) {
					lines.remove(i);
					i--;
				}
			}
			// lines.trimToSize();

			// write to file
			for (int i = 0; i < lines.size(); i++) {
				writer.println(lines.get(i));
			}

			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Called by write(). Reads a webpage
	 * 
	 * @return a String representation of normal text from HTML code
	 * @param writer
	 *            the PrintWriter object to write to a file
	 */
	private String readWebpage(PrintWriter writer) {

		// create new userAgent (headless browser)
		UserAgent userAgent = new UserAgent();

		// visit a url
		try {
			userAgent.visit(url);

			title = userAgent.doc.findFirst("<title>").getText();

			// prints all text with "span" header
			Elements step1 = userAgent.doc.findEvery(heading);

			// trims extra spaces
			String noSpaces = "";
			if (innerText) {
				noSpaces = step1.innerText(null, true, true);
			} else {
				noSpaces = step1.innerHTML();
			}
			noSpaces = noSpaces.replaceAll("  ", "");
			return noSpaces;
		} catch (JauntException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * reads each line from a file, searches for ingredients, and places
	 * ingredients into the ingredients List
	 * 
	 * @param in
	 *            the document to be read from
	 * @param ingredients
	 *            the array to put the ingredient list into
	 */
	public void readFile(ArrayList<String> ingredients) {

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader("FinalRecipe.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// current line of file
		String currentLine = "";

		// used to count lines
		// int lineNumber = 1;

		try {

			while ((currentLine = in.readLine()) != null) {

				// System.out.println(lineNumber + " " + currentLine);
				// System.out.println(currentLine);

				// do not read blank lines
				if (currentLine.trim().equals("")) {
					currentLine = in.readLine();
				}

				// this block handles ingredients without an amount
				if (ingredients.size() > 1) {

					// prevents reading some numbers (redundant but safe)
					if (currentLine.length() > 3) {

						// if the current line and the previous line are not
						// numbers, then the amount is 0
						if (notANumber(currentLine) && notANumber(ingredients.get(ingredients.size() - 1))) {
							ingredients.add("0");
						}
					}
				}
				ingredients.add(currentLine);
			}

			// System.out.println("\n\n-----------------END----------------OF-------------------FILE---------------\n\n");
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Accessor for the title of the recipe
	 * 
	 * @return the title of the recipe
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns whether the input string is a number or not
	 * 
	 * @param s
	 *            the string in question
	 * @return boolean
	 */
	private boolean notANumber(String s) {
		if (!(String.valueOf((s.charAt(0))).matches("\\d+$"))
				&& !(String.valueOf((s.charAt(s.length() - 1))).matches("\\d+$"))) {
			return true;
		}
		return false;
	}

	/**
	 * Tester for this class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String urlPrime = "http://www.bettycrocker.com/recipes/salted-caramel-apple-cookies/a8324330-b1a6-4a7e-a446-ba52727a7677";

		ArrayList<String> ingredients = new ArrayList<String>();

		FileManager fm = new FileManager(urlPrime, "<dl class=\"recipePartIngredient\"", true);

		fm.write();

		// read file
		fm.readFile(ingredients);

		ArrayManager am = new ArrayManager(ingredients);

		System.out.println(fm.getTitle() + "\n" + urlPrime + "\n\n");

		// organizes arrays properly
		am.manageArrays();
		am.outputRecipes();
	}
}
