package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

/**
 * This class tests the Ingredient Class. 
 * 
 * @author jsoeder
 *
 */
public class IngredientTest {

    @BeforeEach
    public void setup () {
    	
    }
    
    
    /**
	 * Ensures that the getId method function properly with a variety of ID values.
	 */
    @Transactional
    @Test
	public void testGetId() {

    	Ingredient i1 =  new Ingredient("Sugar", 500);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 5);

    	i1.setId((long) 1);
    	i2.setId((long) 2);
    	i3.setId((long) 3);
    	
        Assertions.assertEquals( 1, i1.getId(), "Creating an ingredient should result in a correct id value for sugar" );
        Assertions.assertEquals( 2, i2.getId(), "Creating an ingredient should result in a correct id value for milk" );
        Assertions.assertEquals( 3, i3.getId(), "Creating an ingredient should result in a correct id value for chocolate" );

	}

	/**
	 *  Ensures that the set id method functions with a variety of unique id values.
	 */
    @Transactional
    @Test
	public void testValidSetId() {
    	
    	Ingredient i1 =  new Ingredient("Sugar", 500);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 5);
    	
    	i1.setId((long) 100);
    	i2.setId((long) 101);
    	i3.setId((long) 102);
        
    	Assertions.assertEquals( 100, i1.getId(), "Setting an ingredient ID should result in a correct id value for sugar" );
        Assertions.assertEquals( 101, i2.getId(), "Setting an ingredient ID should result in a correct id value for milk" );
        Assertions.assertEquals( 102, i3.getId(), "Setting an ingredient ID should result in a correct id value for chocolate" );
	}
    
    /**
	 *  Ensures that exceptions are caught when the set id method is called with an invalid argument.
	 */
    @Transactional
    @Test
	public void testInvalidSetId() {
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	
    	try {
        	i2.setId((long) -1); 
			Assertions.fail("Should throw an illegal argument exception, negative id"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    }
    
	/**
	 *  Ensures that the getName method returns the correct names for the ingredients. 
	 */
    @Transactional
    @Test
	public void testGetName() {
    	
    	Ingredient i1 =  new Ingredient("Sugar", 500);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 5);

        Assertions.assertEquals( "Sugar", i1.getName(), "The get name method should result in the correct name for the ingredient" );
        Assertions.assertEquals( "Milk", i2.getName(), "The get name method should result in the correct name for the ingredient" );
        Assertions.assertEquals( "Chocolate", i3.getName(), "The get name method should result in the correct name for the ingredient" );

	}

	/**
	 * Ensures that the set name method updates ingredient names. 
	 */
    @Transactional
    @Test
	public void testValidSetName() {
    	Ingredient i1 =  new Ingredient("Sugar", 500);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 5);
        
        i1.setName("Milk");
        i2.setName("Pumpkin Spice");
        i3.setName("Sprinkles");

        Assertions.assertEquals( "Milk", i1.getName(), "The get name method should result in the correct name for the ingredient" );
        Assertions.assertEquals( "Pumpkin Spice", i2.getName(), "The get name method should result in the correct name for the ingredient" );
        Assertions.assertEquals( "Sprinkles", i3.getName(), "The get name method should result in the correct name for the ingredient" );

        
    }
   
	/**
	 * Ensures that exceptions are caught when the set name method is called with an invalid argument.
	 */
	@Transactional
	@Test
	public void testInvalidSetName() {
    	Ingredient i1 =  new Ingredient("Sugar", 500);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	
    	try {
        	i1.setName(null);
			Assertions.fail("Should throw an illegal argument exception, null"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    	try {
        	i2.setName("");
			Assertions.fail("Should throw an illegal argument exception, empty string"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
    	
	}

	/**
	 * Ensure that the getAmount method returns the proper amount for each ingredient. 
	 */
    @Transactional
    @Test
	public void testGetAmount() {
		Ingredient i1 =  new Ingredient("Sugar", 500);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 5);

        Assertions.assertEquals( 500, i1.getAmount(), "The get amount method should result in the correct amount for the ingredient" );
        Assertions.assertEquals( 50, i2.getAmount(), "The get amount method should result in the correct amount for the ingredient" );
        Assertions.assertEquals( 5, i3.getAmount(), "The get amount method should result in the correct amount for the ingredient" );

	}

	/**
	 * Ensures that the setAmount method updates the amount for the necessary ingredient.
	 */
    @Transactional
    @Test
	public void testValidSetAmount() {
    	Ingredient i1 =  new Ingredient("Sugar", 500);
    	Ingredient i2 =  new Ingredient("Milk", 50);
    	Ingredient i3 =  new Ingredient("Chocolate", 5);
    	
    	i1.setAmount(0);
    	i2.setAmount(1);
    	i3.setAmount(1000);
    	
    	Assertions.assertEquals( 0, i1.getAmount(), "The get amount method should result in the correct amount for the ingredient" );
        Assertions.assertEquals( 1, i2.getAmount(), "The get amount method should result in the correct amount for the ingredient" );
        Assertions.assertEquals( 1000, i3.getAmount(), "The get amount method should result in the correct amount for the ingredient" );
	}
    
    /**
	 * Ensures that exceptions are caught when the set id method is called with an invalid argument.
	 */
    @Transactional
    @Test
	public void testInvalidSetAmount() {
    	Ingredient i1 =  new Ingredient("Sugar", 500);
    	
    	try {
    		i1.setAmount(-1);
    		Assertions.fail("Should throw an illegal argument exception, empty string"); 
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
    	}

	}
	    
    /**
     * Ensures that the toString method returns a proper string representation of an ingredient object. 
     */
    @Transactional
    @Test
	public void testToString() {
    	Ingredient i1 =  new Ingredient("Sugar", 500);
    	Assertions.assertEquals( "Ingredient [name=Sugar, amount=500]", i1.toString(), "The toString method returns an ingredient as a string" );

	}

}