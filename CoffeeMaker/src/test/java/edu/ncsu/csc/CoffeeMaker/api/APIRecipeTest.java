package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    @Autowired
    private RecipeService service;

    @Autowired
    private MockMvc       mvc;
    
    @Autowired
	private WebApplicationContext context;

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
 	    r.setName("mocha");
 	    r.setPrice(9);
 	    Ingredient milk = new Ingredient("milk", 2); 
 	    Ingredient sugar = new Ingredient("sugar", 2); 
 	    Ingredient coffee = new Ingredient("coffee", 2); 
 	    Ingredient chocolate = new Ingredient("chocolate", 2); 

 	    r.addIngredient(milk, 2);
 	    r.addIngredient(sugar, 2);
 	    r.addIngredient(coffee, 2);
 	    r.addIngredient(chocolate, 2);

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }
    
    
    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        Recipe r = new Recipe();
 	    r.setName("mocha");
 	    r.setPrice(9);
 	    Ingredient milk = new Ingredient("milk", 2); 
 	    Ingredient sugar = new Ingredient("sugar", 2); 
 	    Ingredient coffee = new Ingredient("coffee", 2); 
 	    Ingredient chocolate = new Ingredient("chocolate", 2); 

 	    r.addIngredient(milk, 2);
 	    r.addIngredient(sugar, 2);
 	    r.addIngredient(coffee, 2);
 	    r.addIngredient(chocolate, 2);

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {
        service.deleteAll();

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        Recipe r = new Recipe();
 	    r.setName(name);
 	    r.setPrice(9);
 	    Ingredient milk = new Ingredient("milk", 2); 
 	    Ingredient sugar = new Ingredient("sugar", 2); 
 	    Ingredient coffee = new Ingredient("coffee", 2); 
 	    Ingredient chocolate = new Ingredient("chocolate", 2); 

 	    r.addIngredient(milk, 2);
 	    r.addIngredient(sugar, 2);
 	    r.addIngredient(coffee, 2);
 	    r.addIngredient(chocolate, 2);

        service.save( r );

        Recipe r2 = new Recipe();
 	    r.setName(name);
 	    r.setPrice(9);

 	    r2.addIngredient(milk, 2);
 	    
 	    mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {
        service.deleteAll();

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        
        final Recipe r = new Recipe();
 	    r.setName("Coffee");
 	    r.setPrice(9);
 	    Ingredient milk = new Ingredient("milk", 2); 
 	    Ingredient sugar = new Ingredient("sugar", 2); 
 	    Ingredient coffee = new Ingredient("coffee", 2); 
 	    Ingredient chocolate = new Ingredient("chocolate", 2); 

 	    r.addIngredient(milk, 2);
 	    r.addIngredient(sugar, 2);
 	    r.addIngredient(coffee, 2);
 	    r.addIngredient(chocolate, 2);
 	    	    
 	    final Recipe r2 = new Recipe();
	    r.setName("mocha");
	    r.setPrice(9);

	    r.addIngredient(milk, 2);
	    r.addIngredient(sugar, 2);
	    r.addIngredient(coffee, 2);
	    r.addIngredient(chocolate, 2);
	    
	    final Recipe r3 = new Recipe();
 	    r.setName("Milk");
 	    r.setPrice(9);
 	    
 	    r.addIngredient(milk, 2);
 	    r.addIngredient(sugar, 2);
 	    r.addIngredient(coffee, 2);
 	    r.addIngredient(chocolate, 2);

        service.save( r );
        service.save( r2 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );
        
        final Recipe r4 = new Recipe();
	    r.setName("mocha");
	    r.setPrice(9);

	    r.addIngredient(milk, 2);
	    r.addIngredient(sugar, 2);
	    r.addIngredient(coffee, 2);
	    r.addIngredient(chocolate, 2);
	    
	    //????
	    service.save(r4);
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }


}
