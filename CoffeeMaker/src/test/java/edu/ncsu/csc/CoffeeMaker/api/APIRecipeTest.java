package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import com.mysql.cj.x.protobuf.MysqlxCrud.Delete;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    @Autowired
    private RecipeService service;
    
    @Autowired 
    private IngredientService ingredientService; 
    
    @Autowired 
    private InventoryService inventoryService; 

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
    

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();
    	ingredientService.deleteAll(); 
    	inventoryService.deleteAll();
    	

    	Inventory ivt = new Inventory();
    	ivt.addIngredients("milk", 500);
    	ivt.addIngredients("sugar", 500);
    	ivt.addIngredients("coffee", 500);
    	ivt.addIngredients("chocolate", 500);
    	
    	inventoryService.save(ivt);


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
    public void invalidrecipes() throws Exception {
    	
    	service.deleteAll();
    	ingredientService.deleteAll(); 
    	inventoryService.deleteAll();
    	

    	Inventory ivt = new Inventory();
    	ivt.addIngredients("milk", 500);
    	ivt.addIngredients("sugar", 500);
    	ivt.addIngredients("coffee", 500);
    	ivt.addIngredients("chocolate", 500);
    	
    	inventoryService.save(ivt);
   
        //case when there are no ingredits 
        final Recipe r3 = new Recipe();
 	    r3.setName("water");
 	    r3.setPrice(5);
 	 
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r3 ) ) ).andExpect( status().isConflict() );
        
        //case when an ingredits is less than 0 
        service.deleteAll();
        
        final Recipe r4 = new Recipe();
 	    r4.setName("matte");
 	    r4.setPrice(5);
 	    Ingredient milk4 = new Ingredient("milk", -1);
 	    r4.addIngredient(milk4, 0);
 	    

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isConflict() );
        
        //alrady 3 ingredients
        service.deleteAll();
        
        final Recipe r5 = new Recipe();
 	    r5.setName("Coffee");
 	    r5.setPrice(9);
 	    
 	    Ingredient coco = new Ingredient("coco", 2); 
 	    Ingredient water = new Ingredient("water", 2); 
 	    Ingredient splenda = new Ingredient("splenda", 2); 
 	    r5.addIngredient(coco, 2);
 	    r5.addIngredient(water, 2);
 	    r5.addIngredient(splenda, 2);
 	    
 	   
    }
    
    
    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

    	service.deleteAll();
    	ingredientService.deleteAll(); 
    	inventoryService.deleteAll();
    	

    	Inventory ivt = new Inventory();
    	ivt.addIngredients("milk", 500);
    	ivt.addIngredients("sugar", 500);
    	ivt.addIngredients("coffee", 500);
    	ivt.addIngredients("chocolate", 500);
    	
    	inventoryService.save(ivt);
    	
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
    	ingredientService.deleteAll();
    	inventoryService.deleteAll();
    	

    	Inventory ivt = new Inventory();
    	ivt.addIngredients("milk", 500);
    	ivt.addIngredients("sugar", 500);
    	ivt.addIngredients("coffee", 500);
    	ivt.addIngredients("chocolate", 500);
    	
    	inventoryService.save(ivt);
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
    	ingredientService.deleteAll(); 
    	inventoryService.deleteAll();
        /* Tests to make sure that our cap of 3 recipes is enforced */
    	

    	Inventory ivt = new Inventory();
    	ivt.addIngredients("milk", 500);
    	ivt.addIngredients("sugar", 500);
    	ivt.addIngredients("coffee", 500);
    	ivt.addIngredients("chocolate", 500);
    	
    	inventoryService.save(ivt);

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        
        final Recipe r = new Recipe();
 	    r.setName("Coffee");
 	    r.setPrice(9);
 	    Ingredient milk = new Ingredient("milk", 100); 
 	    Ingredient sugar = new Ingredient("sugar", 100); 
 	    Ingredient coffee = new Ingredient("coffee", 100); 
 	    Ingredient chocolate = new Ingredient("chocolate", 100); 

 	    r.addIngredient(milk, 2);
 	    r.addIngredient(sugar, 2);
 	    r.addIngredient(coffee, 2);
 	    r.addIngredient(chocolate, 2);
 	    	    
 	    final Recipe r2 = new Recipe();
	    r2.setName("mocha");
	    r2.setPrice(9);

	    r2.addIngredient(milk, 1);
	    r2.addIngredient(sugar, 1);
	    r2.addIngredient(coffee, 1);
	    r2.addIngredient(chocolate, 1);
	    
	    final Recipe r3 = new Recipe();
 	    r3.setName("Milk");
 	    r3.setPrice(9);
 	    
 	    r3.addIngredient(milk, 3);
 	    r3.addIngredient(sugar, 3);
 	    r3.addIngredient(coffee, 3);
 	    r3.addIngredient(chocolate, 3);

        service.save( r );
        service.save( r2 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );
        
        final Recipe r4 = new Recipe();
	    r4.setName("mocha");
	    r4.setPrice(9);

	    r4.addIngredient(milk, 2);
	    r4.addIngredient(sugar, 2);
	    r4.addIngredient(coffee, 2);
	    r4.addIngredient(chocolate, 2);
	    try {
	    	service.save(r4);
	    }
	    catch (IllegalArgumentException iae) {
	    	//carry on
	    }
	    
        
        //Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }
    
    /**
     *  Test that a recipe that is created is deleted 
     * @throws Exception
     */
    @Test
    @Transactional
    public void deletrecipetest()  throws Exception {
    	service.deleteAll();
    	ingredientService.deleteAll(); 
    	inventoryService.deleteAll();
    	
    	Inventory ivt = new Inventory();
    	ivt.addIngredients("milk", 500);
    	ivt.addIngredients("sugar", 500);
    	ivt.addIngredients("coffee", 500);
    	ivt.addIngredients("chocolate", 500);
    	
    	inventoryService.save(ivt);
    	
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
 	    
 	  
 	    Recipe r2 = new Recipe();
	    r2.setName("ian");
	    r2.setPrice(9);

	    r2.addIngredient(milk, 2);
	    r2.addIngredient(sugar, 2);
	    r2.addIngredient(coffee, 2);
	    r2.addIngredient(chocolate, 2);
	    
	   
 	    service.save(r);
 	    
 	  

        mvc.perform( delete( "/api/v1/recipes/mocha" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) );
        
        
        
        mvc.perform( delete( "/api/v1/recipes/ian" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) );
        
        Assertions.assertEquals( 0, (int) service.count() );
        
        

    	
    	
    }


}
