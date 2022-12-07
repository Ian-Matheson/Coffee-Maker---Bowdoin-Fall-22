package edu.ncsu.csc.CoffeeMaker.api;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;


/**
 * Rest API Tests for the API Ingredient Controller class. 
 * 
 * @author srutman
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIngredientTest {
	
	 	@Autowired
	    private IngredientService service;
	 	
	 	@Autowired
	    private RecipeService rservice;
	 	
	 	@Autowired
	    private InventoryService iservice;

	    @Autowired
	    private MockMvc       mvc;
	    
	    @Autowired
		private WebApplicationContext context;

	    /**
		 * Sets up the tests.
		 */
		@Before
		public void setup () {
		    mvc = MockMvcBuilders.webAppContextSetup( context ).build();
		}
		
		/**
		 * Test Method for the rest API, getting ingredients from the Ingredients table, 
		 * and creating new ingredients and putting them in the Ingredients table. 
		 * 
		 * @throws Exception 
		 * @throws UnsupportedEncodingException 
		 */
		@Test 
		@Transactional
		public void getAndCreateIngredientTest() throws Exception {
			
			rservice.deleteAll(); 
			
			service.deleteAll();
			
			iservice.deleteAll();
			
			Ingredient milk = new Ingredient("milk", 5); 

			mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( milk ) ) ).andExpect( status().isOk() );
			
			
			//assert milk was added 
			Assertions.assertTrue(service.count() == 1); 		

			
			
			mvc.perform( get( "/api/v1/ingredients/milk" ) ).andDo( print() ).andExpect( status().isOk() )
				    .andReturn().getResponse().getContentAsString();
						
//			//cannot get ingredients that do not exist
//			mvc.perform( get( "/api/v1/ingredients/chocolate" ) ).andDo( print() ).andExpect( status().isNotFound() )
//				   .andReturn().getResponse().getContentAsString();
//			
//			mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() );
		}
		
		/**
		 * Test Method for the rest API, creating new ingredients and putting them in 
		 * the Ingredients table, and deleting ingredients from the restAPI table. 
		 * 
		 * @throws Exception 
		 * @throws UnsupportedEncodingException 
		 */
		@Test 
		@Transactional
		public void createAndDeleteIngredient() throws Exception { 
			
			rservice.deleteAll(); 
			
			service.deleteAll();
			
			iservice.deleteAll();
			
			Ingredient milk = new Ingredient("milk", 5);
			Ingredient sugar = new Ingredient("sugar", 5); 

			mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
	 	            .content( TestUtils.asJsonString( milk ) ) ).andExpect( status().isOk() );
				

			
			mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
	 	            .content( TestUtils.asJsonString( sugar ) ) ).andExpect( status().isOk() );
			
			
			//assert milk and sugar were added 
			Assertions.assertTrue(service.count() == 2); 
			
			mvc.perform(delete( "/api/v1/ingredients/milk" ) ).andExpect( status().isOk() );
//			
//			
//			//assert milk was deleted
//			Assertions.assertTrue(service.findAll().size() == 2); 
//			
//			//cannot delete an ingredient that does not exist
//			mvc.perform(delete( "/api/v1/ingredients/milk" ) ).andDo( print() ).andExpect( status().isNotFound() );
//			
//			
			
		}
		
	    
	    
	
}
