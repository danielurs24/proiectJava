package com.dursu.proiect.repository;

import com.dursu.proiect.entity.Category;
import com.dursu.proiect.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	List<Product> findAll();
	
	List<Product> findAllByName(String name);
	
	Optional<Product> findByName(String name);
	
	List<Product> findByNameContainingIgnoreCase(String name);
	
	List<Product> findByCategory(Category category);
}
