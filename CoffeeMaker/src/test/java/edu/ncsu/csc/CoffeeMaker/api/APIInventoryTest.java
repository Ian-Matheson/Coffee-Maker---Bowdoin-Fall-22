package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

public class APIInventoryTest {
	
	@Autowired
    private MockMvc          mvc;


    @Autowired
    private InventoryService iService;
	
	@Test
    @Transactional
    public void getInventoryTest() throws Exception {
		
		Inventory inventory = new Inventory(); 
		Ingredient milk = new Ingredient("milk", 5); 
		inventory.addIngredients("milk", 5);
		
		mvc.perform( get( String.format( "/api/v1/inventory") ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( milk ) ) ).andExpect( status().isOk() );
		
		}
	}
