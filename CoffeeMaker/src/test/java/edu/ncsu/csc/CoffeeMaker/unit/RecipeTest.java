package edu.ncsu.csc.CoffeeMaker.unit;


import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.validation.ConstraintViolationException;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This class tests the functionality of the recipe class.
 * 
 * @author jsoeder
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

	
	/** allows inventory to be added to database*/
	@SuppressWarnings("unused")
	private MockMvc mvc;


	@Autowired
	private WebApplicationContext context;
	

	@SuppressWarnings("unused")

	@Autowired
 	private RecipeService recipeService;
	
	
    @Autowired
    private RecipeService service;


    @BeforeEach
    public void setup () {
        service.deleteAll();
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }
    
    /**
     * Ensures that the check recipe properly checks the amount values of each ingredient in a recipe.
     */
    @Test
    @Transactional
    public void testCheckRecipe() {
    	
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(2);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);
    	
    	Recipe r2 = new Recipe();
    	r2.setName("moche");
    	r2.setPrice(4);
    	r2.addIngredient(i1, 0);
    	r2.addIngredient(i2, 0);
    	r2.addIngredient(i3, 0);
    	
    	Recipe r3 = new Recipe();
    	r3.setName("mochi");
    	r3.setPrice(5);
    	
    	Assertions.assertFalse(r1.checkRecipe(), "Returns false if the ingredient amounts are not all zero" );
    	Assertions.assertTrue(r2.checkRecipe(), "Returns true if the ingredient amounts are all zero" );
    	Assertions.assertTrue(r3.checkRecipe(), "Returns true if there are no ingredients" );

    }
    
    /**
     * Ensures that the get name method returns the correct name for a recipe. 
     */
    @Test
    @Transactional
    public void testGetName() {
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(2);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);
    	
    	Recipe r2 = new Recipe();
    	r2.setName("moche");
    	r2.setPrice(4);
    	r2.addIngredient(i1, 1);
    	r2.addIngredient(i2, 1);
    	r2.addIngredient(i3, 1);
    	
    	Assertions.assertEquals( "mocha", r1.getName(), "Returns the name of the recipe" );
    	Assertions.assertEquals( "moche", r2.getName(), "Returns the name of the recipe" );
    }

    /**
     * Ensures that exceptions are thrown when invalid name arguments are passed into the set name method.
     */
    @Test
    @Transactional
    public void testInvalidSetName () {
    	Recipe r1 = new Recipe();
    	try {
    		r1.setName("");
    		Assertions.fail("Should throw an illegal argument exception, name not valid"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
     
    	Recipe r2 = new Recipe();
    	try {
    		r2.setName(null);
    		Assertions.fail("Should throw an illegal argument exception, name not valid"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}  
    }
    

    /**
     * Ensures that the price is set as intended when a valid argument is passed into the method.
     */
    @Test
    @Transactional
    public void testValidGetSetPrice () {
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(0);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);
    	
    	Recipe r2 = new Recipe();
    	r2.setName("moche");
    	r2.setPrice(10);
    	r2.addIngredient(i1, 1);
    	r2.addIngredient(i2, 1);
    	r2.addIngredient(i3, 1);
    	
    	Assertions.assertEquals( 0, r1.getPrice(), "Returns the price of the recipe" );
    	Assertions.assertEquals( 10, r2.getPrice(), "Returns the price of the recipe" );
    }
    
    /**
     * Ensures that exceptions are thrown when invalid prcie arguments are passed into the set price method.
     */
    @Test
    @Transactional
    public void testInvalidSetPrice () {
    	Recipe r1 = new Recipe();
    	try {
    		r1.setPrice(-1);
    		Assertions.fail("Should throw an illegal argument exception, price not valid"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
     
    	Recipe r2 = new Recipe();
    	r2.setPrice(5);
    	try {
    		r2.setPrice(10);
    		r2.setPrice(null);
    		Assertions.fail("Should throw an illegal argument exception, price not valid"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}  
    }

    /**
     * Ensures that the desired ingredients are added to a recipe with correct amounts.
     */
    @Test
    @Transactional
    public void testValidAddIngredient () {
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(0);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);
    	
    	Assertions.assertEquals("Sugar", r1.getIngredients().get(0).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(2, r1.getIngredients().get(0).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Milk", r1.getIngredients().get(1).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(4, r1.getIngredients().get(1).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Chocolate", r1.getIngredients().get(2).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(1, r1.getIngredients().get(2).getAmount(), "The new ingredient is included in the list" );

    	Recipe r2 = new Recipe();
    	r2.setName("moche");
    	r2.setPrice(10);
    	r2.addIngredient(i1, 1);
    	r2.addIngredient(i2, 1);
    	r2.addIngredient(i3, 1);

    	Assertions.assertEquals("Sugar", r2.getIngredients().get(0).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(1, r2.getIngredients().get(0).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Milk", r2.getIngredients().get(1).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(1, r2.getIngredients().get(1).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Chocolate", r2.getIngredients().get(2).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(1, r2.getIngredients().get(2).getAmount(), "The new ingredient is included in the list" );

    }
    
    /**
     * Ensures that exceptions are thrown when invalid ingredient/amount arguments are passed into the add ingredient method.
     */
    @Test
    @Transactional
    public void testInvalidAddIngredient() {
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(0);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);
    	try {
        	r1.addIngredient(i3, 2);
    		Assertions.fail("Should throw an illegal argument exception, duplicate ingredient"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    	try {
        	r1.addIngredient(i2, 4);
    		Assertions.fail("Should throw an illegal argument exception, duplicate ingredient"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    	
    	Recipe r2 = new Recipe();
    	r2.setName("moche");
    	r2.setPrice(10);
    	r2.addIngredient(i1, 1);
    	r2.addIngredient(i2, 1);
    	try {
        	r2.addIngredient(null, 2);
    		Assertions.fail("Should throw an illegal argument exception, null ingredient"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    	try {
        	r2.addIngredient(i3, -1);
    		Assertions.fail("Should throw an illegal argument exception, negative ingredient amount"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    }
  
    
    /**
     * Ensure that the correct ingredients within a recipe are edited, and check that the amounts
     * of the edited ingredients is updated. 
     */
    @Test
    @Transactional
    public void testValidEditIngredient () {
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(0);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);
    	r1.editIngredient(i1, 3);
    	r1.editIngredient(i2, 5);
    	r1.editIngredient(i3, 2);

    	Assertions.assertEquals("Sugar", r1.getIngredients().get(0).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(3, r1.getIngredients().get(0).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Milk", r1.getIngredients().get(1).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(5, r1.getIngredients().get(1).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Chocolate", r1.getIngredients().get(2).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(2, r1.getIngredients().get(2).getAmount(), "The new ingredient is included in the list" );

    	Recipe r2 = new Recipe();
    	r2.setName("moche");
    	r2.setPrice(10);
    	r2.addIngredient(i1, 1);
    	r2.addIngredient(i2, 1);
    	r2.addIngredient(i3, 1);
    	r2.editIngredient(i1, 2);
    	r2.editIngredient(i2, 2);
    	r2.editIngredient(i3, 2);

    	Assertions.assertEquals("Sugar", r2.getIngredients().get(0).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(2, r2.getIngredients().get(0).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Milk", r2.getIngredients().get(1).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(2, r2.getIngredients().get(1).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Chocolate", r2.getIngredients().get(2).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(2, r2.getIngredients().get(2).getAmount(), "The new ingredient is included in the list" );

    }
    
    /**
     * Ensures that exceptions are thrown when invalid Ingredient/amount arguments are passed into the 
     * edit ingredient method.
     */
    @Test
    @Transactional
    public void testInvalidEditIngredient() {
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);
    	Ingredient i4 =  new Ingredient("Pumpkin Spice", 300);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(0);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);
    	try {
        	r1.editIngredient(i4, 2);
    		Assertions.fail("Should throw an illegal argument exception, ingredient not in recipe"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    	
    	Recipe r2 = new Recipe();
    	r2.setName("moche");
    	r2.setPrice(10);
    	r2.addIngredient(i1, 1);
    	r2.addIngredient(i2, 1);
    	try {
        	r2.editIngredient(null, 2);
    		Assertions.fail("Should throw an illegal argument exception, null ingredient"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    	try {
        	r2.editIngredient(i3, -1);
    		Assertions.fail("Should throw an illegal argument exception, negative ingredient amount"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    }

    
    /**
     * Checks that the get ingredients method returns a list with only the correct ingredients.
     */
    @Test
    @Transactional
    public void testGetIngredients () {
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(2);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);

    	Assertions.assertEquals( 3, r1.getIngredients().size(), "The new ingredients are included in the list" );
    	
    	Assertions.assertEquals("Sugar", r1.getIngredients().get(0).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(2, r1.getIngredients().get(0).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Milk", r1.getIngredients().get(1).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(4, r1.getIngredients().get(1).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Chocolate", r1.getIngredients().get(2).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(1, r1.getIngredients().get(2).getAmount(), "The new ingredient is included in the list" );

    	Recipe r2 = new Recipe();
    	r2.setName("moche");
    	r2.setPrice(4);
    	r2.addIngredient(i1, 1);
    	r2.addIngredient(i2, 1);
    	r2.addIngredient(i3, 1);
        
    	Assertions.assertEquals("Sugar", r2.getIngredients().get(0).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(1, r2.getIngredients().get(0).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Milk", r2.getIngredients().get(1).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(1, r2.getIngredients().get(1).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Chocolate", r2.getIngredients().get(2).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(1, r2.getIngredients().get(2).getAmount(), "The new ingredient is included in the list" );

    }
   
    /**
     * Ensures that ingredients are removed from a recipe.
     */
    @Test
    @Transactional
    public void testValidRemoveIngredient () {
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);

    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(2);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);
    	r1.removeIngredient("Sugar");

    	Assertions.assertEquals( 2, r1.getIngredients().size(), "The list decreases in size" );
    
    	Assertions.assertEquals("Milk", r1.getIngredients().get(0).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(4, r1.getIngredients().get(0).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Chocolate", r1.getIngredients().get(1).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(1, r1.getIngredients().get(1).getAmount(), "The new ingredient is included in the list" );

    	
    	r1.removeIngredient("Milk");

    	Assertions.assertEquals( 1, r1.getIngredients().size(), "The list decreases in size" );
    	
    	Assertions.assertEquals("Chocolate", r1.getIngredients().get(0).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(1, r1.getIngredients().get(0).getAmount(), "The new ingredient is included in the list" );

    }
    
    /**
     * Ensures that an exception is thrown when a user tries to remove an ingredient from a recipe that it is 
     * not a part of or if the last ingredient in a recipe is removed.
     */
    @Test
    @Transactional
    public void testInvalidRemoveIngredient () {
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(2);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);
    	r1.removeIngredient("Sugar");
    	r1.removeIngredient("Milk");

    	try {
        	r1.removeIngredient("Chocolate");
    		Assertions.fail("Should throw an illegal argument exception, can't remove last ingredient"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}

    	Recipe r2 = new Recipe();
    	r2.setName("moche");
    	r2.setPrice(4);
    	r2.addIngredient(i1, 1);
    	r2.addIngredient(i2, 1);
    	r2.addIngredient(i3, 1);
    	r2.removeIngredient("Sugar");   
    	try {
        	r2.removeIngredient("Panda");
    		Assertions.fail("Should throw an illegal argument exception, ingredient not in recipe"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    	try {
        	r2.removeIngredient(null);
    		Assertions.fail("Should throw an illegal argument exception, ingredient null"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    }

    /**
     * Ensures that the update recipe method resets all of the necessary characteristics of the recipe.
     */

    @Test
    @Transactional
    public void testUpdateRecipe () {
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);
    	Ingredient i4 =  new Ingredient("Cream", 100);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(2);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);

    	Assertions.assertEquals("Sugar", r1.getIngredients().get(0).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(2, r1.getIngredients().get(0).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Milk", r1.getIngredients().get(1).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(4, r1.getIngredients().get(1).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Chocolate", r1.getIngredients().get(2).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(1, r1.getIngredients().get(2).getAmount(), "The new ingredient is included in the list" );
    	
    	Recipe r2 = new Recipe();
    	r2.setName("mochi");
    	r2.setPrice(3);
    	r2.addIngredient(i1, 3);
    	r2.addIngredient(i3, 2);
    	r2.addIngredient(i4, 2);

    	
    	r1.updateRecipe(r2);
    	
    	Assertions.assertEquals( 3, r1.getIngredients().size(), "The new ingredients are included in the list" );

    	Assertions.assertEquals("Sugar", r1.getIngredients().get(0).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(3, r1.getIngredients().get(0).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Chocolate", r1.getIngredients().get(1).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(2, r1.getIngredients().get(1).getAmount(), "The new ingredient is included in the list" );
    	Assertions.assertEquals("Cream", r1.getIngredients().get(2).getName(), "The new ingredient is included in the list" );
    	Assertions.assertEquals(2, r1.getIngredients().get(2).getAmount(), "The new ingredient is included in the list" );

    	Assertions.assertEquals( "mochi", r1.getName(), "The new ingredient name is not set" );
    	Assertions.assertEquals( 3, r1.getPrice(), "The new ingredient price is not set" );
    }
    

    /**
     * Ensures that the toString method returns a string representation of the recipe.
     */
    @Test
    @Transactional
    public void testToString() {
    	Ingredient i1 =  new Ingredient("Sugar", 20);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 100);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(2);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);
    	
    	String rString = "Recipe mocha with ingredients " + r1.getIngredients() + "]";
    	System.out.println();
    	System.out.println(rString);
    	System.out.println(r1.toString());
    	System.out.println();
    	Assertions.assertEquals( rString, r1.toString(), "The toString method did not work properly" );

	}

    /**
     * Ensures that the equals method works properly for the recipe class. 
     */
    @SuppressWarnings("unlikely-arg-type")
	@Test 
    @Transactional
    public void testEquals() {
    	Recipe r = new Recipe(); 
    	
    	Assertions.assertFalse(r.equals(null)); 
    	Assertions.assertTrue(r.equals(r)); 
    	
    	String randomString = "aslkdfhalkfj"; 
    	Assertions.assertFalse(r.equals(randomString)); 
    	
    	r.setName(randomString);
    	Recipe anotherRecipe = new Recipe(); 
    	Assertions.assertFalse(r.equals(anotherRecipe)); 
    	
    	r.setName("Sophia");
    	anotherRecipe.setName("Ian");
    	Assertions.assertFalse(r.equals(anotherRecipe)); 
    	
    	anotherRecipe.setName("Sophia");
    	Assertions.assertTrue(r.equals(anotherRecipe)); 

    }
    
    @Test
    public void testToString() { 
    	Recipe r = new Recipe(); 
    	r.setName("Sophia"); 
    	Assertions.assertEquals("Sophia", r.toString());
    	Assertions.assertFalse("Ian".equals(r.toString())); 
    }

    @Test 
    public void testEquals() {
    	Recipe r = new Recipe(); 
    	
    	Assertions.assertFalse(r.equals(null)); 
    	Assertions.assertTrue(r.equals(r)); 
    	
    	String string = "aslkdfhalkfj"; 
    	Assertions.assertFalse(r.equals(string)); 
    	
    	r.setName(null);
    	Recipe anotherRecipe = new Recipe(); 
    	Assertions.assertFalse(r.equals(anotherRecipe)); 
    	
    	r.setName("Sophia");
    	anotherRecipe.setName("Ian");
    	Assertions.assertFalse(r.equals(anotherRecipe)); 
    	
    	anotherRecipe.setName("Sophia");
    	Assertions.assertTrue(r.equals(anotherRecipe)); 
    }

}