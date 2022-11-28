package edu.ncsu.csc.CoffeeMaker.DataGeneration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateIngredients {

    @Autowired
    private IngredientService ingredientService;
    
    @Autowired
    private RecipeService recipeService;

    @Test
    @Transactional
    public void testCreateIngredients() {
    	recipeService.deleteAll();
    	
        ingredientService.deleteAll();
    
        final Ingredient i1 = new Ingredient( "Coffee", 5 );

        ingredientService.save( i1 );

        final Ingredient i2 = new Ingredient( "Milk", 3 );

        ingredientService.save( i2 );
        
        Assertions.assertEquals( 2, ingredientService.count() );

    }
}