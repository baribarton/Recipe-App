import java.util.ArrayList;

/**
 * @author Jabari Barton
 *
 */

public class Crawler {

	// initial URL
	String urlPrime = "http://www.bettycrocker.com/recipes/orange-smoothies/d1bb1d19-894d-43a9-a8cd-d1f7fd506552";
	final int AMOUNT_OF_RECIPES = 3;
	
	public void initiateCrawling() {
		LinkFinder lf = null;
		for (int i = 0; i <= AMOUNT_OF_RECIPES; i++) {
			
			// these arrays store amount each ingredient and the ingredients
			// themselves
			ArrayList<String> amounts = new ArrayList<String>();
			ArrayList<String> ingredients = new ArrayList<String>();
			
			//find links to other pages
			lf = new LinkFinder();
			lf.generateLinks(urlPrime, 5);

			//manage writing to a file
			FileManager file = new FileManager();
			ArrayManager arrayHandler = new ArrayManager();

			// write file
			file.write(urlPrime, "<span>", true);

			// read file
			file.readFile(ingredients);

			System.out.println(file.getTitle() + "\n" + urlPrime + "\n\n");

			// organizes arrays properly
			arrayHandler.manageArrays(amounts, ingredients);
			arrayHandler.outputArrays(amounts, ingredients);
			
			//change link
			urlPrime = lf.getLink(i);
		}
		lf.removeDuplicates();
	}
}