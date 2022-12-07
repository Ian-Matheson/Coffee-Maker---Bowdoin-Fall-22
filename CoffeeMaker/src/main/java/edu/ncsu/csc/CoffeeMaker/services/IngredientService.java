package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

/**
 * The InventoryService is used to handle CRUD operations on the Inventory
 * model. In addition to all functionality in `Service`, we also manage the
 * Inventory singleton.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class IngredientService extends Service<Ingredient, Long> {

    /**
     * IngredientRepository, to be autowired in by Spring and provide CRUD
     * operations on Ingredient model.
     */
    @Autowired
    private IngredientRepository ingredientRepository;

    
    /**
     * Retrieves the singleton IngredientRepository instance from the database, creating it
     * if it does not exist.
     *
     * @return the IngredientRepository, either new or fetched
     */
    @Override
    protected JpaRepository getRepository () {
        return ingredientRepository;
    }
    
    /**
     * Find an ingredient with the provided name
     * 
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public Ingredient findByName ( final String name ) {
        return ingredientRepository.findByName( name );
    }
    
    /**
     * Find an ingredient with the provided name
     * 
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    @Override
    public void delete ( final Ingredient ing ) {
    	List<Ingredient> ings = super.findAll();
    	for (int i=0; i < count(); i++ ) {
    		if (ings.get(i).getName().equals(ing.getName())) {
    			if (ings.get(i).getAmount().equals(ing.getAmount())) {
    				ingredientRepository.delete(ing);
    			}
    		}
    	}
    }

}