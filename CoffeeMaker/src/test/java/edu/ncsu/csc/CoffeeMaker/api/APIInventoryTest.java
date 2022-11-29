package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
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

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.Service;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIInventoryTest {
	
	@Autowired
    private MockMvc          mvc;


    @Autowired
    private InventoryService iService;
    
    @Autowired
	private WebApplicationContext context;
    
    /**
	 * Sets up the tests.
	 */
	@Before
	public void setup () {
	    mvc = MockMvcBuilders.webAppContextSetup( context ).build();
	}
	
	@Test
    @Transactional
    public void getInventoryTest() throws Exception {
		
		Inventory inventory = new Inventory(); 
		Ingredient milk = new Ingredient("milk", 5); 
		inventory.addIngredients("milk", 5);
	
		
		String inventoryString = mvc.perform( get( ( "/api/v1/inventory") ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milk ) ) ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
	
		//Assertions.assertTrue(inventoryString.contains("milk")); 
	}
	
	@Test
    @Transactional
    public void updateInventory() throws Exception {
		
		iService.deleteAll(); 
		
		Inventory inventory = new Inventory(); 
		Ingredient milk = new Ingredient("milk", 5); 
		inventory.addIngredients("milk", 5);
		iService.save(inventory);
		
		mvc.perform( put( String.format( "/api/v1/inventory") ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milk ) ) ).andExpect( status().isOk() );
		String inventoryString = mvc.perform( get( ( "/api/v1/inventory") ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milk ) ) ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
		
		
		
		Ingredient sauce = new Ingredient("sauce", -1); 
		inventory.addIngredients("sauce", 0);
		
		mvc.perform( put( String.format("/api/v1/inventory") ).contentType( MediaType.APPLICATION_JSON )
        .content( TestUtils.asJsonString( sauce ) ) ).andExpect( status().isConflict() );
		inventoryString = mvc.perform( get( ( "/api/v1/inventory") ).contentType( MediaType.APPLICATION_JSON )
        .content( TestUtils.asJsonString( milk ) ) ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

		Assertions.assertFalse(inventoryString.contains("sauce"));
		
	}
	
	
}
