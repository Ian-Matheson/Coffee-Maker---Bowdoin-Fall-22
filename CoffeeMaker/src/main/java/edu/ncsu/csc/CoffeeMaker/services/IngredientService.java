package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;
import edu.ncsu.csc.CoffeeMaker.repositories.InventoryRepository;

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
    @Enumerated ( EnumType.STRING )
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
    
    public Ingredient findByName ( final String name ) {
        return ingredientRepository.findByName( name );
    }

}