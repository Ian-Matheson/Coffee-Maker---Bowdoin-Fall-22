package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

//Eclipse reccommend adding these, not sure if I was supposed too? (William) 
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.azul.crs.client.Inventory;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
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

	/**
	 * Sets up the tests.
	 */
	@Before
	public void setup () {
	    mvc = MockMvcBuilders.webAppContextSetup( context ).build();
	}
	
	/**
	 * Test Method for the rest API
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	@Transactional
	public void testAPI() throws UnsupportedEncodingException, Exception {
		
	
		
		String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
			    .andReturn().getResponse().getContentAsString();
		
		//Creating an Object for Testing
		if (!recipe.contains("Mocha")) {
			Recipe r = new Recipe();
	 	    r.setName("mocha");
	 	    r.setPrice(9);
	 	    r.setMilk(1);
	 	    r.setSugar(1);
	 	    r.setCoffee(2);
	 	    r.setChocolate(1);
	 	    
	 	   //for everything below this, NOT SURE IF CORRECT because I cannot test (William)
	 	    
	 	   mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
	 	            .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
	 
	 	   //Team Task - Getting Recipes
	 	   assertTrue(recipe.contains((CharSequence) r));
	 	   
	 	   //Team Task - Adding Inventory 
	 	   
	 	   //create an inventory object, not sure why it need the full path?
	 	   edu.ncsu.csc.CoffeeMaker.models.Inventory products = new edu.ncsu.csc.CoffeeMaker.models.Inventory(50, 50, 50, 50);
	 	   
	 	   //same as post 
	 	   mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
	 	            .content( TestUtils.asJsonString( products ) ) ).andExpect( status().isOk() );
	 	   
	 	   
	 	  //Team Task - Make Coffee!
	 	   //not sure about the second use of content for passing in value 9, the cost of a mocha 
	 	  mvc.perform( post( "/api/v1/makecoffee/mocha" ).contentType( MediaType.APPLICATION_JSON )
	 	            .content( TestUtils.asJsonString( r) ).content(TestUtils.asJsonString(9)) ).andExpect( status().isOk() );
			  
			}
	}


}
