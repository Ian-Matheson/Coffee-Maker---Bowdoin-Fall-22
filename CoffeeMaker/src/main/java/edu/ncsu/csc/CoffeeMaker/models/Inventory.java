package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long    id;
    
    /** id for inventory entry */
    private List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
    	ingredients = new ArrayList<Ingredient>();
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }
    

    /**
     * Ensures that ingredient has enough units.
     *
     * @param ingredient type
     * @return checked amount of that ingredient
     * @throws IllegalArgumentException if the parameter isn't a positive integer
     */
    public Integer checkIngredient ( Ingredient ingredient ) throws IllegalArgumentException {
    	int amountIngredient = ingredient.getAmount();
        if (amountIngredient < 0) {
        	throw new IllegalArgumentException( "Units of chocolate must be a positive integer" );
        }
        return amountIngredient;
    }
    

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    //WORKS BASED OFF NAME OF INGREDIENT
    public boolean enoughIngredients ( final Recipe r ) {
    	List<Ingredient> recipeIngredients = r.getIngredients();
    	
    	for (int i=0; i < recipeIngredients.size(); i++) {
    		String recipeIngredientName = recipeIngredients.get(i).getName();
    		int recipeIngredientAmount = recipeIngredients.get(i).getAmount();
    		
    		for (int j=0; j < ingredients.size(); j++) {
    			String inventoryIngredientName = ingredients.get(j).getName();
    			
    			if (inventoryIngredientName.equals(recipeIngredientName)) {
    				int inventoryIngredientAmount = ingredients.get(j).getAmount();
    				
    				if (inventoryIngredientAmount < recipeIngredientAmount) {
    					return false;
    				}
    			}
    		}
    	}
    	return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r recipe to make
     * @return true if recipe is made.
     */
    //WORKS BASED OFF NAME OF INGREDIENT
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
        	List<Ingredient> recipeIngredients = r.getIngredients();
        	
        	for (int i=0; i < ingredients.size(); i++) {
        		int inventoryIngredientAmount = ingredients.get(i).getAmount();
        		String inventoryIngredientName = ingredients.get(i).getName();

        		for (int j=0; j < ingredients.size(); j++) {
            		String recipeIngredientName = recipeIngredients.get(j).getName();
            		int recipeIngredientAmount = recipeIngredients.get(j).getAmount();
     
        			if (inventoryIngredientName.equals(recipeIngredientName)) {
        				ingredients.get(i).setAmount( inventoryIngredientAmount - recipeIngredientAmount);
        			}
        		}
        	}
        	return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param name of ingredient
     * @param amount of ingredient
     * @return true if successful, false if not
     */
    public boolean addIngredients ( String name, Integer amount ) {
    	//I believe handeled in constructor
//        if ( amount < 0 ) {
//            throw new IllegalArgumentException( "Amount cannot be negative" );
//        }
        Ingredient ingredient = new Ingredient(name, amount);
        ingredients.add(ingredient);
        return true;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for (int i=0; i < ingredients.size(); i++) {
        	buf.append(ingredients.get(i).getName());
        	buf.append(ingredients.get(i).getAmount());
        }
        return buf.toString();
    }


    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

}
