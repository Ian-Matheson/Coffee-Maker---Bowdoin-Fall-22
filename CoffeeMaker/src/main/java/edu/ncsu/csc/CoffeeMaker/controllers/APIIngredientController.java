package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {
	
	
	    @Autowired
	    private IngredientService service;

	    /**
	     * REST API method to provide GET access to all ingredients in the system
	     *
	     * @return JSON representation of all ingredients
	     */
	    @GetMapping ( BASE_PATH + "/ingredients" )
	    public List<Ingredient> getIngredients () {
	        return service.findAll();
	    }

	    /**
	     * REST API method to provide GET access to a specific ingredient, as indicated
	     * by the path variable provided (the name of the recipe desired)
	     *
	     * @param name
	     *            ingredient name
	     * @return response to the request
	     */
	    @GetMapping ( BASE_PATH + "/ingredients/{name}" )
	    public ResponseEntity getIngredient ( @PathVariable ( "name" ) final String name ) {
	        final Ingredient ingredient = service.findByName( name );
	        return null == ingredient
	                ? new ResponseEntity( errorResponse( "No ingredient found with name " + name ), HttpStatus.NOT_FOUND )
	                : new ResponseEntity( ingredient, HttpStatus.OK );
	    }

	    /**
	     * REST API method to provide POST access to the Ingredient model. This is used
	     * to create a new Ingredient by automatically converting the JSON RequestBody
	     * provided to a Ingredient object. Invalid JSON will fail.
	     *
	     * @param ingredient
	     *            The valid Ingredient to be saved.
	     * @return ResponseEntity indicating success if the Ingredient could be saved to
	     *         the inventory, or an error if it could not be
	     */
	    @PostMapping ( BASE_PATH + "/ingredients" )
	    public ResponseEntity createIngredient ( @RequestBody final Ingredient ingredient ) {
	        if ( null != service.findByName( ingredient.getName() ) ) {
	            return new ResponseEntity( errorResponse( "Ingredient with the name " + ingredient.getName() + " already exists" ),
	                    HttpStatus.CONFLICT );
	        }
	        if ( 0 >= ingredient.getAmount() ) {
	        	return new ResponseEntity( errorResponse( "Amount of " + ingredient.getAmount() + "is not a positive integer" ),
	                    HttpStatus.CONFLICT );
	        }
        		service.save( ingredient );
	            return new ResponseEntity(successResponse( ingredient.getName() + " successfully created" ), HttpStatus.OK );
	    }

	    /**
	     * REST API method to allow deleting a Ingredient from the CoffeeMaker's
	     * Inventory, by making a DELETE request to the API endpoint and indicating
	     * the ingredient to delete (as a path variable)
	     *
	     * @param name
	     *            The name of the Ingredient to delete
	     * @return Success if the ingredient could be deleted; an error if the ingredient
	     *         does not exist
	     */
	    @DeleteMapping ( BASE_PATH + "/ingredients/{name}" )
	    public ResponseEntity deleteIngredient ( @PathVariable final String name ) {
	        final Ingredient ingredient = service.findByName( name );
	        if ( null == ingredient ) {
	            return new ResponseEntity( errorResponse( "No ingredient found for name " + name ), HttpStatus.NOT_FOUND );
	        }
	        service.delete( ingredient );

	        return new ResponseEntity( successResponse( name + " was deleted successfully" ), HttpStatus.OK );
	    }
	    
	    @PutMapping (BASE_PATH + "/ingredients/{name}" )
	    public ResponseEntity editIngredient ( @PathVariable final String name, final String newName) {
	    	final Ingredient ingredient = service.findByName( name ); 
	    	 if ( null == ingredient ) {
		            return new ResponseEntity( errorResponse( "No ingredient found for name " + name ), HttpStatus.NOT_FOUND );
		        }
	    	 service.delete(ingredient); 
	    	 ingredient.setName(newName); 
	    	 service.save(ingredient);
	    	 return new ResponseEntity( successResponse( name + " was edited successfully" ), HttpStatus.OK );	    	
	    }
	    
	    @PutMapping (BASE_PATH + "/ingredients/{name}" )
	    public ResponseEntity editIngredient ( @PathVariable final String name, final int newAmount) {
	    	final Ingredient ingredient = service.findByName( name ); 
	    	 if ( null == ingredient ) {
		            return new ResponseEntity( errorResponse( "No ingredient found for name " + name ), HttpStatus.NOT_FOUND );
		        }
	    	 service.delete(ingredient); 
	    	 ingredient.setAmount(newAmount); 
	    	 service.save(ingredient);
	    	 return new ResponseEntity( successResponse( name + " was edited successfully" ), HttpStatus.OK );	    	
	    }
	    
	    
	    
}


