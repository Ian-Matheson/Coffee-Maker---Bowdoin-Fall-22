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
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;



@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )


public class TestDatabaseInteractionIngredient {
	
	@Autowired
	private IngredientService ingredientService;
	
	@Autowired
	private RecipeService recipeService;
	
	/**
	 * BeforeEach clears the current Recipe Service.
	 * @throws Exception if fails to build
	 */
	@BeforeEach
	public void setUp() throws Exception {
		recipeService.deleteAll();
		ingredientService.deleteAll();
	}
	
	@Test
	@Transactional
	public void testValidIngredients(){

        Ingredient ing1 = new Ingredient("Coffee", 500);
        ingredientService.save(ing1);

	    List<Ingredient> dbIngredients = (List<Ingredient>) ingredientService.findAll();
	    assertEquals(1, dbIngredients.size());
	    Ingredient dbIngredient = dbIngredients.get(0);

	    assertEquals(ing1.getName(), dbIngredient.getName());
	    assertEquals(ing1.getAmount(), dbIngredient.getAmount());
	    assertEquals(ing1.getId(), dbIngredient.getId());    
	    
	    Ingredient ingFBN = ingredientService.findByName("Coffee");
	    assertEquals(ing1.getName(), ingFBN.getName());
	    assertEquals(ing1.getAmount(), ingFBN.getAmount());
	    
	    dbIngredient.setName("Sugar");
	    dbIngredient.setAmount(300);

	    ingredientService.save(dbIngredient);
	    
	    dbIngredients = (List<Ingredient>) ingredientService.findAll();
	    assertEquals(1, dbIngredients.size());
	    dbIngredient = dbIngredients.get(0);

        assertEquals( 1, ingredientService.count() );

        assertEquals( 300, (int) ( (Ingredient) ingredientService.findAll().get( 0 ) ).getAmount() );
        
        assertEquals("Sugar", dbIngredient.getName());
        assertEquals(300, dbIngredient.getAmount());  
	}
	
	@Test
	@Transactional
	public void testValidDeleteIngredients(){

        Ingredient ing1 = new Ingredient("Coffee", 500);
        
        ingredientService.save(ing1);
        
        Ingredient ing2 = new Ingredient("Milk", 400);
        
        ingredientService.save(ing2);

	    List<Ingredient> dbIngredients = (List<Ingredient>) ingredientService.findAll();
	    assertEquals(2, dbIngredients.size());
	    
	    Ingredient dbIngredient1 = dbIngredients.get(0);
	    assertEquals(ing1.getName(), dbIngredient1.getName());
	    assertEquals(ing1.getAmount(), dbIngredient1.getAmount());
	    assertEquals(ing1.getId(), dbIngredient1.getId());
	    
	    Ingredient dbIngredient2 = dbIngredients.get(1);
	    assertEquals(ing2.getName(), dbIngredient2.getName());
	    assertEquals(ing2.getAmount(), dbIngredient2.getAmount());
	    assertEquals(ing2.getId(), dbIngredient2.getId());
	    
	    ingredientService.delete(dbIngredient1);
	    
	    dbIngredients = (List<Ingredient>) ingredientService.findAll();
	    assertEquals(1, dbIngredients.size());

	    dbIngredient1 = dbIngredients.get(0);

	    assertEquals(ing2.getName(), dbIngredient1.getName());
	    assertEquals(ing2.getAmount(), dbIngredient1.getAmount());
	    assertEquals(ing2.getId(), dbIngredient1.getId());

	    ingredientService.delete(dbIngredient1);
	    
	    dbIngredients = (List<Ingredient>) ingredientService.findAll();
	    
	    assertEquals(0, dbIngredients.size());
	}
	//MULTIPLE INGREDIENTS WITH THE SAME NAME
}
