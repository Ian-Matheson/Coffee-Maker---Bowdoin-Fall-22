package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.function.BooleanSupplier;

import javax.transaction.Transactional;

//Eclipse reccommend adding these, not sure if I was supposed too? (William) 
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.invocation.MockHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.controllers.APIInventoryController;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.repositories.InventoryRepository;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler.ForAbstractMethod;


/**
 * Rest API Test for Order Beverage --> Use Case 6 
 * Oct 30th 2022
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {
	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST
	 * API
	 */
	private MockMvc               mvc;

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
 	private RecipeService recipeService;
	
	@Autowired
 	private IngredientService ingredientService;
	
	@Autowired
 	private InventoryService inventoryService;
	

	/**
	 * Sets up the tests.
	 */
	@Before
	public void setup () {
	    mvc = MockMvcBuilders.webAppContextSetup( context ).build();
	}
	
	@BeforeEach
    public void setup2 () {
    	recipeService.deleteAll();
    	ingredientService.deleteAll();
    	inventoryService.deleteAll();
    }
	
	/**
	 * Test Method for the rest API, getting recipes, adding inventory, and making coffee
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	@Transactional
	public void testAPI() throws UnsupportedEncodingException, Exception {
		
	
	    mvc = MockMvcBuilders.webAppContextSetup( context ).build();

		String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
			    .andReturn().getResponse().getContentAsString();
		
		Inventory ivt = new Inventory();
		ivt.addIngredients("milk", 500);
		ivt.addIngredients("sugar", 500);
		ivt.addIngredients("coffee", 500);
		ivt.addIngredients("chocolate", 500);
		
		inventoryService.save(ivt);
		//Creating an Object for Testing
		if (!recipe.contains("Mocha")) {
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
	 	            .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
	 	   
	 	   //Team Task - Getting Recipes
	 	   assertTrue(recipe.contains(recipe));
	 	   
	 	   
	 	   Inventory products = new Inventory(); 
	 	   
	 	   products.addIngredients("milk", 2);
	 	   products.addIngredients("sugar", 2);
	 	   products.addIngredients("coffee", 2);
	 	   products.addIngredients("chocolate", 2);
	 	   
	 	   ResultActions addingInventoryTest = mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
	 	            .content( TestUtils.asJsonString( products ) ) ).andExpect( status().isOk() );
	 	   
	 	   assertTrue(products.getIngredients().get(0).getAmount() == 2);
	 	   assertTrue(products.getIngredients().get(1).getAmount() == 2);
	 	   assertTrue(products.getIngredients().get(2).getAmount() == 2);
	 	   assertTrue(products.getIngredients().get(3).getAmount() == 2);
	 	   
	 	   //Team Task - Make Coffee!
	 	   mvc.perform( post( "/api/v1/makecoffee/mocha" ).contentType( MediaType.APPLICATION_JSON )
	 	            .content( TestUtils.asJsonString( r) ).content(TestUtils.asJsonString(9)) ).andExpect( status().isOk() );
	 	   
		}
	}
}
