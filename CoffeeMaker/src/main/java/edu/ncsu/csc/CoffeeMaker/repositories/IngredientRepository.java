package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

public interface IngredientRepository extends JpaRepository <Ingredient, Long> {
	

    /**
     * Finds a Ingredient object with the provided name. Spring will generate code
     * to make this happen.
     * 
     * @param name
     *            Name of the ingredient
     * @return Found ingredient, null if none.
     */
    Ingredient findByName ( String name );
    
}

