package edu.ncsu.csc.CoffeeMaker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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
	
	@Test
	@Transactional
	public void testRecipes(){
		recipeService.deleteAll();
		
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
	
	@Test
	@Transactional
	public void testValidDeleteRecipes(){
		recipeService.deleteAll();
		
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

	    assertEquals(1, dbRecipes.size());

	    assertEquals(r.getName(), dbRecipe.getName());
	    assertEquals(r.getPrice(), dbRecipe.getPrice());
	    assertEquals(r.getMilk(), dbRecipe.getMilk());
	    assertEquals(r.getSugar(), dbRecipe.getSugar());
	    assertEquals(r.getCoffee(), dbRecipe.getCoffee());
	    assertEquals(r.getChocolate(), dbRecipe.getChocolate());
	    
	    recipeService.delete(dbRecipe);
	    
	    assertEquals(0, dbRecipes.size());

  
	}
	
	@Test
	@Transactional
	public void testInvalidDeleteRecipes(){
		//TODO
	}
	
	
}
