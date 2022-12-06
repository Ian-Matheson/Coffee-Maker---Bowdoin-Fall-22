package edu.ncsu.csc.CoffeeMaker.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
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
     * Gets the ingredients in the inventory.
     *
     * @return List of ingredients
     */
    public List<Ingredient> getIngredients () {
    	return ingredients;
    }
    
    
    /**
     * Ensures that ingredient has enough units.
     *
     * @param ingredient type
     * @return checked amount of that ingredient
     * @throws IllegalArgumentException if the parameter isn't a positive integer 
     * or if ingredient is null
     */
    public Integer checkIngredient ( Ingredient ingredient ) throws IllegalArgumentException {
    	if (ingredient == null) {
        	throw new IllegalArgumentException( "ingredient cannot be null" );
        }
    	int amountIngredient = ingredient.getAmount();
        if (amountIngredient < 0) {
        	throw new IllegalArgumentException( "Units of ingredient must be a positive integer" );
        }
        return amountIngredient;
    }
    

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     * 
     * @throws IllegalArgumentException iae if the recipe is null
     */
    //WORKS BASED OFF NAME OF INGREDIENT
    public boolean enoughIngredients ( final Recipe r ) {
    	if (r == null) {
        	throw new IllegalArgumentException( "recipe cannot be null" );
        }
    	
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
     * @throws IllegalArgumentException iae if the recipe is null
     */
    public boolean useIngredients ( final Recipe r ) {

        if ( enoughIngredients( r ) ) {
        	List<Ingredient> recipeIngredients = r.getIngredients();
        	
        	for (int i=0; i < ingredients.size(); i++) {
        		int inventoryIngredientAmount = ingredients.get(i).getAmount();
        		String inventoryIngredientName = ingredients.get(i).getName();
        	
        		for (int j=0; j < recipeIngredients.size(); j++) {
            		String recipeIngredientName = recipeIngredients.get(j).getName();
            		int recipeIngredientAmount = recipeIngredients.get(j).getAmount();
            		
        			if (inventoryIngredientName.equals(recipeIngredientName)) {
        				int newAmount = inventoryIngredientAmount - recipeIngredientAmount;
        				Ingredient newIng = new Ingredient(inventoryIngredientName, newAmount);
        				ingredients.set(i, newIng);
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
     * 
     * @throws IllegalArgumentException iae if amount is less than 0, name is null or empty string.
     */
    public boolean addIngredients ( String name, Integer amount ) {
    	if (amount < 0 || name == null || "".equals(name)) {
    		throw new IllegalArgumentException();
    	}
    	
    	//searching to see if ingredient already exists
    	for (int i=0; i < ingredients.size(); i++) {
    		if (ingredients.get(i).getName().equals(name)) {
    			ingredients.get(i).setAmount(amount);
    			return true;
    		}
    	}
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
        	buf.append("[" + ingredients.get(i).getName() + "-");
        	buf.append(ingredients.get(i).getAmount() + "] ");
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
    	if (id < 0) {
    		throw new IllegalArgumentException();
    	}
        this.id = id;
    }

}
