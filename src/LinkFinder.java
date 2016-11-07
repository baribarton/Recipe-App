import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LinkFinder {

	static ArrayList<String> links = new ArrayList<String>();

	public LinkFinder() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * findsearches for links on webpage
	 * 
	 * @param url
	 *            desired webpage to search for links on
	 */
	private void findLinks(String url) {
		FileManager fileManager = new FileManager();
		fileManager.write(url, "<li class=\"relatedContentItem recipe\"", false);

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader("Recipe.txt"));

			String currentLine = "";

			while ((currentLine = in.readLine()) != null) {
				if (currentLine.contains("http://www.bettycrocker.com/recipes/")) {
					// System.out.println("!!");
					links.add(currentLine);
				}
			}

			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * removes duplicate elements in list
	 */
	public void removeDuplicates() {
		for (int i = 0; i < links.size(); i++) {
			for (int j = 0; j < links.size(); j++) {
				if ((i != j) && (links.get(i).equals(links.get(j)))) {
					links.remove(j);
					j--;
				}
			}
		}
	}

	/**
	 * removes extra text on the beginning and end of the url in order to
	 * separate the URL
	 */
	private void exposeLink() {
		int size = links.size();
		for (int i = 0; i < size; i++) {
			links.set(i, (links.get(i).replaceAll("<a href=\"", "")));
			links.set(i, (links.get(i).replaceAll("\">", "")));
		}
	}

	private void changeLink(String url) {
		findLinks(url);
	}

	/**
	 * searches some amount of web pages and generates unique links from each
	 * one and adds them to the list
	 * 
	 * @param urlPrime
	 * initial url to start with
	 */
	public void generateLinks(String urlPrime, int limit) {
		findLinks(urlPrime);

		for (int i = 0; i < limit; i++) {
			removeDuplicates();
			exposeLink();
			changeLink(links.get(i));
		}

		removeDuplicates();
		exposeLink();
		displayLinks();
	}

	/**
	 * displays url links to screen with the size of the "links" list
	 */
	public void displayLinks() {
		for (int i = 0; i < links.size(); i++) {
			System.out.println(links.get(i));
		}

		System.out.println("List Size = " + links.size() + "\n\n");
	}

	/**
	 * returns a link at a given index
	 * 
	 * @param index
	 *            list index of url to be returned
	 * @return a string representaion of the url at the provided index
	 */
	public String getLink(int index) {
		return links.get(index);
	}

	/**
	 * Small tester
	 */
	public static void main(String[] args) {

		// starting link
		String urlPrime = "http://www.bettycrocker.com/recipes/sweet-potato-pie-smoothies/f7501540-a89c-4775-96e4-69edc45732c4";
		LinkFinder lf = new LinkFinder();
		lf.generateLinks(urlPrime, 5);

	}
}