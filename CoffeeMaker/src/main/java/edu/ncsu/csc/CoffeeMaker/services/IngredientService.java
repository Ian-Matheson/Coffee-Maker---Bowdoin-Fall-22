package edu.ncsu.csc.CoffeeMaker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;
import edu.ncsu.csc.CoffeeMaker.repositories.RecipeRepository;

public class IngredientService extends Service<Recipe, Long>{

	@Autowired
    private IngredientRepository ingredientRepository;

    @Override
    protected JpaRepository getRepository () {
        return ingredientRepository;
    }
    
    public Ingredient findByName ( final String name ) {
        return ingredientRepository.findByName( name );
    }
    
    
    
}
