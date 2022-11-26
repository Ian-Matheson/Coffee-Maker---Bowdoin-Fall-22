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
    @Transactional
    @Test
    public void testAddAndEditValidIngredientsInventory () {
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
            ivt.addIngredients("Sugar", -1);
            Assertions.fail("Cannot add an ingredient with a negative number");
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, ivt.getIngredients().get(0).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, ivt.getIngredients().get(1).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, ivt.getIngredients().get(2).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, ivt.getIngredients().get(3).getAmount(),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
        }

    }

    
    @Test
    @Transactional
    public void testConsumeInventory () {
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

    

}
