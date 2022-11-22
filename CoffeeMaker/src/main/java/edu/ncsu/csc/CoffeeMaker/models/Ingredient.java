package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

import edu.ncsu.csc.CoffeeMaker.models.enums.IngredientType;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Ingredient extends DomainObject {


    /** id for ingredient entry */
    @Id
    @GeneratedValue
    private Long    id;
    
    
	/** enum of available ingredients */
   // @Enumerated( EnumType.STRING )
    private IngredientType ingredient;
    
    
    /** amount of ingredient */
    @Min ( 0 )
    private Integer amount;
    
	public Ingredient(IngredientType ingredient, @Min(0) Integer amount) {
		super();
	    this.ingredient = ingredient;
		this.amount = amount;
	}
    
    /**
     * Empty constructor for Hibernate
     */
    public Ingredient() {

    }
    
    /**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the ingredient
	 */
	public IngredientType getIngredient() {
		return ingredient;
	}

	/**
	 * @param ingredient the ingredient to set
	 */
	public void setIngredient(IngredientType ingredient) {
		this.ingredient = ingredient;
	}

	/**
	 * @return the amount
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	    
	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", ingredient=" + ingredient + ", amount=" + amount + "]";
	}
}
