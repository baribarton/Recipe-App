import java.util.ArrayList;

/**
 * This class uses various objects from other classes to read and write from
 * different files, store data, extract data, display data, and traverse
 * webpages
 * 
 * @author Jabari Barton
 *
 */

public class Crawler {

	// initial URL
	String urlPrime = "http://www.bettycrocker.com/recipes/salted-caramel-apple-cheesecake-crisp/a992c890-6535-476d-a234-77489a1fb14f";
	final int AMOUNT_OF_RECIPES = 3;

	public void initiateCrawling() {
		LinkFinder lf = null;
		for (int i = 0; i <= AMOUNT_OF_RECIPES; i++) {

			// this array stores the amount each ingredient and the ingredients
			// themselves
			ArrayList<String> ingredients = new ArrayList<String>();

			// find links to other pages
			lf = new LinkFinder();
			lf.generateLinks(urlPrime, 5);

			// manage writing to a file
			FileManager file = new FileManager(urlPrime, "<dl class=\"recipePartIngredient\"", true);

			// read file and write to file
			file.write();
			file.readFile(ingredients);

			System.out.println(file.getTitle() + "\n" + urlPrime + "\n\n");

			ArrayManager arrayHandler = new ArrayManager(ingredients);

			// organizes arrays properly (ingredients and amounts separated)
			arrayHandler.manageArrays();
			arrayHandler.outputRecipes();

			// change link
			urlPrime = lf.getLink(i);
		}
		lf.removeDuplicates();
	}
}