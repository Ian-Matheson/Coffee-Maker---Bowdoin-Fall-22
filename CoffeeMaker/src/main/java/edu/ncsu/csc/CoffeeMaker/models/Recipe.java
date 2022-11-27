package edu.ncsu.csc.CoffeeMaker.models;

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
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long    id;

    /** Recipe name */
    private String  name;
    
    /** ingredients */
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> ingredients;

    /** Recipe price */
    @Min ( 0 )
    private Integer price;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        ingredients = new ArrayList();
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
    	for (int i=0; i < ingredients.size(); i++) {
    		if (ingredients.get(0).getAmount() != 0) {
    			return false;
    		}
    	}
        return true;
    }
    
    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }
    
    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }
    
    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }
    
    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }


    /**
     * Adds an ingredient to the recipe
     *
     * @return true if the ingredient is successfully added, otherwise return false
     */
    public boolean addIngredient (Ingredient ingredient, int amount) {
    	//Maybe need cases for when can't add ingredient? like when ingredient null?
    	//TODO  check for duplicates! Make sure ingredient is in inventory!
    	Ingredient temp = new Ingredient(ingredient.getName(), amount);
    	ingredients.add(temp);
    	return true;
    }
    
    /**
     * Edit an ingredient currently in the recipe
     *
     * @return true if the ingredient is successfully added, otherwise return false
     */
    //Would these two ingredients ever have different amounts? NO
    public boolean editIngredient (Ingredient ingredient, int amount) {
    	for (int i=0; i < ingredients.size(); i++) {
    		//CHANGED THIS TO NAME BECAUSE OUR EQUALS METHOD ISN'T SET UP
    		if (ingredients.get(i).getName().equals(ingredient.getName())) {
    			ingredient.setAmount(amount);
    			ingredients.set(i, ingredient);
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Gets a list of all the ingredients in the recipe
     *
     * @return ingredients in list
     */
    public List<Ingredient> getIngredients () {
    	return ingredients;
    }
    
    /**
     * Removes an ingredient from the recipe
     *
     * @return true if the ingredient is successfully added, otherwise return false
     */
    //Would these two ingredients ever have different amounts?
    public boolean removeIngredient (Ingredient ingredient) {
    	return ingredients.remove(ingredient);
    }
   

    /**
     * Updates the fields to be equal to the passed Recipe
     *
     * @param r with updated fields
     */
    //DONT RLY GET THE POINT OF THIS
    public void updateRecipe ( final Recipe r ) {
//    	for (int i=0; i < ingredients.size(); i++) {
//    		String currName = ingredients.get(i).getName();
//    		
//    		for (int j=0; j < r.getIngredients().size(); j++) {
//    			String newName = r.getIngredients().get(j).getName();
//    			if (currName.equals(newName)) {
//    				int newAmount = r.getIngredients().get(j).getAmount();
//    				ingredients.get(i).setAmount(newAmount);
//    			}
//    		}
//    	}
    	ingredients = r.getIngredients();
        setPrice( r.getPrice() );
        setName( r.getName() );
    }
    
    @Override
	public String toString() {
		return "Recipe " + name + " with ingredients " + ingredients + "]";
	}

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
    	if ( name == obj)
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}