package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.repositories.RecipeRepository;

/**
 * The RecipeService is used to handle CRUD operations on the Recipe model. In
 * addition to all functionality from `Service`, we also have functionality for
 * retrieving a single Recipe by name.
 *
 * @author Kai Presler-Marshall
 *
 */
@Component
@Transactional
public class RecipeService extends Service<Recipe, Long> {

    /**
     * RecipeRepository, to be autowired in by Spring and provide CRUD
     * operations on Recipe model.
     */
    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    protected JpaRepository getRepository () {
        return recipeRepository;
    }

    /**
     * Find a recipe with the provided name
     * 
     * @param name
     *            Name of the recipe to find
     * @return found recipe, null if none
     */
    public Recipe findByName ( final String name ) {
        return recipeRepository.findByName( name );
    }
    
    /**
     * Saves the provided object into the database. If the object already
     * exists, `save()` will perform an in-place update, overwriting the
     * existing record.
     *
     * @param obj
     *            The object to save into the database.
     */
    @Override
    public void save ( final Recipe recipe ) {
    	if (findByName(recipe.getName()) == null && count() < 3) {
    		super.save(recipe);
    		//getRepository().saveAndFlush( recipe );
    	}
    	else if (findByName(recipe.getName()) != null && recipe.getId() == findByName(recipe.getName()).getId()) {
    		super.save(recipe);
    		//getRepository().saveAndFlush( recipe );
    	}
    	else {
    		throw new IllegalArgumentException();
    	}
    }
    
    /**
     * Deletes an object from the database. This will remove the object from the
     * database, but not from memory. Trying to save it again after deletion is
     * undefined behaviour. YMMV.
     *
     * @param obj
     *            The object to delete from the database.
     */
    @Override
    public void delete ( final Recipe recipe ) {
    	if (findByName(recipe.getName()) == null) {
    		throw new IllegalArgumentException();
    	}
        getRepository().delete( recipe );
    }

}
