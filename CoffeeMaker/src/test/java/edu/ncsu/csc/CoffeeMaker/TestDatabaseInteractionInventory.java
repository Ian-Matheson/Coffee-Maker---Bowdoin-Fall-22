package edu.ncsu.csc.CoffeeMaker;

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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteractionInventory {

	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private IngredientService ingredientService;
	
	@Autowired
	private RecipeService recipeService;
	
	/**
	 * BeforeEach clears the current Inventory Service.
	 * @throws Exception if fails to build
	 */
	@BeforeEach
	public void setUp() throws Exception {
		inventoryService.deleteAll();
		recipeService.deleteAll();
		ingredientService.deleteAll();
		
	}
	
	/**
	 * Tests the database interaction of the inventory using the methods of the inventory class.
	 */
	@Test
	@Transactional
	public void testValidInventory(){
		
		Inventory i = new Inventory();

	    Ingredient i1 =  new Ingredient("Sugar", 200);
    	Ingredient i2 =  new Ingredient("Milk", 500);
    	Ingredient i3 =  new Ingredient("Chocolate", 1000);
    	ingredientService.save(i1);
    	ingredientService.save(i2);
    	ingredientService.save(i3);
    	
    	Recipe r1 = new Recipe();
    	r1.setName("mocha");
    	r1.setPrice(2);
    	r1.addIngredient(i1, 2);
    	r1.addIngredient(i2, 4);
    	r1.addIngredient(i3, 1);
    	
    	Recipe r2 = new Recipe();
    	r2.setName("americano");
    	r2.setPrice(2);
    	r2.addIngredient(i1, 201);
    	r2.addIngredient(i2, 4);
    	r2.addIngredient(i3, 1);

    	i.addIngredients("Sugar", 200);
    	i.addIngredients("Milk", 500);
    	i.addIngredients("Chocolate", 1000);
    	
    	inventoryService.save(i);
    	Inventory DBinventory= inventoryService.getInventory();
    	
	    Assertions.assertEquals(200, DBinventory.checkIngredient(i1), "Returns the amount of the checked ingredient.");
	    Assertions.assertEquals(500, DBinventory.checkIngredient(i2), "Returns the amount of the checked ingredient.");
	    Assertions.assertEquals(1000, DBinventory.checkIngredient(i3), "Returns the amount of the checked ingredient.");

	    Assertions.assertEquals("Sugar", DBinventory.getIngredients().get(0).getName(), "Returns the name of the first ingredient.");
	    Assertions.assertEquals("Milk", DBinventory.getIngredients().get(1).getName(), "Returns the name of the second ingredient.");
	    Assertions.assertEquals("Chocolate", DBinventory.getIngredients().get(2).getName(), "Returns the name of the third ingredient.");

	    Assertions.assertEquals(200, DBinventory.getIngredients().get(0).getAmount(), "Returns the amount of the first ingredient.");
	    Assertions.assertEquals(500, DBinventory.getIngredients().get(1).getAmount(), "Returns the amount of the second ingredient.");
	    Assertions.assertEquals(1000, DBinventory.getIngredients().get(2).getAmount(), "Returns the amount of the third ingredient.");

	    Assertions.assertTrue(DBinventory.enoughIngredients(r1), "There are enough ingredients to make this recipe.");
	    Assertions.assertFalse(DBinventory.enoughIngredients(r2), "There are not enough ingredients to make this recipe.");

	    DBinventory.useIngredients(r1);
    	
	    Assertions.assertEquals(198, i.getIngredients().get(0).getAmount(), "Returns the amount of the first ingredient.");
	    Assertions.assertEquals(496, i.getIngredients().get(1).getAmount(), "Returns the amount of the second ingredient.");
	    Assertions.assertEquals(999, i.getIngredients().get(2).getAmount(), "Returns the amount of the third ingredient.");
    	
	}
	
	/**
	 * Tests the database interaction of the inventory using the invalid methods of the inventory class.
	 * 
	 * @Test
	@Transactional
	public void testInvalidInventory(){
		
		Inventory i = new Inventory();
		inventoryService.save(i);
		Inventory i2 = new Inventory();
		try {
			inventoryService.save(i2);
			Assertions.fail("Can not have two inventories.");
		} catch (IllegalArgumentException e) {
			//Exception caught, carry on
		}
	
		
	}
	 */
	
	
	
}
