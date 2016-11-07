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

	String title;
	/**
	 * constructor
	 */
	public FileManager() {
		title = "";
	}

	/**
	 * writes text from a webpage to a file. Calls redWebpage()
	 */
	public void write(String url, String heading, boolean text) {
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

		String noSpaces = readWebpage(writer, url, heading, text);

		// write to a file to be read from later
		writer.println("EVERY \"<span>\":" + noSpaces);
		writer.close();

		if(text)
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
//			lines.trimToSize();

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
	 */
	/**
	 * @param writer
	 * @param url
	 * @param heading
	 * @param inner
	 * @return
	 */
	private String readWebpage(PrintWriter writer, String url, String heading, boolean text) {

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
			if (text) {
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

		// this is used to indicate when the ingredients
		// need to start being read in on the document
		// since there is so much other text
		boolean start = false;

		// used to count lines
		// int lineNumber = 1;

		try {

			while ((currentLine = in.readLine()) != null) {

				// System.out.println(lineNumber + " " + currentLine);
				// System.out.println(currentLine);

				// starts reading ingredients after the first instance of
				// "&nbsp" is detected
				if (!start) {
					if ((currentLine.length() > 4) && (currentLine.substring(0, 5).equals("&nbsp"))) {

						// removes the "indication line" from being in the
						// file
						currentLine = in.readLine();
						start = true;
					}
				}

				// do not read blank lines
				if (currentLine.trim().equals("")) {
					currentLine = in.readLine();
				}

				// read ingredient
				if (start) {

					// this next block is just removing lines that are garbage
					if (currentLine.trim().equals("Recipe by")) {
						for (int i = 1; i <= 3; i++) {
							currentLine = in.readLine();
						}
					}

					// this block handles ingredients without an amount
					if (ingredients.size() > 1) {
						if ((ingredients.get(ingredients.size() - 2).length() > 6)
								&& (ingredients.get(ingredients.size() - 1).length() > 6)) {
							ingredients.add("0");
						}
					}
					ingredients.add(currentLine);
				}
				// lineNumber++;
			}

			//System.out.println("\n\n-----------------END----------------OF-------------------FILE---------------\n\n");
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getTitle() {
		return title;
	}
}
