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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;
    

    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();
        inventoryService.save( ivt );
    }
    
    
    
    /**
     * Tests adding ingredients that do and don't already exist.
     */
//    @Transactional
    @Test
    public void testAddAndEditValidIngredientsInventory () {
    	inventoryService.deleteAll();
        Inventory ivt = inventoryService.getInventory();

        ivt.addIngredients("Coffee", 500);
        ivt.addIngredients("Pumpkin Spice", 500);
        ivt.addIngredients("Milk", 500);
        ivt.addIngredients("Sugar", 500);

        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 500, ivt.getIngredients().get(0).getAmount(), "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 500, ivt.getIngredients().get(1).getAmount(), "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 500, ivt.getIngredients().get(2).getAmount(), "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 500, ivt.getIngredients().get(3).getAmount(), "Adding to the inventory should result in correctly-updated values chocolate" );

        
        //editing ingredients that are already in inventory
        ivt.addIngredients("Coffee", 600);
        ivt.addIngredients("Milk", 400);
        
        inventoryService.save( ivt );
        
        ivt = inventoryService.getInventory();

        //index might be off
        Assertions.assertEquals( 600, ivt.getIngredients().get(0).getAmount(), "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 500, ivt.getIngredients().get(1).getAmount(), "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 400, ivt.getIngredients().get(2).getAmount(), "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 500, ivt.getIngredients().get(3).getAmount(), "Adding to the inventory should result in correctly-updated values chocolate" );

    }

    @Test
    @Transactional
    /**
     * Tests adding ingredients with invalid amounts that don't already exist.
     */
    public void testAddInvalidIngredientsInventory () {
        final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredients("Coffee", 500);
        ivt.addIngredients("Pumpkin Spice", 1);
        ivt.addIngredients("Milk", 0);
        
        try {
        	ivt.addIngredients("", 500);
        	Assertions.fail("Should throw an illegal argument exception, empty");
        }
        catch ( final IllegalArgumentException iae ) {
        	//Exception caught, carry on
        }
        try {
        	ivt.addIngredients(null, 500);
        	Assertions.fail("Should throw an illegal argument exception, null");
        }
        catch ( final IllegalArgumentException iae ) {
        	//Exception caught, carry on
        }
        try {
            ivt.addIngredients("Sugar", -1);
            Assertions.fail("Cannot add an ingredient with a negative number");
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredients().get(0).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 1, ivt.getIngredients().get(1).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 0, ivt.getIngredients().get(2).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
        }
    }

    
    
    
    
    @Test
    @Transactional
    /**
     * Tests adding ingredients with invalid amounts that do already exist.
     */
    public void testEditInvalidIngredientsInventory () {
    	final Inventory ivt = inventoryService.getInventory();

        ivt.addIngredients("Coffee", 500);
        ivt.addIngredients("Pumpkin Spice", 500);
        ivt.addIngredients("Milk", 500);
        ivt.addIngredients("Sugar", 500);
        
        try {
            ivt.addIngredients("", 100);
            Assertions.fail("Cannot add an ingredient with a negative number");
        }
        catch ( final IllegalArgumentException iae ) {
        	//Exception caught, carry on
        }
        try {
            ivt.addIngredients(null, -1);
            Assertions.fail("Cannot add an ingredient with a negative number");
        }
        catch ( final IllegalArgumentException iae ) {
        	//Exception caught, carry on
        }
        try {
            ivt.addIngredients("Sugar", -1);
            Assertions.fail("Cannot add an ingredient with a negative number");
        }
        catch ( final IllegalArgumentException iae ) {
        	//Exception caught, carry on
        }
        
        Assertions.assertEquals( 500, ivt.getIngredients().get(0).getAmount(),
                "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
        Assertions.assertEquals( 500, ivt.getIngredients().get(1).getAmount(),
                "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
        Assertions.assertEquals( 500, ivt.getIngredients().get(2).getAmount(),
                "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
        Assertions.assertEquals( 500, ivt.getIngredients().get(3).getAmount(),
                "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );

    }

    
    @Test
    @Transactional
    public void testValidUseInventory () {
        Inventory ivt = inventoryService.getInventory();
        
        ivt.addIngredients("Coffee", 500);
        ivt.addIngredients("Pumpkin Spice", 500);
        ivt.addIngredients("Milk", 500);
        ivt.addIngredients("Sugar", 500);

        inventoryService.save( ivt );
        
        ivt = inventoryService.getInventory();
        
        Ingredient ing1 = ivt.getIngredients().get(0);
        Ingredient ing2 = ivt.getIngredients().get(1);
        Ingredient ing3 = ivt.getIngredients().get(2);
        Ingredient ing4 = ivt.getIngredients().get(3);
        
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        
        recipe.addIngredient(ing1, 25);
        recipe.addIngredient(ing2, 20);
        recipe.addIngredient(ing3, 15);
        recipe.addIngredient(ing4, 10);
        recipe.setPrice( 5 );

        ivt.useIngredients( recipe );

        inventoryService.save( ivt );
        
        ivt = inventoryService.getInventory();
        
        Assertions.assertEquals( 475, ivt.getIngredients().get(0).getAmount());
        Assertions.assertEquals( 480, ivt.getIngredients().get(1).getAmount());
        Assertions.assertEquals( 485, ivt.getIngredients().get(2).getAmount());
        Assertions.assertEquals( 490, ivt.getIngredients().get(3).getAmount());
    }
    
    @Test
    @Transactional
    public void testInvalidUseInventory () {
        Inventory ivt = inventoryService.getInventory();
        
        ivt.addIngredients("Coffee", 500);
        ivt.addIngredients("Pumpkin Spice", 500);
        ivt.addIngredients("Milk", 500);
        ivt.addIngredients("Sugar", 500);

        inventoryService.save( ivt );
        
        ivt = inventoryService.getInventory();
        
        Ingredient ing1 = ivt.getIngredients().get(0);
        Ingredient ing2 = ivt.getIngredients().get(1);
        Ingredient ing3 = ivt.getIngredients().get(2);
        Ingredient ing4 = ivt.getIngredients().get(3);
        
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        
        recipe.addIngredient(ing1, 501);
        recipe.addIngredient(ing2, 20);
        recipe.addIngredient(ing3, 15);
        recipe.addIngredient(ing4, 10);
        recipe.setPrice( 5 );

        Assertions.assertEquals( false, ivt.useIngredients( recipe ));

        inventoryService.save( ivt );
        
        ivt = inventoryService.getInventory();
        
        Assertions.assertEquals( 500, ivt.getIngredients().get(0).getAmount());
        Assertions.assertEquals( 500, ivt.getIngredients().get(1).getAmount());
        Assertions.assertEquals( 500, ivt.getIngredients().get(2).getAmount());
        Assertions.assertEquals( 500, ivt.getIngredients().get(3).getAmount());
        
        try {
            ivt.useIngredients(null);
            Assertions.fail("Cannot use null ingredients");
        }
        catch ( final IllegalArgumentException iae ) {
        	//Exception caught, carry on
        }

    }
    
    @Test
    @Transactional
    public void testValidCheckIngredient () {
        Inventory ivt = inventoryService.getInventory();
        
        Ingredient ing1 = new Ingredient("Coffee", 500);
        Ingredient ing2 = new Ingredient("Pumpkin Spice", 400);
        Ingredient ing3 = new Ingredient("Milk", 300);
        Ingredient ing4 = new Ingredient("Sugar", 200);
        Ingredient ing5 = new Ingredient("Chocolate", 0);
        
        Assertions.assertEquals( 500, ivt.checkIngredient(ing1));
        Assertions.assertEquals( 400, ivt.checkIngredient(ing2));
        Assertions.assertEquals( 300, ivt.checkIngredient(ing3));
        Assertions.assertEquals( 200, ivt.checkIngredient(ing4));
        Assertions.assertEquals( 0, ivt.checkIngredient(ing5));
        
    }
    
    @Test
    @Transactional
    public void testInvalidCheckIngredient () {
        Inventory ivt = inventoryService.getInventory();
        
        Ingredient ing1 = new Ingredient("Beetroot", -1);
        
        try {
        	ivt.checkIngredient(ing1);
        	Assertions.fail("An ingredient with a negative amount should not be allowed.");
        }
        catch ( IllegalArgumentException iae ) {
        	
        }
        
        try {
        	ivt.checkIngredient(null);
        	Assertions.fail("A null ingredient cannot be checked.");
        }
        catch ( IllegalArgumentException iae ) {
        	
        }
    }
    
    @Test
    @Transactional
    public void testToString () {
        Inventory ivt = inventoryService.getInventory();
        
        ivt.addIngredients("Coffee", 500);
        ivt.addIngredients("Pumpkin Spice", 500);
        ivt.addIngredients("Milk", 500);
        ivt.addIngredients("Sugar", 500);
        
        Assertions.assertEquals("[Coffee-500] [Pumpkin Spice-500] [Milk-500] [Sugar-500] " , ivt.toString());
    }
    
    @Test
    @Transactional
    public void testSetID () {
        Inventory ivt = inventoryService.getInventory();
        
        ivt.setId((long) 100);
        Assertions.assertEquals(100, ivt.getId());
        
        ivt.setId((long) 0);
        Assertions.assertEquals(0, ivt.getId());
        
        try {
        	ivt.setId((long) -1);
        	Assertions.fail("An id cannot be negative.");
        }
        catch ( IllegalArgumentException iae ) {
        	
        }
    }

    

}
