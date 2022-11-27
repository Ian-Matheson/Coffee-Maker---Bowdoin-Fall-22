package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Inventory;


public interface IngredientRepository extends JpaRepository<Inventory, Long> {

}