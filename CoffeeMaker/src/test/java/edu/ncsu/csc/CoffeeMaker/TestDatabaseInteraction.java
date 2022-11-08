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

import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;



@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )


public class TestDatabaseInteraction {
	
	@Autowired
	private RecipeService recipeService;
	
	
	/**
	 * BeforeEach clears the current Recipe Service.
	 * @throws Exception if fails to build
	 */
	@BeforeEach
	public void setUp() throws Exception {
		recipeService.deleteAll();
	}
	
	@Test
	@Transactional
	public void testValidRecipes(){
		
		Recipe r = new Recipe();
	    
	    r.setName("mocha");
	    r.setPrice(50);
	    r.setMilk(1);
	    r.setSugar(1);
	    r.setCoffee(2);
	    r.setChocolate(1);
	    recipeService.save( r );
		
	    List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();

	    assertEquals(1, dbRecipes.size());

	    Recipe dbRecipe = dbRecipes.get(0);

	    assertEquals(r.getName(), dbRecipe.getName());
	    assertEquals(r.getPrice(), dbRecipe.getPrice());
	    assertEquals(r.getMilk(), dbRecipe.getMilk());
	    assertEquals(r.getSugar(), dbRecipe.getSugar());
	    assertEquals(r.getCoffee(), dbRecipe.getCoffee());
	    assertEquals(r.getChocolate(), dbRecipe.getChocolate());
	    
	    Recipe recipeBN = recipeService.findByName("mocha");
	    assertEquals(r.getName(), recipeBN.getName());
	    
	    dbRecipe.setPrice(15);
	    dbRecipe.setSugar(12);
	    dbRecipe.setCoffee(4);
	    dbRecipe.setChocolate(2);
	    recipeService.save(dbRecipe);

        assertEquals( 1, recipeService.count() );

        assertEquals( 15, (int) ( (Recipe) recipeService.findAll().get( 0 ) ).getPrice() );
        assertEquals(12, dbRecipe.getSugar());
        assertEquals(4, dbRecipe.getCoffee());
        assertEquals(2, dbRecipe.getChocolate());
	       
	}
	
	/**
	 * @Test
	@Transactional
	public void testInvalidRecipes(){
		
		Recipe r1 = new Recipe();
		r1.setName("mocha");
	    r1.setPrice(50);
	    r1.setMilk(1);
	    r1.setSugar(1);
	    r1.setCoffee(2);
	    r1.setChocolate(1);
	    recipeService.save( r1 );
	    
	    Recipe r2 = new Recipe();
		r2.setName("latte");
	    r2.setPrice(50);
	    r2.setMilk(3);
	    r2.setSugar(1);
	    r2.setCoffee(3);
	    r2.setChocolate(0);
	    recipeService.save( r2 );
	    
	    //testing setting a duplicate recipe
	    try {
	    	Recipe r3 = new Recipe();
			r3.setName("latte");
		    r3.setPrice(50);
		    r3.setMilk(3);
		    r3.setSugar(1);
		    r3.setCoffee(3);
		    r3.setChocolate(0);
		    recipeService.save( r3 );
		    Assertions.fail("Adding a duplicate recipe to a list should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
		    Assertions.assertEquals( 2, recipeService.count() ); //ensuring duplicate not added
		}
	    
	    Recipe r3 = new Recipe();
		r3.setName("espresso");
	    r3.setPrice(50);
	    r3.setMilk(1);
	    r3.setSugar(1);
	    r3.setCoffee(5);
	    r3.setChocolate(1);
	    recipeService.save( r3 );
	    
	    //testing too many recipes
	    try {
	    	Recipe r4 = new Recipe();
			r4.setName("black");
		    r4.setPrice(20);
		    r4.setMilk(0);
		    r4.setSugar(0);
		    r4.setCoffee(3);
		    r4.setChocolate(0);
		    recipeService.save( r4 );
		    Assertions.fail("Adding a fourth recipe to a list should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
	    	Assertions.assertEquals( 3, recipeService.count() ); //ensuring fourth recipe not added
		}
	    
	    recipeService.deleteAll();
	    
	    Recipe r = new Recipe();
	    
		//testing setName
		try {
			r.setName(null);
			Assertions.fail("Setting name to null should throw iae but did not");
		} catch (IllegalArgumentException iae) {
		}
		
		try {
			r.setName("");
			Assertions.fail("Setting name to empty string should throw iae but did not");
		} catch (IllegalArgumentException iae) {
		}
	
		//testing set price
	    try {
	    	r.setPrice(-1);
	    	Assertions.fail("Setting price to a negative number should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
		}
	    
	    //testing set milk
	    try {
	    	r.setMilk(-1);
	    	Assertions.fail("Setting milk to a negative number should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
		}
	    
	    //testing set sugar
	    try {
	    	r.setSugar(-1);
	    	Assertions.fail("Setting milk to a negative number should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
		}
	    
	    //testing set coffee
	    try {
	    	r.setCoffee(-1);
	    	Assertions.fail("Setting coffee to a negative number should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
		}
	    
	    //testing set chocolate
	    try {
	    	r.setChocolate(-1);
	    	Assertions.fail("Setting chocolate to a negative number should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
		}
	}
	 */
	
	
	@Test
	@Transactional
	public void testValidDeleteRecipes(){
		
		Recipe r = new Recipe();
	    
	    r.setName("mocha");
	    r.setPrice(50);
	    r.setMilk(1);
	    r.setSugar(1);
	    r.setCoffee(2);
	    r.setChocolate(1);
	    recipeService.save( r );
	    
	    Recipe r2 = new Recipe();
	    
	    r2.setName("americano");
	    r2.setPrice(50);
	    r2.setMilk(1);
	    r2.setSugar(1);
	    r2.setCoffee(2);
	    r2.setChocolate(1);
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
	    assertEquals(r.getMilk(), dbRecipe1.getMilk());
	    assertEquals(r.getSugar(), dbRecipe1.getSugar());
	    assertEquals(r.getCoffee(), dbRecipe1.getCoffee());
	    assertEquals(r.getChocolate(), dbRecipe1.getChocolate());
	    
	    recipeService.delete(dbRecipe);
	    
	    List<Recipe> dbRecipes3 = (List<Recipe>) recipeService.findAll();
	    
	    assertEquals(0, dbRecipes3.size());

	}
	
	/**
	 * @Test
	@Transactional
	public void testInvalidDeleteRecipes(){

		//testing deleting a ticket that has already been deleted
	    try {
			Recipe r1 = new Recipe();
			r1.setName("mocha");
		    r1.setPrice(50);
		    r1.setMilk(1);
		    r1.setSugar(1);
		    r1.setCoffee(2);
		    r1.setChocolate(1);
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
		    r2.setMilk(0);
		    r2.setSugar(0);
		    r2.setCoffee(3);
		    r2.setChocolate(0);
		    recipeService.save( r2 );
		    
		    recipeService.deleteAll();
		    
		    Assertions.assertEquals( 0, recipeService.count() );
	    	recipeService.delete( r2 );
	    	Assertions.fail("Deleting a recipe that has already been deleted from deleteAll (DNE) should throw iae but did not");
	    } catch (IllegalArgumentException iae) {
	    	Assertions.assertEquals( 0, recipeService.count() );
		}
	}
	 */
	
}
