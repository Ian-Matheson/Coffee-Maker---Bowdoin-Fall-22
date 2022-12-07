package edu.ncsu.csc.CoffeeMaker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;


/**
 * 
 * This class tests how recipes interacts with the database, ensuring changes that 
 * are made to recipes are properly reflected in the database too. Such changes include
 * creating a new recipe, altering the data within a recipe, deleting a recipe, and updating
 * a recipe. This class also ensures that the recipe characteristics are met such as having no more 
 * than 3 recipes, no duplicate recipes, and no recipes without ingredients.
 * 
 * @author IanMatheson
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteractionRecipe {
	
	/** allows recipes to be added to database*/
	@Autowired
	private RecipeService recipeService;
	
	/** allows inventory to be added to database*/
	@Autowired
	private InventoryService inventoryService;
	
	
	/**
	 * BeforeEach clears the current Recipe Service.
	 * @throws Exception if fails to build
	 */
	@BeforeEach
	public void setUp() throws Exception {
		recipeService.deleteAll();
		inventoryService.deleteAll();
	}
	
	/**
	 * 
	 * This class tests how valid recipes operations interacts with the database, 
	 * ensuring changes that are made to recipes are properly reflected in the database too. 
	 * Such changes tested include creating a new recipe and altering the data within a recipe. 
	 */
	@Test
	@Transactional
	public void testValidRecipes(){
        
        Inventory ivt = new Inventory();
        
        ivt.addIngredients("Coffee", 500);
        ivt.addIngredients("Pumpkin Spice", 500);
        ivt.addIngredients("Milk", 500);
        ivt.addIngredients("Sugar", 500);
        
        inventoryService.save(ivt);
        
        Ingredient ing1 = new Ingredient("Coffee", 500);
        Ingredient ing2 = new Ingredient("Pumpkin Spice", 400);
        Ingredient ing3 = new Ingredient("Milk", 300);
        Ingredient ing4 = new Ingredient("Sugar", 200);
        
		Recipe r = new Recipe();
	    
	    r.setName("mocha");
	    r.setPrice(50);
        r.addIngredient(ing1, 25);
        r.addIngredient(ing2, 20);
        r.addIngredient(ing3, 15);
        r.addIngredient(ing4, 10);
        r.setPrice( 5 );
        
	    recipeService.save( r );
	    
	    //ensuring that adding an ingredient count in recipe doesn't effect inventory
	    inventoryService.save(ivt);
	    Assertions.assertEquals("Coffee", ivt.getIngredients().get(0).getName()); 
	    Assertions.assertEquals(500, ivt.getIngredients().get(0).getAmount()); 
	    Assertions.assertEquals("Pumpkin Spice", ivt.getIngredients().get(1).getName()); 
	    Assertions.assertEquals(500, ivt.getIngredients().get(1).getAmount()); 
	    Assertions.assertEquals("Milk", ivt.getIngredients().get(2).getName()); 
	    Assertions.assertEquals(500, ivt.getIngredients().get(2).getAmount()); 
	    Assertions.assertEquals("Sugar", ivt.getIngredients().get(3).getName()); 
	    Assertions.assertEquals(500, ivt.getIngredients().get(3).getAmount()); 
		
	    List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();

	    assertEquals(1, dbRecipes.size());

	    Recipe dbRecipe = dbRecipes.get(0);

	    assertEquals(r.getName(), dbRecipe.getName());
	    assertEquals(r.getPrice(), dbRecipe.getPrice());
	    assertEquals(r.getIngredients().get(0), dbRecipe.getIngredients().get(0));
	    assertEquals(r.getIngredients().get(1), dbRecipe.getIngredients().get(1));
	    assertEquals(r.getIngredients().get(2), dbRecipe.getIngredients().get(2));
	    assertEquals(r.getIngredients().get(3), dbRecipe.getIngredients().get(3));
	    
	    Recipe recipeBN = recipeService.findByName("mocha");
	    assertEquals(r.getName(), recipeBN.getName());
	    
	    dbRecipe.setPrice(15);
	    dbRecipe.editIngredient(ing1, 12);
	    dbRecipe.editIngredient(ing2, 8);
	    dbRecipe.editIngredient(ing3, 4);
	    dbRecipe.editIngredient(ing4, 2);

	    recipeService.save(dbRecipe);
	    
	    //ensuring that editing an ingredient count in recipe doesn't effect inventory
	    inventoryService.save(ivt);
	    Assertions.assertEquals("Coffee", ivt.getIngredients().get(0).getName()); 
	    Assertions.assertEquals(500, ivt.getIngredients().get(0).getAmount()); 
	    Assertions.assertEquals("Pumpkin Spice", ivt.getIngredients().get(1).getName()); 
	    Assertions.assertEquals(500, ivt.getIngredients().get(1).getAmount()); 
	    Assertions.assertEquals("Milk", ivt.getIngredients().get(2).getName()); 
	    Assertions.assertEquals(500, ivt.getIngredients().get(2).getAmount()); 
	    Assertions.assertEquals("Sugar", ivt.getIngredients().get(3).getName()); 
	    Assertions.assertEquals(500, ivt.getIngredients().get(3).getAmount()); 

        assertEquals( 1, recipeService.count() );
        assertEquals( 15, (int) ( (Recipe) recipeService.findAll().get( 0 ) ).getPrice() );
        assertEquals(12, dbRecipe.getIngredients().get(0).getAmount());
        assertEquals(8, dbRecipe.getIngredients().get(1).getAmount());
        assertEquals(4, dbRecipe.getIngredients().get(2).getAmount());
        assertEquals(2, dbRecipe.getIngredients().get(3).getAmount());
	       
	}
	
	/**
	 * 
	 * This class tests how valid recipes operations interacts with the database, 
	 * ensuring changes that are made to recipes are properly reflected in the database too. 
	 * Such changes tested include updating a recipe. 
	 * 
	 */
	@Test
	@Transactional
	public void testValidRecipes2(){
        
        Inventory ivt = new Inventory();
        
        ivt.addIngredients("Coffee", 500);
        ivt.addIngredients("Pumpkin Spice", 500);
        ivt.addIngredients("Milk", 500);
        ivt.addIngredients("Sugar", 500);
        ivt.addIngredients("Marshmallow", 500);
        
        inventoryService.save(ivt);
        
        Ingredient ing1 = new Ingredient("Coffee", 500);
        Ingredient ing2 = new Ingredient("Pumpkin Spice", 400);
        Ingredient ing3 = new Ingredient("Milk", 300);
        Ingredient ing4 = new Ingredient("Sugar", 200);
        
		Recipe r1 = new Recipe();
	    
	    r1.setName("mocha");
	    r1.setPrice(50);
        r1.addIngredient(ing1, 25);
        r1.addIngredient(ing2, 20);
        r1.addIngredient(ing3, 15);
        r1.addIngredient(ing4, 10);
        r1.setPrice( 5 );
        
	    recipeService.save( r1 );
	    
	    Recipe r2 = new Recipe();
	    
	    r2.setName("espresso");
	    r2.setPrice(50);
        r2.addIngredient(ing1, 10);
        r2.addIngredient(ing2, 20);
        r2.addIngredient(ing3, 15);
        r2.addIngredient(ing4, 10);
        
        r1.updateRecipe(r2);
        
	    recipeService.save( r1 );
	    
	    List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();

	    assertEquals(1, dbRecipes.size());

	    Recipe dbRecipe = dbRecipes.get(0);

	    assertEquals(r2.getName(), dbRecipe.getName());
	    assertEquals(r2.getPrice(), dbRecipe.getPrice());
	    assertEquals(r2.getIngredients().get(0), dbRecipe.getIngredients().get(0));
	    assertEquals(r2.getIngredients().get(1), dbRecipe.getIngredients().get(1));
	    assertEquals(r2.getIngredients().get(2), dbRecipe.getIngredients().get(2));
	    assertEquals(r2.getIngredients().get(3), dbRecipe.getIngredients().get(3));
	    
	    Recipe recipeBN = recipeService.findByName("espresso");
	    assertEquals(r2.getName(), recipeBN.getName());
	    
        Ingredient ing5 = new Ingredient("Marshmallow", 200);
        
        Recipe r3 = new Recipe();
	    
	    r3.setName("Christmas Special");
	    r3.setPrice(100);
        r3.addIngredient(ing1, 10);
        r3.addIngredient(ing2, 20);
        r3.addIngredient(ing5, 15);
        
        r1.updateRecipe(r3);
        
	    recipeService.save( r1 );
	    
	    dbRecipes = (List<Recipe>) recipeService.findAll();

	    assertEquals(1, dbRecipes.size());

	    dbRecipe = dbRecipes.get(0);

	    assertEquals(r3.getName(), dbRecipe.getName());
	    assertEquals(r3.getPrice(), dbRecipe.getPrice());
	    assertEquals(r3.getIngredients().get(0), dbRecipe.getIngredients().get(0));
	    assertEquals(r3.getIngredients().get(1), dbRecipe.getIngredients().get(1));
	    assertEquals(r3.getIngredients().get(2), dbRecipe.getIngredients().get(2));

	    recipeBN = recipeService.findByName("Christmas Special");
	    assertEquals(r3.getName(), recipeBN.getName());

	}
	

	/**
	 * 
	 * This class tests how invalid recipes operations interacts with the database, 
	 * ensuring invalid changes are caught and not reflected in the database. 
	 * Such changes tested include adding a duplicate recipe, adding more than 3 recipes,
	 * adding an ingredient to a recipe that doesn't exist in the inventory, and creating
	 * a recipe without any ingredients.
	 *  
	 */
 	@Test
	@Transactional
	public void testInvalidRecipes1(){
		
 		Inventory ivt = new Inventory();
        
        ivt.addIngredients("Coffee", 500);
        ivt.addIngredients("Pumpkin Spice", 500);
        ivt.addIngredients("Milk", 500);
        ivt.addIngredients("Sugar", 500);
        
        inventoryService.save(ivt);
        
	    Ingredient ing1 = new Ingredient("Coffee", 500);
        Ingredient ing2 = new Ingredient("Pumpkin Spice", 400);
        Ingredient ing3 = new Ingredient("Milk", 300);
        Ingredient ing4 = new Ingredient("Sugar", 200);
        
		Recipe r1 = new Recipe();
		r1.setName("mocha");
	    r1.setPrice(50);
        r1.addIngredient(ing1, 1);
        r1.addIngredient(ing2, 1);
        r1.addIngredient(ing3, 1);
        r1.addIngredient(ing4, 1);
	    recipeService.save( r1 );
	    
	    Recipe r2 = new Recipe();
		r2.setName("lattee");
	    r2.setPrice(50);
	    r2.addIngredient(ing1, 3);
        r2.addIngredient(ing2, 1);
        r2.addIngredient(ing3, 3);
        r2.addIngredient(ing4, 0);
	    recipeService.save( r2 );
	    
	    //testing setting a duplicate recipe
	    try {
	    	Recipe r3 = new Recipe();
			r3.setName("lattee");
		    r3.setPrice(50);
		    r3.addIngredient(ing1, 3);
	        r3.addIngredient(ing2, 1);
	        r3.addIngredient(ing3, 3);
	        r3.addIngredient(ing4, 0);
		    recipeService.save( r3 );
		    Assertions.fail("Adding a duplicate recipe to a list should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
		    Assertions.assertEquals( 2, recipeService.count() ); //ensuring duplicate not added
		}
	    
	    Recipe r3 = new Recipe();
		r3.setName("espresso");
	    r3.setPrice(50);
	    r3.addIngredient(ing1, 1);
        r3.addIngredient(ing2, 1);
        r3.addIngredient(ing3, 5);
        r3.addIngredient(ing4, 1);
	    recipeService.save( r3 );
	    
	    //testing too many recipes
	    try {
	    	Recipe r4 = new Recipe();
			r4.setName("black");
		    r4.setPrice(20);
		    r4.addIngredient(ing1, 0);
	        r4.addIngredient(ing2, 0);
	        r4.addIngredient(ing3, 3);
	        r4.addIngredient(ing4, 0);
		    recipeService.save( r4 );
		    Assertions.fail("Adding a fourth recipe to a list should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
	    	Assertions.assertEquals( 3, recipeService.count() ); //ensuring fourth recipe not added
		}
	    
	    recipeService.deleteAll();

	    //adding an ingredient to a recipe that isn't in inventory
	    try {
		    Ingredient ing5 = new Ingredient("Marshmallow", 200);	//not in inventory
		    Recipe r5 = new Recipe();
		    r5.setName("Christmas Special");
		    r5.setPrice(100);
		    r5.addIngredient(ing1, 5);
		    r5.addIngredient(ing2, 5);
		    r5.addIngredient(ing3, 5);
		    r5.addIngredient(ing5, 5);
		    recipeService.save(r5);
		    Assertions.fail("Cannot save a recipe that has an ingredient that isn't in inventory");
	    }
	    catch (IllegalArgumentException iae) {
			Assertions.assertEquals(0, recipeService.count());
	    	
		}
	    
	  //adding an recipe with no ingredients
	    try {
		    Recipe r6 = new Recipe();
		    r6.setName("Cappucino");
		    r6.setPrice(100);
		    recipeService.save(r6);
		    Assertions.fail("Cannot have a recipe with no ingredients");
	    }
	    catch (IllegalArgumentException iae) {
			Assertions.assertEquals(0, recipeService.count());
	    	
		}
	    
	    Recipe r = new Recipe(); 
	    
		try {
			r.setName(null);
			Assertions.fail("Setting name to null should throw iae but did not");
		} catch (IllegalArgumentException iae) {
			//Exception caught, carry on
		}
		try {
			r.setName("");
			Assertions.fail("Setting name to empty string should throw iae but did not");
		} catch (IllegalArgumentException iae) {
			//Exception caught, carry on
		}
		
	    try {
	    	r.setPrice(-1);
	    	Assertions.fail("Setting price to a negative number should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
			//Exception caught, carry on
		}
	    
	    try {
	    	r.addIngredient(ing1, -1);
	    	Assertions.fail("Setting milk to a negative number should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
			//Exception caught, carry on
		}
	    
	}

	/**
	 * 
	 * This class tests how invalid recipes operations interacts with the database, 
	 * ensuring changes that are caught and not reflected in the database. 
	 * Such changes tested include updating a valid recipe to one with an ingredient 
	 * that doesn't exist in the inventory and without any ingredients.
	 */
 	@Test
	@Transactional
	public void testInvalidRecipes2(){
        
        Inventory ivt = new Inventory();
        
        ivt.addIngredients("Coffee", 500);
        ivt.addIngredients("Pumpkin Spice", 500);
        ivt.addIngredients("Milk", 500);
        ivt.addIngredients("Sugar", 500);
        
        inventoryService.save(ivt);
        
        Ingredient ing1 = new Ingredient("Coffee", 500);
        Ingredient ing2 = new Ingredient("Pumpkin Spice", 400);
        Ingredient ing3 = new Ingredient("Milk", 300);
        Ingredient ing4 = new Ingredient("Sugar", 200);
        
		Recipe r1 = new Recipe();
	    
	    r1.setName("mocha");
	    r1.setPrice(50);
        r1.addIngredient(ing1, 25);
        r1.addIngredient(ing2, 20);
        r1.addIngredient(ing3, 15);
        r1.addIngredient(ing4, 10);
        r1.setPrice( 5 );
        
	    recipeService.save( r1 );
	    
	    //updating a recipe to one with an ingredient not in inventory
	    try {
	    	Ingredient ing5 = new Ingredient("Marshmallow", 200);
	        
		    Recipe r2 = new Recipe();
		    
		    r2.setName("Christmas Special");
		    r2.setPrice(50);
	        r2.addIngredient(ing1, 10);
	        r2.addIngredient(ing2, 20);
	        r2.addIngredient(ing3, 15);
	        r2.addIngredient(ing5, 10);
	        
	        r1.updateRecipe(r2);
	        
		    recipeService.save( r1 );
		    Assertions.fail("Cannot have a recipe with ingredients not in inventory");
	    }
        catch (IllegalArgumentException iae) {
        	//Exception caught, carry on
        }
	    
	  //updating a recipe to one with no ingredients
	    try {
		    Recipe r3 = new Recipe();
		    
		    r3.setName("water");
		    r3.setPrice(50);
	        
	        r1.updateRecipe(r3);
	        
		    recipeService.save( r1 );
		    Assertions.fail("Cannot have a recipe with no ingredients");
	    }
        catch (IllegalArgumentException iae) {
        	//Exception caught, carry on
        }
	}
	
	/**
	 * 
	 * This class tests how valid recipes operations interacts with the database, 
	 * ensuring that changes are properly reflected in the database. 
	 * Such changes tested include deleting a recipe.
	 */
	@Test
	@Transactional
	public void testValidDeleteRecipes(){
	    
	    Inventory ivt = new Inventory();
	    
	    ivt.addIngredients("Milk", 500);
	    ivt.addIngredients("Sugar", 500);
	    ivt.addIngredients("Coffee", 500);
	    ivt.addIngredients("Chocolate", 500);
	    
	    inventoryService.save(ivt);
	    
		Recipe r = new Recipe();
		
	    Ingredient ing1 = new Ingredient("Milk", 500);
	    Ingredient ing2 = new Ingredient("Sugar", 500);
	    Ingredient ing3 = new Ingredient("Coffee", 500);
	    Ingredient ing4 = new Ingredient("Chocolate", 500);
	    
	    r.setName("mocha");
	    r.setPrice(50);
	    r.addIngredient(ing1, 1);
		r.addIngredient(ing2, 1);
		r.addIngredient(ing3, 2);
		r.addIngredient(ing4, 1);
	    recipeService.save( r );
	    
	    Recipe r2 = new Recipe();
	    
	    r2.setName("americano");
	    r2.setPrice(50);
	    r2.addIngredient(ing1, 1);
		r2.addIngredient(ing2, 1);
		r2.addIngredient(ing3, 2);
		r2.addIngredient(ing4, 1);
	    recipeService.save( r2 );
		
	    List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();

	    assertEquals(2, dbRecipes.size());

	    Recipe dbRecipe = dbRecipes.get(0);
	    Recipe dbRecipe2 = dbRecipes.get(1);
	    
	    recipeService.delete(dbRecipe2);
	    
	    List<Recipe> dbRecipes2 = (List<Recipe>) recipeService.findAll();

	    assertEquals(1, dbRecipes2.size());
	    
	    Recipe dbRecipe1 = dbRecipes.get(0);

	    assertEquals(r.getName(), dbRecipe1.getName());
	    assertEquals(r.getPrice(), dbRecipe1.getPrice());
	    assertEquals(r.getIngredients().get(0), dbRecipe1.getIngredients().get(0));
	    assertEquals(r.getIngredients().get(1), dbRecipe1.getIngredients().get(1));
	    assertEquals(r.getIngredients().get(2), dbRecipe1.getIngredients().get(2));
	    assertEquals(r.getIngredients().get(3), dbRecipe1.getIngredients().get(3));
	    
	    recipeService.delete(dbRecipe);
	    
	    List<Recipe> dbRecipes3 = (List<Recipe>) recipeService.findAll();
	    
	    assertEquals(0, dbRecipes3.size());
	}
	

	/**
	 * 
	 * This class tests how invalid recipes operations interacts with the database, 
	 * ensuring changes that are caught and not reflected in the database. 
	 * Such changes tested include deleting a recipe that doesn't exist or deleting all 
	 * recipes when no recipes exist.
	 * 
	 */
	@Test
	@Transactional
	public void testInvalidDeleteRecipes(){

		Inventory ivt = new Inventory();
        
        ivt.addIngredients("Coffee", 500);
        ivt.addIngredients("Chocolate", 500);
        ivt.addIngredients("Milk", 500);
        ivt.addIngredients("Sugar", 500);
        
        inventoryService.save(ivt);
        
	    Ingredient ing1 = new Ingredient("Milk", 500);
	    Ingredient ing2 = new Ingredient("Sugar", 400);
	    Ingredient ing3 = new Ingredient("Coffee", 300);
	    Ingredient ing4 = new Ingredient("Chocolate", 200);
	    

		//testing deleting a ticket that has already been deleted
	    try {
			Recipe r1 = new Recipe();
			r1.setName("mocha");
		    r1.setPrice(50);
		    r1.addIngredient(ing1, 1);
			r1.addIngredient(ing2, 1);
			r1.addIngredient(ing3, 2);
			r1.addIngredient(ing4, 1);
		    recipeService.save( r1 );
		    
		    recipeService.delete( r1 );
		    
		    Assertions.assertEquals( 0, recipeService.count() );
	    	recipeService.delete( r1 );
	    	Assertions.fail("Deleting a recipe that has already been deleted from delete (DNE) should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
	    	Assertions.assertEquals( 0, recipeService.count() );
		}
	    
	    //testing deleting a ticket that has already been deleted
	    try {
			Recipe r2 = new Recipe();
			r2.setName("black");
		    r2.setPrice(20);
		    r2.addIngredient(ing1, 0);
			r2.addIngredient(ing2, 0);
			r2.addIngredient(ing3, 3);
			r2.addIngredient(ing4, 0);
		    recipeService.save( r2 );
		    
		    recipeService.deleteAll();
		    
		    Assertions.assertEquals( 0, recipeService.count() );
	    	recipeService.delete( r2 );
	    	Assertions.fail("Deleting a recipe that has already been deleted from deleteAll (DNE) should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
	    	Assertions.assertEquals( 0, recipeService.count() );
		}
	}

	//Have an inventory and when adding an ingredient it doesn't duplicate it, but instead updates the count
	
	//USE INGREDIENT
}
