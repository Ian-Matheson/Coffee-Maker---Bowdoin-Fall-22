package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

/**
 * InventoryRepository is used to provide CRUD operations for the Inventory
 * model. Spring will generate appropriate code with JPA.
 *
 * @author Kai Presler-Marshall
 *
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	
	/**
     * Finds a Recipe object with the provided name. Spring will generate code
     * to make this happen.
     * 
     * @param name
     *            Name of the recipe
     * @return Found recipe, null if none.
     */
    Ingredient findByName ( String name );
}
